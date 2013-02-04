/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.data.StudyHelper;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.DwrUtilFactory;
import gov.nih.nci.caintegrator.web.ajax.StudyDeploymentAjaxUpdater;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class StudyDeploymentAjaxUpdaterTest extends AbstractSessionBasedTest {

    private StudyDeploymentAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;
    private StudyConfiguration studyConfiguration;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new StudyDeploymentAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setDeploymentService(deploymentService);
        WebContextFactory.setWebContextBuilder(webContextBuilder);
        StudyHelper studyHelper = new StudyHelper();
        studyConfiguration = studyHelper.populateAndRetrieveStudyWithSourceConfigurations().getStudyConfiguration();
        studyConfiguration.setId(Long.valueOf(1));

        UserWorkspace workspace = new UserWorkspace();
        workspace.setUsername("Test");
        workspace.getSubscriptionCollection().add(getStudySubscription());
        studyConfiguration.setUserWorkspace(workspace);
        when(workspaceService.getWorkspace()).thenReturn(workspace);
    }

    @Test
    public void testInitializeJsp() throws InterruptedException, ServletException, IOException {
        updater.initializeJsp();
        assertNotNull(dwrUtilFactory.retrieveStudyConfigurationUtil("Test"));
    }

    @Test
    public void testRunJob() throws InterruptedException {
        studyConfiguration.setUserWorkspace(workspaceService.getWorkspace());
        studyConfiguration.setLastModifiedBy(workspaceService.getWorkspace());
        updater.runJob(studyConfiguration, new HeatmapParameters());
        Thread.sleep(500);
        verify(deploymentService, times(1)).performDeployment(any(StudyConfiguration.class), any(HeatmapParameters.class));
    }
}
