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

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;

/**
 * Stubbed implementation of WorkspaceService for testing.
 */
public class WorkspaceServiceStub implements WorkspaceService {

    private StudySubscription subscription;
    public boolean subscribeCalled;
    public boolean subscribeAllCalled;
    public boolean retrieveStudyConfigurationJobsCalled;
    public boolean unSubscribeCalled;
    public boolean unSubscribeAllCalled;
    public boolean saveUserWorspaceCalled;
    public boolean refreshAnnotationDefinitionsCalled;
    public boolean createDisplayableStudySummaryCalled;
    public boolean savePersistedAnalysisJobCalled;
    public boolean createGeneListCalled;
    public boolean createSubjectListCalled;
    public boolean makeListGlobalCalled;
    public boolean makeListPrivateCalled;
    public boolean getRefreshedEntityCalled;
    public boolean deleteAbstractListCalled;
    public boolean refreshWorkspaceStudiesCalled;
    public boolean getWorkspaceReadOnlyCalled;
    public boolean clearSessionCalled;
    
    public void clear() {
        subscribeCalled = false;
        retrieveStudyConfigurationJobsCalled = false;
        subscribeAllCalled = false;
        unSubscribeCalled = false;
        saveUserWorspaceCalled = false;
        refreshAnnotationDefinitionsCalled = false;
        createDisplayableStudySummaryCalled = false;
        savePersistedAnalysisJobCalled = false;
        unSubscribeAllCalled = false;
        createGeneListCalled = false;
        createSubjectListCalled = false;
        makeListGlobalCalled = false;
        makeListPrivateCalled = false;
        getRefreshedEntityCalled = false;
        deleteAbstractListCalled = false;
        refreshWorkspaceStudiesCalled = false;
        getWorkspaceReadOnlyCalled = false;
        clearSessionCalled = false;
    }
    public UserWorkspace getWorkspace() {
        UserWorkspace workspace = new UserWorkspace();
        workspace.setDefaultSubscription(getSubscription());
        workspace.getSubscriptionCollection().add(workspace.getDefaultSubscription());
        if (subscription != null) {
            workspace.getSubscriptionCollection().add(subscription);
        }
        workspace.setUsername("username");
        return workspace;
    }

    public StudySubscription retrieveStudySubscription(Long id) {
        return subscription;
    }

    public void saveUserWorkspace(UserWorkspace workspace) {
        saveUserWorspaceCalled = true; 
    }

    public Set<StudyConfiguration> retrieveStudyConfigurationJobs(UserWorkspace workspace) {
        retrieveStudyConfigurationJobsCalled = true;
        HashSet<StudyConfiguration> results = new HashSet<StudyConfiguration>();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        results.add(studyConfiguration);
        studyConfiguration.setStatus(Status.PROCESSING);
        Date today = new Date();
        studyConfiguration.setDeploymentStartDate(DateUtils.addHours(today, -13));
        studyConfiguration = new StudyConfiguration();
        results.add(studyConfiguration);
        studyConfiguration.setStatus(Status.PROCESSING);
        studyConfiguration.setDeploymentStartDate(today);
        return results;
    }
    
    public void subscribeAll(UserWorkspace userWorkspace) {
        subscribeAllCalled = true;
    }

    public void subscribe(UserWorkspace workspace, Study study, boolean isPublicSubscription) {
        subscribeCalled = true;
    }

    public void unsubscribe(UserWorkspace workspace, Study study) {
        unSubscribeCalled = true;
        
    }
    
    public void unsubscribeAll(Study study) {
        unSubscribeAllCalled = true;
    }
    
    public void setSubscription(StudySubscription subscription) {
        this.subscription = subscription;
    }
    
    public StudySubscription getSubscription() {
        if (subscription == null) {
            subscription = new StudySubscription();
            subscription.setId(Long.valueOf(1));
            subscription.setStudy(new Study());
            subscription.getStudy().setShortTitleText("Study Name");
            subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        }
        return subscription;
    }
    
    public List<StudySubscription> getStudySubscriptions() {
        List<StudySubscription> studySubscriptions = new ArrayList<StudySubscription>();
        studySubscriptions.add(getSubscription());
        return studySubscriptions;
    }


    public void refreshAnnotationDefinitions() {
        refreshAnnotationDefinitionsCalled = true;
    }
    
    public DisplayableStudySummary createDisplayableStudySummary(Study study) {
        createDisplayableStudySummaryCalled = true;
        return new DisplayableStudySummary(study);
    }


    public void savePersistedAnalysisJob(AbstractPersistedAnalysisJob job) {
        savePersistedAnalysisJobCalled = true;
    }

    public AbstractPersistedAnalysisJob getPersistedAnalysisJob(Long id) {
        return null;
    }

    public GeneList getGeneList(String name, StudySubscription subscription) {
        return null;
    }

    public GeneList getGeneList(Long id) {
        return null;
    }

    public void createGeneList(GeneList geneList, Set<String> geneSymbols) {
        createGeneListCalled = true;
    }

    public void createSubjectList(SubjectList subjectList, Set<String> subjects) {
        createSubjectListCalled = true;
    }

    public void makeListGlobal(AbstractList abstractList) {
        makeListGlobalCalled = true;
    }

    public void makeListPrivate(AbstractList abstractList) {
        makeListPrivateCalled = true;
    }

    public <T> T getRefreshedEntity(T entity) {
        getRefreshedEntityCalled = true;
        return entity;
    }

    public void deleteAbstractList(StudySubscription subscription, AbstractList abstractList) {
        deleteAbstractListCalled = true;
    }

    public UserWorkspace getWorkspaceReadOnly() {
        getWorkspaceReadOnlyCalled = true;
        UserWorkspace workspace = new UserWorkspace();
        workspace.setDefaultSubscription(getSubscription());
        workspace.getSubscriptionCollection().add(workspace.getDefaultSubscription());
        workspace.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        return workspace;
    }

    public void refreshWorkspaceStudies(UserWorkspace workspace) {
        refreshWorkspaceStudiesCalled = true;
        
    }
    
    public void subscribeAllReadOnly(UserWorkspace userWorkspace) {
        subscribeAll(userWorkspace);
    }

    public Set<Platform> retrievePlatformsInStudy(Study study) {
        return new HashSet<Platform>();
    }

    public void clearSession() {
        clearSessionCalled = true;
        
    }

}
