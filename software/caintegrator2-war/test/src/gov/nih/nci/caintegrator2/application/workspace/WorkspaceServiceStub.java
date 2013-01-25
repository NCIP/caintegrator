/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.workspace;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
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

import org.apache.commons.lang3.time.DateUtils;

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
    @Override
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

    @Override
    public void saveUserWorkspace(UserWorkspace workspace) {
        saveUserWorspaceCalled = true;
    }

    @Override
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

    @Override
    public void subscribeAll(UserWorkspace userWorkspace) {
        subscribeAllCalled = true;
    }

    @Override
    public void subscribe(UserWorkspace workspace, Study study, boolean isPublicSubscription) {
        subscribeCalled = true;
    }

    @Override
    public void unsubscribe(UserWorkspace workspace, Study study) {
        unSubscribeCalled = true;

    }

    @Override
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

    @Override
    public DisplayableStudySummary createDisplayableStudySummary(Study study) {
        createDisplayableStudySummaryCalled = true;
        return new DisplayableStudySummary(study);
    }


    @Override
    public void savePersistedAnalysisJob(AbstractPersistedAnalysisJob job) {
        savePersistedAnalysisJobCalled = true;
    }

    @Override
    public AbstractPersistedAnalysisJob getPersistedAnalysisJob(Long id) {
        return null;
    }

    public GeneList getGeneList(String name, StudySubscription subscription) {
        return null;
    }

    public GeneList getGeneList(Long id) {
        return null;
    }

    @Override
    public void createGeneList(GeneList geneList, Set<String> geneSymbols) {
        createGeneListCalled = true;
    }

    @Override
    public void createSubjectList(SubjectList subjectList, Set<String> subjects) {
        createSubjectListCalled = true;
    }

    @Override
    public void makeListGlobal(AbstractList abstractList) {
        makeListGlobalCalled = true;
    }

    @Override
    public void makeListPrivate(AbstractList abstractList) {
        makeListPrivateCalled = true;
    }

    @Override
    public <T extends AbstractCaIntegrator2Object> T getRefreshedEntity(T entity) {
        getRefreshedEntityCalled = true;
        return entity;
    }

    @Override
    public void deleteAbstractList(StudySubscription subscription, AbstractList abstractList) {
        deleteAbstractListCalled = true;
    }

    @Override
    public UserWorkspace getWorkspaceReadOnly() {
        getWorkspaceReadOnlyCalled = true;
        UserWorkspace workspace = new UserWorkspace();
        workspace.setDefaultSubscription(getSubscription());
        workspace.getSubscriptionCollection().add(workspace.getDefaultSubscription());
        workspace.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        return workspace;
    }

    @Override
    public void refreshWorkspaceStudies(UserWorkspace workspace) {
        refreshWorkspaceStudiesCalled = true;

    }

    @Override
    public void subscribeAllReadOnly(UserWorkspace userWorkspace) {
        subscribeAll(userWorkspace);
    }

    @Override
    public Set<Platform> retrievePlatformsInStudy(Study study) {
        return new HashSet<Platform>();
    }

    @Override
    public void clearSession() {
        clearSessionCalled = true;

    }

}
