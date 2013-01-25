/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.DataElementSearchAjaxUpdater;

import org.junit.Before;
import org.junit.Test;


public class DefineGroupFieldDescriptorActionTest extends AbstractSessionBasedTest {

    private DefineGroupFieldDescriptorAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new DefineGroupFieldDescriptorAction();
        action.setStudyManagementService(studyManagementServiceStub);
        action.setWorkspaceService(workspaceService);
    }


    @Test
    public void testPrepare() {
        action.getFieldDescriptor().setId(1L);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        action.setGroupId("1");
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }

    @Test
    public void testGetEntityTypeForSearch() {
        assertEquals(DataElementSearchAjaxUpdater.ReturnTypeEnum.GROUP_SOURCE.toString(), action.getEntityTypeForSearch());
    }

    @Test
    public void testGetNewDefinitionAction() {
        assertEquals("createNewGroupDefinition", action.getNewDefinitionAction());
    }

    @Test
    public void testGetSaveAnnotationDefinitionAction() {
        assertEquals("updateGroupAnnotationDefinition", action.getSaveAnnotationDefinitionAction());
    }

    @Test
    public void testGetSaveFieldDescriptorTypeAction() {
        assertEquals("saveGroupFieldDescriptorType", action.getSaveFieldDescriptorTypeAction());
    }

    @Test
    public void testGetSelectDataElementAction() {
        assertEquals("selectGroupDataElement", action.getSelectDataElementAction());
    }

    @Test
    public void testGetSelectDefinitionAction() {
        assertEquals("selectGroupDefinition", action.getSelectDefinitionAction());
    }
}
