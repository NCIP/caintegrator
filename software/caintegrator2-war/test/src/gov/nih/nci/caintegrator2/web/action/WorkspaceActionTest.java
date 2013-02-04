/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.ActionContext;

public class WorkspaceActionTest {

    private WorkspaceAction workspaceAction;
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("action-test-config.xml", WorkspaceActionTest.class); 
        workspaceAction = (WorkspaceAction) context.getBean("workspaceAction"); 
        ActionContext.getContext().setSession(new HashMap());
    }

    @Test
    public void testOpenWorkspace() {
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        workspaceAction.prepare();
        assertEquals("workspaceStudy", workspaceAction.openWorkspace());
        assertNotNull(workspaceAction.getWorkspace());
        assertNotNull(workspaceAction.getCurrentStudySubscriptionId());
    }

}
