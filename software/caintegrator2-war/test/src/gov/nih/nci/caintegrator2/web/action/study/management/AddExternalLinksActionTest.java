/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class AddExternalLinksActionTest extends AbstractSessionBasedTest {
    
    private AddExternalLinksAction action;
    private StudyManagementServiceStub studyManagementServiceStub;
    
    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        action = (AddExternalLinksAction) context.getBean("addExternalLinksAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testValidate() {
        action.validate();
        assertTrue(action.hasActionErrors());
        action.clearErrorsAndMessages();
        
        action.setExternalLinksFile(TestDataFiles.SIMPLE_EXTERNAL_LINKS_FILE);
        action.validate();
        assertTrue(action.hasActionErrors());
        action.clearErrorsAndMessages();
        
        action.getExternalLinkList().setName("name");
        action.validate();
        assertFalse(action.hasActionErrors());
    }

    @Test
    public void testExecute() {
        action.setExternalLinksFile(TestDataFiles.SIMPLE_EXTERNAL_LINKS_FILE);
        action.setExternalLinksFileFileName("fileName");
        assertEquals(Action.SUCCESS, action.execute());
        assertEquals(action.getExternalLinkList().getFile(), TestDataFiles.SIMPLE_EXTERNAL_LINKS_FILE);
        assertEquals(action.getExternalLinkList().getFileName(), "fileName");
        
        studyManagementServiceStub.throwIOException = true;
        assertEquals(Action.INPUT, action.execute());
        studyManagementServiceStub.clear();
        
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.INPUT, action.execute());
    }

}
