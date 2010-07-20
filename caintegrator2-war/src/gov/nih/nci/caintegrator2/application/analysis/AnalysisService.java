/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.application.CaIntegrator2EntityRefresher;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
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
     * @throws DataRetrievalException exception parsing the result files.
     */
    File executeGridGistic(StatusUpdateListener updater, GisticAnalysisJob job)
            throws ConnectionException, InvalidCriterionException, ParameterException, IOException,
                DataRetrievalException;
    
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
    
    /**
     * Deletes the gistic analysis.
     * @param gisticAnalysis to delete.
     */
    void deleteGisticAnalysis(GisticAnalysis gisticAnalysis);
}
