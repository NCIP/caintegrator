/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator2.web.ajax.DataElementSearchAjaxUpdater;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class DefineGroupFieldDescriptorActionTest extends AbstractSessionBasedTest {
    
    private DefineGroupFieldDescriptorAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", DefineGroupFieldDescriptorActionTest.class);
        action = (DefineGroupFieldDescriptorAction) context.getBean("defineGroupFieldDescriptorAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
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
