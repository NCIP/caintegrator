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
import gov.nih.nci.caintegrator.web.ajax.DataElementSearchAjaxUpdater;

import org.junit.Before;
import org.junit.Test;


public class DefineImagingFieldDescriptorActionTest extends AbstractSessionBasedTest {

    private DefineImagingFieldDescriptorAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        studyManagementServiceStub = new StudyManagementServiceStub();
        action = new DefineImagingFieldDescriptorAction();
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
        assertEquals(DataElementSearchAjaxUpdater.ReturnTypeEnum.IMAGING_SOURCE.toString(),
                action.getEntityTypeForSearch());
    }

    @Test
    public void testGetNewDefinitionAction() {
        assertEquals("createNewImagingDefinition", action.getNewDefinitionAction());
    }

    @Test
    public void testGetSaveAnnotationDefinitionAction() {
        assertEquals("updateImagingAnnotationDefinition", action.getSaveAnnotationDefinitionAction());
    }

    @Test
    public void testGetSaveFieldDescriptorTypeAction() {
        assertEquals("saveImagingFieldDescriptorType", action.getSaveFieldDescriptorTypeAction());
    }

    @Test
    public void testGetSelectDataElementAction() {
        assertEquals("selectImagingDataElement", action.getSelectDataElementAction());
    }

    @Test
    public void testGetSelectDefinitionAction() {
        assertEquals("selectImagingDefinition", action.getSelectDefinitionAction());
    }
}
