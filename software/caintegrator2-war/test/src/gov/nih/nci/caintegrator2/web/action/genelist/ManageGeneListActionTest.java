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
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceServiceStub;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.file.FileManagerStub;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;

public class ManageGeneListActionTest extends AbstractSessionBasedTest {

    ManageGeneListAction action = new ManageGeneListAction();
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
        // Test Validate
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.setSelectedAction("createGeneList");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setGeneListName("Test");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertTrue(action.hasActionErrors());
        action.setGeneSymbols("egfr");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());
        assertEquals(1, action.getGeneSymbolList().size());
        action.setGeneSymbols("egfr cox412");
        action.validate();
        assertEquals(1, action.getGeneSymbolList().size());
        action.setGeneSymbols("egfr,cox412");
        action.validate();
        assertEquals(2, action.getGeneSymbolList().size());
        action.setGeneSymbols(null);
        action.setGeneListFile(TestArrayDesignFiles.EMPTY_FILE);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setGeneListFile(TestDataFiles.GENE_LIST_SAMPLES_FILE);
        action.validate();
        assertEquals(4, action.getGeneSymbolList().size());
        action.setGeneSymbols("egfr,cox412");
        action.validate();
        assertEquals(6, action.getGeneSymbolList().size());
        
        // Test execute
        action.setDescription("Test description");
        action.setSelectedAction("createGeneList");
        assertEquals("editPage", action.execute());
        assertTrue(workspaceServiceStub.createGeneListCalled);
    }
}
