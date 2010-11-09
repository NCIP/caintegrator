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

package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.application.CaIntegrator2EntityRefresher;
import gov.nih.nci.caintegrator2.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator2.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator2.web.action.query.DisplayableResultRow;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Interface to the service which manages query data for a user's workspace.
 */
public interface QueryManagementService extends CaIntegrator2EntityRefresher {

    /**
     * Executes a clinical query and returns back the result.
     * @param query item to execute.
     * @return - Result of the query being executed
     * @throws InvalidCriterionException if criterion is invalid.
     */
    QueryResult execute(Query query) throws InvalidCriterionException;
    
    /**
     * Return annotation for all samples.
     * @param query item to execute.
     * @return - Result of the query being executed
     * @throws InvalidCriterionException if criterion is invalid.
     */
    QueryResult getAnnotationForAllSamples(Query query) throws InvalidCriterionException;

    /**
     * Retrieves the query that will be executed (so we can get things such as platforms used in a query).
     * @param query to transform for execution.
     * @return the query that is transformed to be executable.
     * @throws InvalidCriterionException if criterion is invalid.
     */
    Query retrieveQueryToExecute(Query query) throws InvalidCriterionException;
    
    /**
     * Executes a query that returns a genomic data set.
     * 
     * @param query the query to execute.
     * @return the resulting data.
     * @throws InvalidCriterionException if criterion is invalid. 
     */
    GenomicDataQueryResult executeGenomicDataQuery(Query query) throws InvalidCriterionException;
    
    /**
     * Executes a query that returns a list of segment data.
     * 
     * @param query the query to execute.
     * @return the resulting data.
     * @throws InvalidCriterionException if criterion is invalid. 
     */
    Collection<SegmentData> retrieveSegmentDataQuery(Query query) throws InvalidCriterionException;
    
    /**
     * Retrieves the geneExpression platforms that exist in a study.
     * @param study to find platforms for.
     * @return gene expression platform names.
     */
    Set<String> retrieveGeneExpressionPlatformsForStudy(Study study);
    
    /**
     * Retrieves the copy number platforms that exist in a study.
     * @param study to find platforms for.
     * @return copy number platform names.
     */
    Set<String> retrieveCopyNumberPlatformsForStudy(Study study);
    
    /**
     * Creates a list of queries given a group of subject lists.
     * @param subscription to study subscription.
     * @return list of queries.
     */
    List<Query> createQueriesFromSubjectLists(StudySubscription subscription);
    
    /**
     * Creates a query from a subject list.
     * @param subscription to study subscription.
     * @param subjectList to create query from.
     * @return query created from SubjectList.
     */
    Query createQueryFromSubjectList(StudySubscription subscription, SubjectList subjectList);
    
    /**
     * Creates a Dicom Job object based on the checked rows.
     * @param checkedRows - rows the user selected.
     * @return Dicom Job.
     */
    NCIADicomJob createDicomJob(List<DisplayableResultRow> checkedRows);
    
    /**
     * Creates an NCIA Basket object based on the checked rows.
     * @param checkedRows - rows the user selected.
     * @return NCIA Basket.
     */
    NCIABasket createNciaBasket(List<DisplayableResultRow> checkedRows);

    /**
     * Persists a query.
     * 
     * @param query item to update
     */
    void save(Query query);
    
    /**
     * Deletes a query.
     * 
     * @param query item to update
     */
    void delete(Query query);
    
    /**
     * Creates a CSV file from the genomic results.
     * @param result to create csv file for.
     * @return csv file.
     */
    File createCsvFileFromGenomicResults(GenomicDataQueryResult result);
    
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
     * Retrieves all subject identifiers not found in the criteria of the given query.
     * @param query to scan criteria for subject identifiers that aren't found in the study.
     * @return set of all identifiers not found in study but found in criteria.
     * @throws InvalidCriterionException if there is a criterion that specifies subjects and
     * none of the subjects specified exist in the study.
     */
    Set<String> getAllSubjectsNotFoundInCriteria(Query query) throws InvalidCriterionException;

}
