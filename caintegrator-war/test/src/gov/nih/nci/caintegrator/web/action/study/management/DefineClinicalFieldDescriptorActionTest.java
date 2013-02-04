/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.study.management.DefineClinicalFieldDescriptorAction;
import gov.nih.nci.caintegrator.web.ajax.DataElementSearchAjaxUpdater;

import org.junit.Before;
import org.junit.Test;


public class DefineClinicalFieldDescriptorActionTest extends AbstractSessionBasedTest {

    private DefineClinicalFieldDescriptorAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new DefineClinicalFieldDescriptorAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
    }


    @Test
    public void testPrepare() {
        action.getFieldDescriptor().setId(1L);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }

    @Test
    public void testGetEntityTypeForSearch() {
        assertEquals(DataElementSearchAjaxUpdater.ReturnTypeEnum.CLINICAL_SOURCE.toString(), action.getEntityTypeForSearch());
    }

    @Test
    public void testGetNewDefinitionAction() {
        assertEquals("createNewClinicalDefinition", action.getNewDefinitionAction());
    }

    @Test
    public void testGetSaveAnnotationDefinitionAction() {
        assertEquals("updateClinicalAnnotationDefinition", action.getSaveAnnotationDefinitionAction());
    }

    @Test
    public void testGetSaveFieldDescriptorTypeAction() {
        assertEquals("saveClinicalFieldDescriptorType", action.getSaveFieldDescriptorTypeAction());
    }

    @Test
    public void testGetSelectDataElementAction() {
        assertEquals("selectClinicalDataElement", action.getSelectDataElementAction());
    }

    @Test
    public void testGetSelectDefinitionAction() {
        assertEquals("selectClinicalDefinition", action.getSelectDefinitionAction());
    }
}
