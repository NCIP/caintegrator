/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class ViewExternalLinksActionTest extends AbstractSessionBasedTest {
    
    private ViewExternalLinksAction action;
    private WorkspaceServiceStub workspaceServiceStub;
    
    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        action = (ViewExternalLinksAction) context.getBean("viewExternalLinksAction");
        workspaceServiceStub = (WorkspaceServiceStub) context.getBean("workspaceService");
        workspaceServiceStub.clear();
    }

    @Test
    public void testPrepare() {
        action.prepare();
        assertFalse(workspaceServiceStub.getRefreshedEntityCalled);
        action.getExternalLinkList().setId(1l);
        action.prepare();
        assertTrue(workspaceServiceStub.getRefreshedEntityCalled);
        
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
        
    }

}
