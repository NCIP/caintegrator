/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.SaveStudyAction;

import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class SaveStudyActionTest extends AbstractSessionBasedTest {

    private SaveStudyAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new SaveStudyAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
        SessionHelper.getInstance().getDisplayableUserWorkspace().refresh(workspaceService, true);
    }

    @Test
    public void testExecute() {
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        SecurityContextHolder.getContext().setAuthentication(null);
        assertEquals(Action.ERROR, action.execute());
        // Must add authentication to pass the action.
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        SessionHelper.getInstance().setStudyManager(true);
        action.prepare();
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(studyManagementServiceStub.saveCalled);
    }

    @Test
    public void testValidate() {
        StringBuffer longName = new StringBuffer();
        for (int i =0; i<210; i++) {
            longName.append('A');
        }
        action.getStudyConfiguration().getStudy().setShortTitleText("name");
        action.getStudyConfiguration().getStudy().setLongTitleText("Description");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertTrue(studyManagementServiceStub.isDuplicateStudyNameCalled);
        studyManagementServiceStub.clear();
        action.getStudyConfiguration().getStudy().setShortTitleText(null);
        action.getStudyConfiguration().getStudy().setLongTitleText("Description");
        action.validate();
        assertFalse(studyManagementServiceStub.isDuplicateStudyNameCalled);
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        action.getStudyConfiguration().getStudy().setShortTitleText("");
        action.validate();
        assertFalse(studyManagementServiceStub.isDuplicateStudyNameCalled);
        assertTrue(action.hasFieldErrors());
        action.getStudyConfiguration().getStudy().setShortTitleText("Duplicate");
        action.getStudyConfiguration().getStudy().setLongTitleText("Description");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.getStudyConfiguration().getStudy().setShortTitleText(longName.toString());
        action.getStudyConfiguration().getStudy().setLongTitleText("Description");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.getStudyConfiguration().getStudy().setShortTitleText("name");
        action.getStudyConfiguration().getStudy().setLongTitleText(longName.toString());
        action.validate();
        assertTrue(action.hasFieldErrors());

    }

    @Test
    public void testMaliciousCharacterRemoval() {
        String maliciousNameIn = "StudyName<iframe src=javascript:alert(70946)";
        String goodNameIn = "StudyName";
        String nameOut = "StudyName";

        ActionContext.getContext().setSession(new HashMap<String, Object>());
        SecurityContextHolder.getContext().setAuthentication(null);
        assertEquals(Action.ERROR, action.execute());
        // Must add authentication to pass the action.
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        SessionHelper.getInstance().setStudyManager(true);
        action.getStudyConfiguration().getStudy().setShortTitleText(maliciousNameIn);
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(studyManagementServiceStub.saveCalled);
        assertEquals(nameOut, action.getStudyConfiguration().getStudy().getShortTitleText());

        action.getStudyConfiguration().getStudy().setShortTitleText(goodNameIn);
        assertEquals(Action.SUCCESS, action.execute());
        assertTrue(studyManagementServiceStub.saveCalled);
        assertEquals(nameOut, action.getStudyConfiguration().getStudy().getShortTitleText());

    }
}
