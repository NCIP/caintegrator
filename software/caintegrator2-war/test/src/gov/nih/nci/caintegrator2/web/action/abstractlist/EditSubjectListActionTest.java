/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.abstractlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectIdentifier;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

public class EditSubjectListActionTest extends AbstractSessionBasedTest {

    EditSubjectListAction action = new MyEditSubjectListAction();
    StudySubscription subscription = new StudySubscription();
    WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();

    class MyEditSubjectListAction extends EditSubjectListAction {
        
        private static final long serialVersionUID = 1L;

        /**
         * @return is the current user a study manager
         */
        public boolean isStudyManager() {
            return true;
        }
    }

    @Before
    public void setUp() {
        super.setUp();
        subscription.setId(1L);
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(subscription);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        workspaceService.setSubscription(subscription);
        action.setWorkspaceService(workspaceService);
        
        SubjectList list = new SubjectList();
        list.setName("List1");
        SubjectIdentifier subjectIdentifier = new SubjectIdentifier();
        subjectIdentifier.setIdentifier("ABC123");
        list.getSubjectIdentifiers().add(subjectIdentifier);
        subjectIdentifier = new SubjectIdentifier();
        subjectIdentifier.setIdentifier("DEF123");
        list.getSubjectIdentifiers().add(subjectIdentifier);
        subscription.getListCollection().add(list);
        list = new SubjectList();
        list.setName("List2");
        subscription.getListCollection().add(list);
        
        StudyConfiguration studyConfiguration = subscription.getStudy().getStudyConfiguration();
        studyConfiguration.setListCollection(subscription.getListCollection());
        action.prepare();
    }
    
    @Test
    public void testAll() {
        action.setVisibleToOther(false);
        action.setSelectedAction(EditSubjectListAction.EDIT_ACTION);
        action.validate();
        assertFalse(action.hasFieldErrors());

        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());
        
        assertEquals(13, action.getSubjectIdentifierListing().length());

        assertEquals(EditSubjectListAction.SUCCESS, action.execute());
        
        action.setSelectedAction(EditSubjectListAction.SAVE_ACTION);
        action.setListOldName("Oldname");
        action.setListName("");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());
        
        action.setListName("List1");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.setListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());
        
        action.setListName("List1");
        action.setVisibleToOther(true);
        action.execute();
        assertFalse(action.hasFieldErrors());
        
        action.setSelectedAction(EditSubjectListAction.DELETE_ACTION);
        action.setListName("Test");
        action.execute();
        assertFalse(action.hasFieldErrors());
        
        action.setListName("List1");
        action.execute();
        assertTrue(workspaceService.deleteAbstractListCalled);
        assertFalse(action.hasFieldErrors());
        
        
        action.setListName("List1");
        action.execute();
        assertFalse(action.hasFieldErrors());
    }
    
    @Test
    public void testAllGlobal() {
        action.setVisibleToOther(false);
        action.setSelectedAction(EditSubjectListAction.EDIT_GLOBAL_ACTION);
        action.validate();
        assertFalse(action.hasFieldErrors());

        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());
        
        assertEquals(13, action.getSubjectIdentifierListing().length());

        assertEquals(EditSubjectListAction.SUCCESS, action.execute());
        
        action.setSelectedAction(EditSubjectListAction.SAVE_ACTION);
        action.setListOldName("Oldname");
        action.setListName("");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListName("List1");
        action.validate();
        assertFalse(action.hasActionErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());
        
        action.setListName("List1");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.setListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());
        
        action.setListName("List1");
        action.setVisibleToOther(true);
        action.execute();
        assertFalse(action.hasFieldErrors());
        
        action.setSelectedAction(EditSubjectListAction.DELETE_ACTION);
        action.setListName("Test");
        action.execute();
        assertFalse(action.hasFieldErrors());
        
        action.setListName("List1");
        action.execute();
        assertTrue(workspaceService.deleteAbstractListCalled);
        assertFalse(action.hasFieldErrors());
        
        
        action.setListName("List1");
        action.execute();
        assertFalse(action.hasFieldErrors());
    }
}
