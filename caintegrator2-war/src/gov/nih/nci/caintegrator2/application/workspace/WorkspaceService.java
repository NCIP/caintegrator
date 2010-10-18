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
package gov.nih.nci.caintegrator2.application.workspace;

import gov.nih.nci.caintegrator2.application.CaIntegrator2EntityRefresher;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;
import gov.nih.nci.security.exceptions.CSException;

import java.util.Set;

/**
 * Provides <code>UserWorkspace</code> access and management functionality.
 */
public interface WorkspaceService extends CaIntegrator2EntityRefresher {
    
    /**
     * Returns the workspace belonging to the current user.
     * 
     * @return the current user's workspace.
     */
    UserWorkspace getWorkspace();
    
    /**
     * Returns the users workspace (read only, for anonymous user which won't be saved).
     * @return the anonymous user's workspace.
     */
    UserWorkspace getWorkspaceReadOnly();
    
    /**
     * Refreshes studies for hibernate.
     * @param workspace to refresh studies for.
     */
    void refreshWorkspaceStudies(UserWorkspace workspace);
    
    /**
     * Retrieves the studyConfigurationJobs for the user workspace.
     * 
     * @param userWorkspace workspace of the user.
     * @return all study configuraiton jobs for this users workspace.
     * @throws CSException if there's a problem accessing CSM.
    */
    Set<StudyConfiguration> retrieveStudyConfigurationJobs(UserWorkspace userWorkspace) 
        throws CSException;
   
   /**
    * Subscribes a user to a study.
     * 
     * @param workspace workspace of the user.
     * @param study - study to subscribe to.
     * @param isPublicSubscription - determines if it's a public subscription or not.
     */
    void subscribe(UserWorkspace workspace, Study study, boolean isPublicSubscription);
        
    /**
     * Subscribe to all studies that the user has read access.
     * @param userWorkspace - object to use.
     */
    void subscribeAll(UserWorkspace userWorkspace);

    /**
     * Subscribe to all studies for anonymous user.
     * @param userWorkspace - object to use.
     */
    void subscribeAllReadOnly(UserWorkspace userWorkspace);
    
    /**
     * Unsubscribes a user to a study.
     * 
     * @param workspace workspace of the user.
     * @param study - study to subscribe to.
     */
    void unsubscribe(UserWorkspace workspace, Study study);
    
    /**
     * Un-subscribes all users from the given study.
     * @param study to un-subscribe from.
     */
    void unsubscribeAll(Study study);

    /**
     * Saves the current changes.
     * @param workspace - object that needs to be updated.
     */
    void saveUserWorkspace(UserWorkspace workspace);
    
    /**
     * Get the Analysis Job.
     * @param id - id to be retrieved.
     * @return Analysis Job
     */
    AbstractPersistedAnalysisJob getPersistedAnalysisJob(Long id);
    
    /**
     * Saves the current changes.
     * @param job - object to be updated.
     */
    void savePersistedAnalysisJob(AbstractPersistedAnalysisJob job);
    
    /**
     * Retrieve all platforms from a study.
     * @param study - object to use.
     * @return a set of platform from the study.
     */
    Set<Platform> retrievePlatformsInStudy(Study study);
    
    /**
     * Creates a <code> DisplayableStudySummary </code> from the given Study. 
     * @param study - object to use.
     * @return - DisplayableStudySummary object created from the study.
     */
    DisplayableStudySummary createDisplayableStudySummary(Study study);
    
    /**
     * @param geneList the gene list to create
     * @param geneSymbols the list of gene symbols
     */
    void createGeneList(GeneList geneList, Set<String> geneSymbols);
    
    /**
     * @param subjectList the subject list to create
     * @param subjects the set of subject identifier
     * @throws ValidationException if identifier is too long.
     */
    void createSubjectList(SubjectList subjectList, Set<String>subjects) throws ValidationException;

    /**
     * @param abstractList the list to make global
     */
    void makeListGlobal(AbstractList abstractList);

    /**
     * @param abstractList the list to make private
     */
    void makeListPrivate(AbstractList abstractList);

    /**
     * @param subscription the study subscription that request this action.
     * @param abstractList to delete from the workspace.
     */
    void deleteAbstractList(StudySubscription subscription, AbstractList abstractList);
    
    /**
     * Clears the current session.
     */
    void clearSession();

}
