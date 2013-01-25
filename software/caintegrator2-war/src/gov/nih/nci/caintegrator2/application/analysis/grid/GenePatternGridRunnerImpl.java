/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid;

import gov.nih.nci.caintegrator2.application.analysis.GenePatternGridClientFactory;
import gov.nih.nci.caintegrator2.application.analysis.StatusUpdateListener;
import gov.nih.nci.caintegrator2.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionGridRunner;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticGridRunner;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAGridRunner;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetGridRunner;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ParameterException;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.genepattern.cagrid.service.compmarker.mage.common.ComparativeMarkerSelMAGESvcI;
import org.genepattern.cagrid.service.preprocessdataset.mage.common.PreprocessDatasetMAGEServiceI;
import org.genepattern.gistic.common.GisticI;
import org.genepattern.pca.common.PCAI;

/**
 * Entry point to run all GenePattern grid jobs.
 */
public class GenePatternGridRunnerImpl implements GenePatternGridRunner {
    
    private GenePatternGridClientFactory genePatternGridClientFactory;
    private FileManager fileManager;
    
    /**
     * {@inheritDoc}
     */
    public File runGistic(StatusUpdateListener updater, GisticAnalysisJob job, File segmentFile, File markersFile,
            File cnvFile) 
    throws ConnectionException, InvalidCriterionException, ParameterException, IOException {
        GisticParameters parameters = job.getGisticAnalysisForm().getGisticParameters();
        StudySubscription studySubscription = job.getSubscription();
        GisticI gisticClient = genePatternGridClientFactory.createGisticClient(parameters.getServer());
        GisticGridRunner runner = new GisticGridRunner(gisticClient, fileManager);
        try {
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_REMOTELY);
            return runner.execute(studySubscription, parameters, segmentFile, markersFile, cnvFile);
        } catch (InterruptedException e) {
            return null;
        } finally {
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_LOCALLY);
        }
    }
    
    /**
     * {@inheritDoc}
     * @throws InvalidCriterionException 
     */
    public File runPreprocessComparativeMarkerSelection(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job, File gctFile, File clsFile)
    throws ConnectionException, InvalidCriterionException {
        StudySubscription studySubscription = job.getSubscription();
        runPreprocessDataset(updater, job, job.getForm().getPreprocessDatasetparameters(),
                gctFile);
        File resultsFile = runComparativeMarkerSelection(updater, studySubscription, job, gctFile, clsFile);
        updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_LOCALLY);
        
        return resultsFile;
    }

    /**
     * {@inheritDoc}
     */
    public void runPreprocessDataset(StatusUpdateListener updater,
            AbstractPersistedAnalysisJob job, PreprocessDatasetParameters parameters, File gctFile)
    throws ConnectionException, InvalidCriterionException {
        StudySubscription studySubscription = job.getSubscription();
        updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_REMOTELY);
        PreprocessDatasetMAGEServiceI client = 
            genePatternGridClientFactory.createPreprocessDatasetClient(parameters.getServer());
        PreprocessDatasetGridRunner runner = new PreprocessDatasetGridRunner(client);
        try {
            runner.execute(studySubscription, parameters, gctFile);
        } catch (InterruptedException e) {
            return;
        } finally {
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_LOCALLY);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public File runPCA(StatusUpdateListener updater,
            PrincipalComponentAnalysisJob job, File gctFile)
    throws ConnectionException, InvalidCriterionException {
        PCAI client = genePatternGridClientFactory.createPCAClient(job.getForm().getPcaParameters().getServer());
        if (job.getForm().isUsePreprocessDataset()) {
            runPreprocessDataset(updater,
                    job, job.getForm().getPreprocessParameters(), gctFile);
        }
        return executePCA(updater, job, client, gctFile);
    }
    
    private File executePCA(StatusUpdateListener updater,
            PrincipalComponentAnalysisJob job, PCAI client, File gctFile)
    throws ConnectionException {
        File zipFile = null;
        try {
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_REMOTELY);
            PCAGridRunner runner = new PCAGridRunner(client, fileManager);
            zipFile = runner.execute(job.getSubscription(), job.getForm().getPcaParameters(), gctFile);
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_LOCALLY);
            if (Cai2Util.isValidZipFile(zipFile)) {
                 Cai2Util.addFilesToZipFile(zipFile, gctFile);
            }    
        } catch (IOException e) {
            FileUtils.deleteQuietly(zipFile);
            throw new IllegalStateException("Invalid Zip File retrieved from grid service:  " 
                                            + "Unable to add gct/cls files to the zip file.", e);
        } catch (InterruptedException e) {
            return null;
        } finally {
            FileUtils.deleteQuietly(gctFile);
        }
        return zipFile;
    }
    
    private File runComparativeMarkerSelection(StatusUpdateListener updater,
            StudySubscription subscription, ComparativeMarkerSelectionAnalysisJob job, 
            File gctFile, File clsFile) 
    throws ConnectionException {
        File zipFile = null;
        try {
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_REMOTELY);
            ComparativeMarkerSelMAGESvcI client = genePatternGridClientFactory.createComparativeMarkerSelClient(job
                    .getForm().getComparativeMarkerSelectionParameters().getServer());
            ComparativeMarkerSelectionGridRunner runner = new ComparativeMarkerSelectionGridRunner(client, fileManager);
            zipFile = runner.execute(subscription, job.getForm().getComparativeMarkerSelectionParameters(), gctFile,
                    clsFile);
            updateStatus(updater, job, AnalysisJobStatusEnum.PROCESSING_LOCALLY);
            Cai2Util.isValidZipFile(zipFile);
            Cai2Util.addFilesToZipFile(zipFile, gctFile, clsFile);
        } catch (IOException e) {
            if (zipFile != null) {
                zipFile.delete();
            }
            throw new IllegalStateException("Invalid Zip File retrieved from grid service:  "
                    + "Unable to add gct/cls files to the zip file.", e);
        } catch (InterruptedException e) {
            return null;
        } finally {
            FileUtils.deleteQuietly(gctFile);
            FileUtils.deleteQuietly(clsFile);
        }
        return zipFile;
    }

    /**
     * @param genePatternGridClientFactory the genePatternGridClientFactory to set
     */
    public void setGenePatternGridClientFactory(GenePatternGridClientFactory genePatternGridClientFactory) {
        this.genePatternGridClientFactory = genePatternGridClientFactory;
    }


    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
    
    private void updateStatus(StatusUpdateListener updater,
            AbstractPersistedAnalysisJob job, AnalysisJobStatusEnum status) {
        job.setStatus(status);
        updater.updateStatus(job);
    }

}
