/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.abstractlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySetOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.TestArrayDesignFiles;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.SubjectList;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

public class ManageListActionTest extends AbstractSessionBasedTest {

    private ManageListAction action;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        SessionHelper.getInstance().getDisplayableUserWorkspace().setCurrentStudySubscription(new StudySubscription());
        action = new ManageListAction();
        action.setFileManager(fileManager);
        action.setWorkspaceService(workspaceService);
        action.prepare();
    }

    @Test
    public void validate() {
        action.setVisibleToOther(false);
        // Test Validate
        action.validate();
        assertFalse(action.hasFieldErrors());
        action.setSelectedAction("createList");
        action.validate();
        assertTrue(action.hasFieldErrors());

        action.setListName(RandomStringUtils.randomAlphabetic(150));
        action.validate();
        assertTrue(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());
        assertEquals(1, action.getFieldErrors().size());

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
    }

    @Test
    public void testAll() throws Exception {
        // Test execute
        action.setDescription("Test description");
        action.setSelectedAction("createList");
        action.setListType(ListTypeEnum.GENE.getValue());
        assertEquals("editGenePage", action.execute());
        verify(workspaceService, times(1)).createGeneList(any(GeneList.class), anySetOf(String.class));
        // Subject List
        action.setListType(ListTypeEnum.SUBJECT.getValue());
        assertEquals("editSubjectPage", action.execute());
        verify(workspaceService, times(1)).createSubjectList(any(SubjectList.class), anySetOf(String.class));

        action.setSelectedAction("cancel");
        assertEquals("homePage", action.execute());
    }

    @Test
    public void testAllGlobal() throws Exception {
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
        verify(workspaceService, times(1)).createGeneList(any(GeneList.class), anySetOf(String.class));
        // Subject List
        action.setListType(ListTypeEnum.SUBJECT.getValue());
        assertEquals("editGlobalSubjectPage", action.execute());
        verify(workspaceService, times(1)).createSubjectList(any(SubjectList.class), anySetOf(String.class));
    }

    /**
     * Tests validation of a list's description.
     */
    @Test
    public void validateListDescription() {
        action.setSelectedAction("createList");
        action.setListName("Test List");
        action.setDescription("");
        action.setListFile(TestDataFiles.GENE_LIST_SAMPLES_FILE);
        action.setListFileFileName(TestDataFiles.GENE_LIST_SAMPLES_FILE_PATH);
        action.setListFileContentType("application/vnd.ms-excel");
        action.setGeneInputElements("egfr,cox412");
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());

        action.setDescription(RandomStringUtils.random(Cai2Util.MAX_LIST_DESCRIPTION_LENGTH));
        action.validate();
        assertFalse(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());

        action.setDescription(RandomStringUtils.random(Cai2Util.MAX_LIST_DESCRIPTION_LENGTH + 1));
        action.validate();
        assertTrue(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());
        assertEquals(1, action.getFieldErrors().size());
    }

    /**
     * Tests file type validation.
     */
    @Test
    public void validateFileType() {
        action.setSelectedAction("createList");
        action.setListName("Test List");
        action.setDescription("");
        action.setListFile(TestDataFiles.GENE_LIST_SAMPLES_FILE);
        action.setListFileFileName("incorrect");
        action.setListFileContentType("foo");
        action.setGeneInputElements("egfr,cox412");
        action.validate();
        assertTrue(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());

        action.clearErrorsAndMessages();
        action.setListFileContentType("application/vnd.ms-excel");
        action.validate();
        assertTrue(action.hasFieldErrors());
        assertFalse(action.hasActionErrors());
        assertEquals(1, action.getFieldErrors().size());
    }
}
