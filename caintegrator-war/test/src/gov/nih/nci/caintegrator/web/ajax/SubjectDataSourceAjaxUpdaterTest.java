/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.DwrUtilFactory;
import gov.nih.nci.caintegrator.web.ajax.SubjectDataSourceAjaxRunner;
import gov.nih.nci.caintegrator.web.ajax.SubjectDataSourceAjaxUpdater;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class SubjectDataSourceAjaxUpdaterTest extends AbstractSessionBasedTest {

    private SubjectDataSourceAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;
    private StudyManagementServiceStub studyManagementServiceStub;
    private StudyConfiguration studyConfiguration;
    private DelimitedTextClinicalSourceConfiguration clinicalDataSource;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new SubjectDataSourceAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        studyManagementServiceStub = new StudyManagementServiceStub();
        studyManagementServiceStub.clear();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setStudyManagementService(studyManagementServiceStub);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
        StudyHelper studyHelper = new StudyHelper();
        studyConfiguration = studyHelper.populateAndRetrieveStudyWithSourceConfigurations().getStudyConfiguration();
        studyConfiguration.setId(Long.valueOf(1));

        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudyConfiguration(studyConfiguration);
        clinicalDataSource = (DelimitedTextClinicalSourceConfiguration)
                                studyConfiguration.getClinicalConfigurationCollection().get(0);
        clinicalDataSource.setId(Long.valueOf(1));
        studyManagementServiceStub.refreshedStudyConfiguration = studyConfiguration;

        UserWorkspace workspace = new UserWorkspace();
        workspace.setUsername("Test");
        workspace.getSubscriptionCollection().add(getStudySubscription());
        studyConfiguration.setUserWorkspace(workspace);
        when(workspaceService.getWorkspace()).thenReturn(workspace);
    }

    @Test
    public void testInitializeJsp() throws InterruptedException, ServletException, IOException {
        updater.initializeJsp();
        assertNotNull(dwrUtilFactory.retrieveGenomicDataSourceUtil("Test"));
    }

    @Test
    public void testRunJob() throws InterruptedException {
        studyConfiguration.setUserWorkspace(workspaceService.getWorkspace());
        studyConfiguration.setLastModifiedBy(workspaceService.getWorkspace());
        studyManagementServiceStub.refreshedClinicalSource = clinicalDataSource;

        studyConfiguration.getStudy().setShortTitleText("Invalid");
        updater.runJob(studyConfiguration.getId(), clinicalDataSource.getId(),
                SubjectDataSourceAjaxRunner.JobType.LOAD);
        Thread.sleep(500);
        assertEquals(Status.ERROR, clinicalDataSource.getStatus());

        studyConfiguration.getStudy().setShortTitleText("valid");
        studyManagementServiceStub.clear();
        updater.runJob(studyConfiguration.getId(), clinicalDataSource.getId(),
                SubjectDataSourceAjaxRunner.JobType.LOAD);
        Thread.sleep(500);
        assertTrue(studyManagementServiceStub.loadClinicalAnnotationCalled);
        assertTrue(studyManagementServiceStub.getRefreshedStudyConfigurationCalled);
        assertEquals(Status.LOADED, clinicalDataSource.getStatus());

        studyManagementServiceStub.clear();
        updater.runJob(studyConfiguration.getId(), clinicalDataSource.getId(),
                SubjectDataSourceAjaxRunner.JobType.RELOAD);
        Thread.sleep(500);
        assertTrue(studyManagementServiceStub.reLoadClinicalAnnotationCalled);
        assertTrue(studyManagementServiceStub.getRefreshedStudyConfigurationCalled);
        assertEquals(Status.LOADED, clinicalDataSource.getStatus());

        studyManagementServiceStub.clear();
        updater.runJob(studyConfiguration.getId(), clinicalDataSource.getId(),
                SubjectDataSourceAjaxRunner.JobType.DELETE);
        Thread.sleep(500);
        assertTrue(studyManagementServiceStub.deleteCalled);
        assertTrue(studyManagementServiceStub.getRefreshedStudyConfigurationCalled);

    }
}
