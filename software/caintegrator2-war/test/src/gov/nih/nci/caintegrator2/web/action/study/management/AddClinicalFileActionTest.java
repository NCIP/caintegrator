/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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

import javax.swing.filechooser.FileSystemView;

public class AddClinicalFileActionTest extends AbstractSessionBasedTest {

    private AddClinicalFileAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditStudyActionTest.class); 
        action = (AddClinicalFileAction) context.getBean("addClinicalFileAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testPrepare() {
        action.prepare();
        assertFalse(action.hasActionErrors());
        assertTrue(action.isFileUpload());
        assertFalse(action.hasActionErrors());
    }
    
    @Test
    public void testValidate() {
        
        String fileType;
        
        action.validate();
        assertTrue(action.hasActionErrors());
        
        action.setClinicalFile(TestDataFiles.INVALID_FILE_DOESNT_EXIST);
        action.validate();
        assertTrue(action.hasActionErrors());
        
        action.setClinicalFile(TestDataFiles.VALID_FILE);
        assertNull(action.getClinicalFileContentType());
        action.validate();
        assertTrue(action.hasActionErrors());        
        
        action.setClinicalFile(TestDataFiles.VALID_FILE);
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();   
        fileType = fileSystemView.getSystemTypeDescription(TestDataFiles.VALID_FILE);     
        action.setClinicalFileContentType(fileType);
        //assertEquals("Microsoft Office Excel Comma Separated Values File",action.getClinicalFileContentType());
        action.getClinicalFileContentType();
        action.clearErrorsAndMessages();
        action.validate();
        assertFalse(action.hasActionErrors());         
        
        action.setClinicalFile(TestDataFiles.VALID_FILE);
        action.clearErrorsAndMessages();
        action.validate();
        assertFalse(action.hasActionErrors());
    }


    @Test
    public void testExecute() {
        action.setClinicalFile(TestDataFiles.VALID_FILE);
        action.setClinicalFileFileName(TestDataFiles.VALID_FILE.getName());
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(studyManagementServiceStub.addClinicalAnnotationFileCalled);
        action.setClinicalFile(TestDataFiles.INVALID_FILE_MISSING_VALUE);
        assertEquals(Action.INPUT, action.execute());
        action.setClinicalFile(TestDataFiles.INVALID_FILE_DOESNT_EXIST);
        assertEquals(Action.INPUT, action.execute());
    }

}
