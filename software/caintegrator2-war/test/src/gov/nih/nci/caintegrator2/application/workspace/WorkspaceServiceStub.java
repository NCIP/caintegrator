/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.workspace;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
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
    public boolean getRefreshedEntityCalled;
    
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
        getRefreshedEntityCalled = false;
    }
    public UserWorkspace getWorkspace() {
        UserWorkspace workspace = new UserWorkspace();
        workspace.setDefaultSubscription(getSubscription());
        workspace.setSubscriptionCollection(new HashSet<StudySubscription>());
        workspace.getSubscriptionCollection().add(workspace.getDefaultSubscription());
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

    public void subscribe(UserWorkspace workspace, Study study) {
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
            subscription.setQueryCollection(new HashSet<Query>());
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

    public void createGeneList(GeneList geneList, List<String> geneSymbols) {
        createGeneListCalled = true;
    }

    public <T> T getRefreshedEntity(T entity) {
        getRefreshedEntityCalled = true;
        return entity;
    }

}
