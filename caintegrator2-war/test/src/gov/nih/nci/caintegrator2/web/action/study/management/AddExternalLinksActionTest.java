/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;

public class AddExternalLinksActionTest extends AbstractSessionBasedTest {

    private AddExternalLinksAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new AddExternalLinksAction();
        action.setWorkspaceService(workspaceService);
        action.setStudyManagementService(studyManagementServiceStub);
        studyManagementServiceStub.clear();
    }

    @Test
    public void testValidate() {
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();

        action.setExternalLinksFile(TestDataFiles.SIMPLE_EXTERNAL_LINKS_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();

        action.getExternalLinkList().setName("name");
        action.validate();
        assertFalse(action.hasFieldErrors());
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
