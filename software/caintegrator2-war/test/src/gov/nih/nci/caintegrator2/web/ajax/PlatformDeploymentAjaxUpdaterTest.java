/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContextFactory;
import org.junit.Before;
import org.junit.Test;


public class PlatformDeploymentAjaxUpdaterTest extends AbstractSessionBasedTest {

    private PlatformDeploymentAjaxUpdater updater;
    private DwrUtilFactory dwrUtilFactory;
    private WorkspaceServicePlatformDeploymentJobStub workspaceService;
    private ArrayDataServiceStub arrayDataService;

    @Before
    public void setUp() {
        super.setUp();
        updater = new PlatformDeploymentAjaxUpdater();
        dwrUtilFactory = new DwrUtilFactory();
        workspaceService = new WorkspaceServicePlatformDeploymentJobStub();
        arrayDataService = new ArrayDataServiceStub();
        arrayDataService.clear();
        workspaceService.clear();
        updater.setWorkspaceService(workspaceService);
        updater.setDwrUtilFactory(dwrUtilFactory);
        updater.setArrayDataService(arrayDataService);
        WebContextFactory.setWebContextBuilder(new WebContextBuilderStub());
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
        assertTrue(arrayDataService.loadArrayDesignCalled);
    }
    
    private final class WorkspaceServicePlatformDeploymentJobStub extends WorkspaceServiceStub {
        @Override
        public UserWorkspace getWorkspace() {
            UserWorkspace workspace = new UserWorkspace();
            workspace.setUsername("Test");
            workspace.getSubscriptionCollection().add(getSubscription());
            
            return workspace;
        }
    }

}
