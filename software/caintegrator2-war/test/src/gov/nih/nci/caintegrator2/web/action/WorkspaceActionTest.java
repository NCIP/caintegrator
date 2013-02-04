/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.Status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class WorkspaceActionTest extends AbstractSessionBasedTest {

    private WorkspaceAction workspaceAction;
    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("action-test-config.xml", WorkspaceActionTest.class); 
        workspaceAction = (WorkspaceAction) context.getBean("workspaceAction"); 
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
