/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid;

import gov.nih.nci.caintegrator.application.analysis.StatusUpdateListener;
import gov.nih.nci.caintegrator.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ParameterException;

import java.io.File;
import java.io.IOException;

/**
 * Entry point to run all GenePattern grid jobs.
 */
public interface GenePatternGridRunner {

    /**
     * Runs the GenePattern Grid Services PreprocessDataset followed by ComparativeMarkerSelection on the 
     * preprocessed data and returns the Marker Results.
     * @param updater the ajax updater.
     * @param job the Analysis job.
     * @param gctFile the input genomic file.
     * @param clsFile the input class file.
     * @return zip file containing the ODF marker results file and the gct/cls input files.
     * @throws ConnectionException if unable to connect to grid services.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    File runPreprocessComparativeMarkerSelection(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job, File gctFile, File clsFile)
            throws ConnectionException, InvalidCriterionException;
            
    /**
     * Executes the grid service PreprocessDataset.
     * @param updater the ajax updater.
     * @param job the Analysis job.
     * @param parameters for preprocess dataset.
     * @param gctFile original input gctFile.
     * @throws ConnectionException if unable to connect to grid service.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    void runPreprocessDataset(StatusUpdateListener updater,
            AbstractPersistedAnalysisJob job, PreprocessDatasetParameters parameters, File gctFile)
    throws ConnectionException, InvalidCriterionException;
    
    /**
     * Executes the grid service PCA.
     * @param updater the ajax updater.
     * @param job the Analysis job.
     * @param gctFile the input genomic file.
     * @return zip file containing results.
     * @throws ConnectionException if unable to connect to grid service.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    File runPCA(StatusUpdateListener updater,
            PrincipalComponentAnalysisJob job, File gctFile) 
        throws ConnectionException, InvalidCriterionException; 

    /**
     * Runs Gistic grid service.
     * @param updater the ajax updater.
     * @param job the Analysis job.
     * @param segmentFile segmentation input file.
     * @param markersFile markers input file.
     * @param cnvFile cnv input file.
     * @return GisticResults retrieved from grid.
     * @throws ConnectionException if unable to connect to grid service.
     * @throws InvalidCriterionException if criterion is not valid.
     * @throws ParameterException id parameter is invalid.
     * @throws IOException if there's a problem saving any of the files to the file system.
     */
    File runGistic(StatusUpdateListener updater, GisticAnalysisJob job, File segmentFile, File markersFile, 
            File cnvFile) 
        throws ConnectionException, InvalidCriterionException, ParameterException, IOException;
}
