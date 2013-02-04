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
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.file.FileManagerStub;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

public class ManageListActionTest extends AbstractSessionBasedTest {

    ManageListAction action = new ManageListAction();
    WorkspaceServiceStub workspaceServiceStub = new WorkspaceServiceStub();

    @Before
    public void setUp() {
        super.setUp();
        SessionHelper.getInstance().getDisplayableUserWorkspace().
            setCurrentStudySubscription(new StudySubscription());
        action.setWorkspaceService(workspaceServiceStub);     
        action.setFileManager(new FileManagerStub());
    }
    
    @Test
    public void testAll() {
        action.setVisibleToOther(false);
        // Test Validate
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.setSelectedAction("createList");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertTrue(action.hasActionErrors());
        action.setGeneInputElements("egfr");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());
        assertEquals(1, action.getElementList().size());
        action.setGeneInputElements("egfr cox412");
        action.validate();
        assertEquals(1, action.getElementList().size());
        action.setGeneInputElements("egfr,cox412");
        action.validate();
        assertEquals(2, action.getElementList().size());
        action.setGeneInputElements(null);
        action.setListFile(TestArrayDesignFiles.EMPTY_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListFile(TestDataFiles.GENE_LIST_SAMPLES_FILE);
        action.validate();
        assertEquals(4, action.getElementList().size());
        action.setGeneInputElements("egfr,cox412");
        action.validate();
        assertEquals(4, action.getElementList().size());
        action.setGeneInputElements("brac,emd");
        action.validate();
        assertEquals(6, action.getElementList().size());
        action.setListFileFileName(TestDataFiles.GENE_LIST_SAMPLES_FILE_PATH);
        action.setListFileContentType("application/vnd.ms-excel");
        action.validate();
        assertEquals(6, action.getElementList().size());
        
        // Test execute
        action.setDescription("Test description");
        action.setSelectedAction("createList");
        action.setListType(ListTypeEnum.GENE.getValue());
        assertEquals("editGenePage", action.execute());
        assertTrue(workspaceServiceStub.createGeneListCalled);
        // Subject List
        action.setListType(ListTypeEnum.SUBJECT.getValue());
        assertEquals("editSubjectPage", action.execute());
        assertTrue(workspaceServiceStub.createSubjectListCalled);
        
        action.setSelectedAction("cancel");
        assertEquals("homePage", action.execute());
    }
    
    @Test
    public void testAllGlobal() {
        action.setVisibleToOther(true);
        // Test Validate
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.setSelectedAction("createList");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertTrue(action.hasActionErrors());
        action.setGeneInputElements("egfr");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());
        assertEquals(1, action.getElementList().size());
        action.setGeneInputElements("egfr cox412");
        action.validate();
        assertEquals(1, action.getElementList().size());
        action.setGeneInputElements("egfr,cox412");
        action.validate();
        assertEquals(2, action.getElementList().size());
        action.setGeneInputElements(null);
        action.setListFile(TestArrayDesignFiles.EMPTY_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setListFile(TestDataFiles.GENE_LIST_SAMPLES_FILE);
        action.validate();
        assertEquals(4, action.getElementList().size());
        action.setGeneInputElements("egfr,cox412");
        action.validate();
        assertEquals(4, action.getElementList().size());
        action.setGeneInputElements("espn,emd");
        action.validate();
        assertEquals(6, action.getElementList().size());
        
        // Test execute
        action.setDescription("Test description");
        action.setSelectedAction("createList");
        action.setListType(ListTypeEnum.GENE.getValue());
        assertEquals("editGlobalGenePage", action.execute());
        assertTrue(workspaceServiceStub.createGeneListCalled);
        // Subject List
        action.setListType(ListTypeEnum.SUBJECT.getValue());
        assertEquals("editGlobalSubjectPage", action.execute());
        assertTrue(workspaceServiceStub.createSubjectListCalled);
    }
}
