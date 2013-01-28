/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.ajax.DwrUtilFactory;
import gov.nih.nci.caintegrator.web.ajax.PlatformDeploymentAjaxUpdater;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class PlatformDeploymentAjaxUpdaterTest extends AbstractSessionBasedTest {

    private PlatformDeploymentAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        updater = new PlatformDeploymentAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setArrayDataService(arrayDataService);
        WebContextFactory.setWebContextBuilder(webContextBuilder);

        UserWorkspace workspace = new UserWorkspace();
        workspace.setUsername("Test");
        workspace.getSubscriptionCollection().add(getStudySubscription());
        when(workspaceService.getWorkspace()).thenReturn(workspace);
    }

    @Test
    public void testInitializeJsp() throws InterruptedException, ServletException, IOException {
        updater.initializeJsp();
        assertNotNull(dwrUtilFactory.retrieveStudyConfigurationUtil("Test"));
    }

    @Test
    public void testRunJob() throws InterruptedException {
        PlatformConfiguration platformConfiguration = new PlatformConfiguration();
        platformConfiguration.setId(1l);
        updater.runJob(platformConfiguration, "username");
        Thread.sleep(500);
        verify(arrayDataService, atLeastOnce()).loadArrayDesign(any(PlatformConfiguration.class));
    }
}
