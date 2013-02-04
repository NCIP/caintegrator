/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.genelist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.AbstractList;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

public class EditGeneListActionTest extends AbstractSessionBasedTest {

    EditGeneListAction action = new EditGeneListAction();
    StudySubscription subscription = new StudySubscription();
    WorkspaceServiceStub workspaceService = new WorkspaceServiceStub();

    @Before
    public void setUp() {
        super.setUp();
        subscription.setId(1L);
        subscription.setStudy(new Study());
        subscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        subscription.setListCollection(new ArrayList<AbstractList>());
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(subscription);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ActionContext.getContext().getValueStack().setValue("studySubscription", subscription);
        workspaceService.setSubscription(subscription);
        action.setWorkspaceService(workspaceService);
        
        GeneList geneList = new GeneList();
        geneList.setName("List1");
        subscription.getListCollection().add(geneList);
        geneList = new GeneList();
        geneList.setName("List2");
        subscription.getListCollection().add(geneList);
    }
    
    @Test
    public void testAll() {
        action.setSelectedAction("editGeneList");
        action.validate();
        assertFalse(action.hasFieldErrors());
        
        action.setSelectedAction("renameGeneList");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());
        
        action.setGeneListName("List1");
        action.validate();
        assertTrue(action.hasFieldErrors());
        
        action.setGeneListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.clearErrorsAndMessages();
        assertEquals("success", action.execute());
        
        action.setSelectedAction("deleteGeneList");
        action.setGeneListName("Test");
        action.execute();
        assertFalse(action.hasFieldErrors());
        
        action.setGeneListName("List1");
        action.execute();
        assertFalse(action.hasFieldErrors());
    }
}
