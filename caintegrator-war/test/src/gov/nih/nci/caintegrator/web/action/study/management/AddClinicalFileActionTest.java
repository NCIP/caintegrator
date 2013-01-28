/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.AddClinicalFileAction;

import javax.swing.filechooser.FileSystemView;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class AddClinicalFileActionTest extends AbstractSessionBasedTest {

    private AddClinicalFileAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new AddClinicalFileAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
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
