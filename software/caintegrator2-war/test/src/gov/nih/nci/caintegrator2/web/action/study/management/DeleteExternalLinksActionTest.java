/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class DeleteExternalLinksActionTest extends AbstractSessionBasedTest {
    
    private DeleteExternalLinksAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    
    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        action = (DeleteExternalLinksAction) context.getBean("deleteExternalLinksAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testPrepare() {
        action.prepare();
        assertFalse(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        action.getExternalLinkList().setId(1l);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        
    }

    @Test
    public void testExecute() {
        assertEquals(Action.ERROR, action.execute());
        action.getExternalLinkList().setName("validName");
        assertEquals(Action.SUCCESS, action.execute());
        
    }

}
