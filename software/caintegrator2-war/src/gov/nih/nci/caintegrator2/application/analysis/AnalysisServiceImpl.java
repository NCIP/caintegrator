/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.application.CaIntegrator2BaseService;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotHandler;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.analysis.grid.GenePatternGridRunner;
import gov.nih.nci.caintegrator2.application.analysis.grid.gistic.GisticSamplesMarkers;
import gov.nih.nci.caintegrator2.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator2.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotService;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotService;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.GenePatternUtil;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ParameterException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.genepattern.webservice.WebServiceException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the AnalysisService subsystem.
 */
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class AnalysisServiceImpl extends CaIntegrator2BaseService implements AnalysisService {
    
    private KMPlotService kmPlotService;
    private GeneExpressionPlotService gePlotService;
    private QueryManagementService queryManagementService;
    private GenePatternClientFactory genePatternClientFactory;
    private GenePatternGridRunner genePatternGridRunner;
    private FileManager fileManager;
    
    /**
     * {@inheritDoc}
     */
    public List<AnalysisMethod> getGenePatternMethods(ServerConnectionProfile server)
            throws WebServiceException {
        return new GenePatternHelper(retrieveClient(server)).getMethods();
    }

    private CaIntegrator2GPClient retrieveClient(ServerConnectionProfile server) throws WebServiceException {
        return genePatternClientFactory.retrieveOldGenePatternClient(server);
    }

    /**
     * {@inheritDoc}
     */
    public JobInfoWrapper executeGenePatternJob(ServerConnectionProfile server, AnalysisMethodInvocation invocation) 
    throws WebServiceException {
        JobInfoWrapper jobInfo = new GenePatternHelper(retrieveClient(server)).execute(invocation);
        URL serviceUrl;
        URL resultUrl;
        try {
            serviceUrl = new URL(server.getUrl());
            resultUrl = new URL(serviceUrl.getProtocol(), serviceUrl.getHost(), serviceUrl.getPort(), 
                    "/gp/pages/jobResults.jsf");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL provided for server " + server.getUrl(), e);
        }
        jobInfo.setUrl(resultUrl);
        return jobInfo;
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridPreprocessComparativeMarker(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job) 
        throws ConnectionException, InvalidCriterionException {
        StudySubscription studySubscription = getDao().get(job.getSubscription().getId(), StudySubscription.class);
        PreprocessDatasetParameters preprocessParams = job.getForm().getPreprocessDatasetparameters();
        job.setSubscription(studySubscription);
        GctDataset gctDataset = createGctDataset(studySubscription, preprocessParams.getClinicalQueries(), 
                preprocessParams.getPlatformName(), preprocessParams.getExcludedControlSampleSet());
        File gctFile = createGctFile(gctDataset, studySubscription,
                preprocessParams.getProcessedGctFilename());
        File clsFile = createClassificationFile(studySubscription, 
                job.getForm().getComparativeMarkerSelectionParameters().getClinicalQueries(), 
                job.getForm().getComparativeMarkerSelectionParameters().getClassificationFileName(), 
                gctDataset.getColumnSampleNames());
        job.setInputZipFile(fileManager.createInputZipFile(studySubscription, job,
                "CMS_INPUT_" +  System.currentTimeMillis() + ".zip", 
                gctFile, clsFile));
        return genePatternGridRunner.runPreprocessComparativeMarkerSelection(updater, job, gctFile, clsFile);
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job) 
        throws ConnectionException, InvalidCriterionException, ParameterException, IOException {
        StudySubscription studySubscription = getDao().get(job.getSubscription().getId(), StudySubscription.class); 
        job.setSubscription(studySubscription);
        GisticSamplesMarkers gisticSamplesMarkers = 
            GenePatternUtil.createGisticSamplesMarkers(queryManagementService, 
                    job.getGisticAnalysisForm().getGisticParameters(), job.getSubscription());
        List<File> inputFiles = new ArrayList<File>();
        File segmentFile = fileManager.createSamplesFile(studySubscription, gisticSamplesMarkers.getSamples());
        File markersFile = fileManager.createMarkersFile(studySubscription, gisticSamplesMarkers.getMarkers());
        File cnvFile = job.getGisticAnalysisForm().getGisticParameters().getCnvSegmentsToIgnoreFile();
        if (cnvFile != null) {
            File newCnvFile = fileManager.renameCnvFile(cnvFile);
            inputFiles.add(newCnvFile);
        }
        inputFiles.add(segmentFile);
        inputFiles.add(markersFile);
        job.setInputZipFile(fileManager.createInputZipFile(studySubscription, job,
                "GISTIC_INPUT_" +  System.currentTimeMillis() + ".zip", 
                inputFiles.toArray(new File[inputFiles.size()])));
        if (job.isGridServiceCall()) {
            return genePatternGridRunner.runGistic(updater, job, segmentFile, markersFile, cnvFile);
        } else {
            return runGisticWebService(job, updater, segmentFile, markersFile);
        }
    }
    
    private File runGisticWebService(GisticAnalysisJob job, StatusUpdateListener updater, 
            File segmentFile, File markersFile) 
    throws ConnectionException, InvalidCriterionException, IOException {
        ServerConnectionProfile server = job.getGisticAnalysisForm().getGisticParameters().getServer();
        GisticWebServiceRunner gisticWebServiceRunner;
        try {
            gisticWebServiceRunner = new GisticWebServiceRunner(retrieveClient(server), getFileManager());
            return gisticWebServiceRunner.runGistic(updater, job, segmentFile, markersFile);
        } catch (WebServiceException e) {
            throw new ConnectionException("Unable to connect to GenePattern.", e);
        }
        
    }
    
    /**
     * {@inheritDoc}
     */
    public File executeGridPCA(StatusUpdateListener updater,
            PrincipalComponentAnalysisJob job) throws ConnectionException, InvalidCriterionException {
        StudySubscription studySubscription = getDao().get(job.getSubscription().getId(), StudySubscription.class); 
        job.setSubscription(studySubscription);
        PCAParameters parameters = job.getForm().getPcaParameters();
        File gctFile = createGctFile(createGctDataset(studySubscription, parameters.getClinicalQueries(), 
                parameters.getPlatformName(), parameters.getExcludedControlSampleSet()),
                studySubscription, parameters.getGctFileName());
        job.setInputZipFile(fileManager.createInputZipFile(studySubscription, job,
                "PCA_INPUT_" +  System.currentTimeMillis() + ".zip", 
                gctFile));
        return genePatternGridRunner.runPCA(updater, job, gctFile);
    }

    /**
     * {@inheritDoc}
     */
    public KMPlot createKMPlot(StudySubscription subscription, AbstractKMParameters kmParameters) 
        throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        AbstractKMPlotHandler kmPlotHandler = AbstractKMPlotHandler.createKMPlotHandler(
            subscription, getDao(), kmParameters.getSurvivalValueDefinition(), queryManagementService, kmParameters);
        kmPlotHandler.setupAndValidateParameters(this);
        return kmPlotHandler.createPlot(kmPlotService);

    }
    
    /**
     * {@inheritDoc}
     */
    public GeneExpressionPlotGroup createGeneExpressionPlot(StudySubscription studySubscription,
            AbstractGEPlotParameters plotParameters) 
    throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        if (StringUtils.isNotBlank(plotParameters.getGeneSymbol())) {
            plotParameters.getGenesNotFound().clear();
            plotParameters.getGenesNotFound().addAll(validateGeneSymbols(studySubscription, 
                    Cai2Util.createListFromCommaDelimitedString(plotParameters.getGeneSymbol())));
        }
        AbstractGEPlotHandler gePlotHandler = AbstractGEPlotHandler.createGeneExpressionPlotHandler(
                getDao(), queryManagementService, plotParameters, gePlotService);
        
        return gePlotHandler.createPlots(studySubscription);
    }

    /**
     * {@inheritDoc}
     */
    public List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
            throws GenesNotFoundInStudyException {
        return queryManagementService.validateGeneSymbols(studySubscription, geneSymbols);
    }
    
    private File createClassificationFile(StudySubscription studySubscription, List<Query> clinicalQueries,
            String clsFilename, List<String> sampleColumnOrdering) throws InvalidCriterionException {
        return fileManager.createClassificationFile(studySubscription, 
                GenePatternUtil.createSampleClassification(
                        queryManagementService, clinicalQueries, sampleColumnOrdering), 
                clsFilename);
    }
    
    private File createGctFile(GctDataset gctDataset, StudySubscription studySubscription, String filename)
    throws InvalidCriterionException {
        return fileManager.createGctFile(studySubscription, gctDataset, filename);
    }

    private GctDataset createGctDataset(StudySubscription studySubscription, Collection<Query> querySet,
            String platformName, SampleSet excludedSet) throws InvalidCriterionException {
        SampleSet refreshedExcludedSet = excludedSet == null ? null : getRefreshedEntity(excludedSet);
        return GenePatternUtil.createGctDataset(studySubscription, querySet,
                refreshedExcludedSet, queryManagementService, platformName);
    }
    
    /**
     * {@inheritDoc}
     */
    public StudySubscription getRefreshedStudySubscription(StudySubscription studySubscription) {
        StudySubscription refreshedStudySubscription = getRefreshedEntity(studySubscription);
        HibernateUtil.loadCollection(refreshedStudySubscription);
        return refreshedStudySubscription;
    }

    /**
     * @return the kmPlotService
     */
    public KMPlotService getKmPlotService() {
        return kmPlotService;
    }

    /**
     * @param kmPlotService the kmPlotService to set
     */
    public void setKmPlotService(KMPlotService kmPlotService) {
        this.kmPlotService = kmPlotService;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * @return the gePlotService
     */
    public GeneExpressionPlotService getGePlotService() {
        return gePlotService;
    }

    /**
     * @param gePlotService the gePlotService to set
     */
    public void setGePlotService(GeneExpressionPlotService gePlotService) {
        this.gePlotService = gePlotService;
    }

    /**
     * @return the genePatternClientFactory
     */
    public GenePatternClientFactory getGenePatternClientFactory() {
        return genePatternClientFactory;
    }

    /**
     * @param genePatternClientFactory the genePatternClientFactory to set
     */
    public void setGenePatternClientFactory(GenePatternClientFactory genePatternClientFactory) {
        this.genePatternClientFactory = genePatternClientFactory;
    }

    /**
     * @return the genePatternGridRunner
     */
    public GenePatternGridRunner getGenePatternGridRunner() {
        return genePatternGridRunner;
    }

    /**
     * @param genePatternGridRunner the genePatternGridRunner to set
     */
    public void setGenePatternGridRunner(GenePatternGridRunner genePatternGridRunner) {
        this.genePatternGridRunner = genePatternGridRunner;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    public void deleteAnalysisJob(Long jobId) {
        AbstractPersistedAnalysisJob job = getAnalysisJob(jobId);
        job.getSubscription().getAnalysisJobCollection().remove(job);
        deleteFiles(job);
        getDao().delete(job);
    }
    
    private void deleteFiles(AbstractPersistedAnalysisJob job) {
        if (job.getResultsZipFile() != null) {
            FileUtils.deleteQuietly(job.getResultsZipFile().getFile());
        }
        if (job.getInputZipFile() != null) {
            FileUtils.deleteQuietly(job.getInputZipFile().getFile());
        }
    }

    private AbstractPersistedAnalysisJob getAnalysisJob(Long id) {
        return getDao().get(id, AbstractPersistedAnalysisJob.class);
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }
}
