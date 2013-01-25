/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.CaIntegrator2EntityRefresher;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ParameterException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.genepattern.webservice.WebServiceException;

/**
 * Interface to analysis functionality.
 */
public interface AnalysisService extends CaIntegrator2EntityRefresher {
    
    /**
     * Returns a list of GenePattern analysis tasks that may be run.
     * 
     * @param server the gene pattern server.
     * @return the list of available tasks
     * @throws WebServiceException if the service couldn't be reached.
     */
    List<AnalysisMethod> getGenePatternMethods(ServerConnectionProfile server) throws WebServiceException;

    /**
     * Executes a job on a GenePattern server.
     * 
     * @param server the GenePattern server
     * @param invocation contains the configuration of the job to execute
     * @return the Wrapper for the JobInfo retrieved from GenePattern.
     * @throws WebServiceException if the service couldn't be reached.
     */
    JobInfoWrapper executeGenePatternJob(ServerConnectionProfile server, AnalysisMethodInvocation invocation) 
    throws WebServiceException;
    
    /**
     * Executes preprocessDataset followed by Comparative Marker Selection via grid interface.
     * @param updater the ajax updater.
     * @param job the Analysis job.
     * @return zip file containing the marker results (.odf format) and gct/cls input files.
     * @throws ConnectionException if unable to connect to grid.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    File executeGridPreprocessComparativeMarker(StatusUpdateListener updater,
            ComparativeMarkerSelectionAnalysisJob job) 
            throws ConnectionException, InvalidCriterionException;
    
    /**
     * Executes Principal Component Analysis grid service and returns back the results files.
     * @param updater the ajax updater.
     * @param job the Analysis job.
     * @return results file.
     * @throws ConnectionException if unable to connect to grid.
     * @throws InvalidCriterionException if criterion is not valid.
     */
    File executeGridPCA(StatusUpdateListener updater,
            PrincipalComponentAnalysisJob job) throws ConnectionException, InvalidCriterionException;
    
    /**
     * Executes preprocessDataset followed by Comparative Marker Selection via grid interface.
     * @param updater the ajax updater.
     * @param job the Analysis job.
     * @return GisticResult Result objects.
     * @throws ConnectionException if unable to connect to grid.
     * @throws InvalidCriterionException if criterion is not valid.
     * @throws ParameterException if parameter is invalid.
     * @throws IOException if there's a problem saving files.
     */
    File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job)
            throws ConnectionException, InvalidCriterionException, ParameterException, IOException;
    
    /**
     * Creates a KMPlot object based on clinical subjects for the given parameters.
     * @param subscription the study subscription that the user wants to create the plot for.
     * @param kmParameters are the input parameters for the KMPlot.
     * @return the plot object.
     * @throws InvalidCriterionException if the Criterion is no longer valid for queries.
     * @throws GenesNotFoundInStudyException if the criterion is supposed to have gene input and none found in study. 
     * @throws InvalidSurvivalValueDefinitionException if the survival value definition is invalid.
     */
    KMPlot createKMPlot(StudySubscription subscription, AbstractKMParameters kmParameters) 
    throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException;

    /**
     * Creates the GeneExpressionPlotGroup which is a group of plots based on the input parameters, the plot
     * types are Mean, median, log2 intensity, and box-whisker log2 intensity.
     * @param studySubscription the study subscription that the user wants to create the plot for.
     * @param plotParameters input parameters for the plots.
     * @return the plot group object.
     * @throws ControlSamplesNotMappedException when a control sample is not mapped.
     * @throws InvalidCriterionException if criterion is not valid.
     * @throws GenesNotFoundInStudyException if none of the given genes are found in study.
     */
    GeneExpressionPlotGroup createGeneExpressionPlot(StudySubscription studySubscription, 
            AbstractGEPlotParameters plotParameters) throws ControlSamplesNotMappedException, 
            InvalidCriterionException, GenesNotFoundInStudyException;

    /**
     * Delete the analysis job with given id.
     * 
     * @param jobId the id of the analysis job to delete.
     */
    void deleteAnalysisJob(Long jobId);
    
    /**
     * Validates the gene symbols and returns all symbols that exist in the study.
     * @param studySubscription to check gene symbols against.
     * @param geneSymbols to validate existance in the study.
     * @return all valid gene symbols.
     * @throws GenesNotFoundInStudyException if no genes are found.
     */
    List<String> validateGeneSymbols(StudySubscription studySubscription, List<String> geneSymbols)
        throws GenesNotFoundInStudyException;
    
    /**
     * Refreshes studySubscription object from the database so it can be used.
     * @param studySubscription to be refreshed from the database.
     * @return refreshed studySubscription.
     */
    StudySubscription getRefreshedStudySubscription(StudySubscription studySubscription);
    
}
