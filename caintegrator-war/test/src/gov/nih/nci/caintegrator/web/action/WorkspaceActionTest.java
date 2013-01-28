/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.web.action.WorkspaceAction;

import org.junit.Before;
import org.junit.Test;

public class WorkspaceActionTest extends AbstractSessionBasedTest {
    private WorkspaceAction workspaceAction;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        workspaceAction = new WorkspaceAction();
        workspaceAction.setWorkspaceService(workspaceService);
    }

    @Test
    public void testOpenWorkspace() {
        workspaceAction.prepare();
        assertEquals("workspaceNoStudy", workspaceAction.openWorkspace());
        assertNotNull(workspaceAction.getWorkspace());
        assertNotNull(workspaceAction.getCurrentStudySubscriptionId());

        workspaceAction.getCurrentStudy().getStudyConfiguration().setStatus(Status.DEPLOYED);
        assertEquals("workspaceStudy", workspaceAction.openWorkspace());

        workspaceAction.setRegistrationSuccess(true);
        workspaceAction.openWorkspace();
        assertTrue(workspaceAction.hasActionMessages());
        assertFalse(workspaceAction.hasActionErrors());

        workspaceAction.setInvalidAccess(true);
        workspaceAction.openWorkspace();
        assertTrue(workspaceAction.hasActionErrors());

        workspaceAction.setInvalidAccess(false);
        workspaceAction.setSessionTimeout(true);
        workspaceAction.openWorkspace();
        assertTrue(workspaceAction.hasActionErrors());

    }
}
