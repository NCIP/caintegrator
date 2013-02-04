/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.data.StudyHelper;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class GenomicDataSourceAjaxUpdaterTest extends AbstractSessionBasedTest {

    private GenomicDataSourceAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;
    private WorkspaceServiceStudyDeploymentJobStub workspaceService;
    private StudyManagementServiceStub studyManagementServiceStub;
    private StudyConfiguration studyConfiguration;
    private GenomicDataSourceConfiguration genomicDataSource;

    @Before
    public void setUp() {
        super.setUp();
        updater = new GenomicDataSourceAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        workspaceService = new WorkspaceServiceStudyDeploymentJobStub();
        studyManagementServiceStub = new StudyManagementServiceStub();
        studyManagementServiceStub.clear();
        workspaceService.clear();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setStudyManagementService(studyManagementServiceStub);
        WebContextFactory.setWebContextBuilder(new WebContextBuilderStub());
        StudyHelper studyHelper = new StudyHelper();
        studyConfiguration = studyHelper.populateAndRetrieveStudyWithSourceConfigurations().getStudyConfiguration();
        studyConfiguration.setId(Long.valueOf(1));
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudyConfiguration(studyConfiguration);
        genomicDataSource = studyConfiguration.getGenomicDataSources().get(0);
        genomicDataSource.setId(Long.valueOf(1));
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
        studyManagementServiceStub.refreshedGenomicSource = genomicDataSource;
        updater.runJob(genomicDataSource.getId());
        Thread.sleep(500);
        assertTrue(studyManagementServiceStub.loadGenomicSourceCalled);
        assertTrue(studyManagementServiceStub.getRefreshedGenomicSourceCalled);
    }
    
    private final class WorkspaceServiceStudyDeploymentJobStub extends WorkspaceServiceStub {
        @Override
        public UserWorkspace getWorkspace() {
            UserWorkspace workspace = new UserWorkspace();
            workspace.setUsername("Test");
            workspace.getSubscriptionCollection().add(getSubscription());
            studyConfiguration.setUserWorkspace(workspace);
            
            return workspace;
        }
    }

}
