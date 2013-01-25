/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

/**
 * 
 */
public class EditAnnotationGroupActionTest extends AbstractSessionBasedTest {

    private EditAnnotationGroupAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditClinicalSourceActionTest.class); 
        action = (EditAnnotationGroupAction) context.getBean("editAnnotationGroupAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
        action.clearErrorsAndMessages();
    }
    
    @Test
    public void testValidate() {
        action.setSelectedAction("cancel");
        action.validate();
        action.setSelectedAction("save");
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setGroupName("New Group");
        action.validate();
        assertFalse(action.hasFieldErrors());
        AnnotationGroup ag = new AnnotationGroup();
        ag.setName("New Group");
        action.getStudy().getAnnotationGroups().add(ag);
        action.validate();
        assertTrue(action.hasFieldErrors());
        action.setGroupName("New Group2");
        action.setAnnotationGroupFile(TestDataFiles.ANNOTATION_GROUP_FILE);
        action.validate();
        assertFalse(action.hasFieldErrors());
    }

    @Test
    public void testSettingGetting() {
        action.setGroupName(null);
        assertNull(action.getGroupName());
        action.setGroupName("Group 1   ");
        assertEquals("Group 1", action.getGroupName());
        assertFalse(action.isExistingGroup());
        action.setAnnotationGroup(new AnnotationGroup());
        action.getAnnotationGroup().setId(1L);
        assertTrue(action.isExistingGroup());
        assertNull(action.getSelectedAction());
        action.setAnnotationGroupFile(TestDataFiles.ANNOTATION_GROUP_FILE);
        assertEquals("csvtest_annotationGroup.csv", action.getAnnotationGroupFile().getName());
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
        action.setSelectedAction("cancel");
        assertEquals(Action.SUCCESS, action.execute());
        action.setSelectedAction("save");
        assertEquals(Action.SUCCESS, action.execute());
    }

    @Test
    public void testPrepare() {
        action.prepare();
        assertFalse(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        action.getAnnotationGroup().setId(1l);
        assertTrue(action.isExistingGroup());
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        assertTrue(action.isFileUpload());
    }
    
    @Test
    public void testSave() {
        assertEquals(Action.SUCCESS, action.save());
        assertTrue(studyManagementServiceStub.saveCalled);
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.save());
    }
    
    @Test
    public void testDelete() {
        assertEquals(Action.SUCCESS, action.delete());
        assertTrue(studyManagementServiceStub.deleteCalled);
    }

    @Test
    public void testSaveFieldDescriptors() {
        List<DisplayableAnnotationFieldDescriptor> displayableFields = new ArrayList<DisplayableAnnotationFieldDescriptor>();
        action.setDisplayableFields(displayableFields);
        assertEquals(Action.SUCCESS, action.saveFieldDescriptors());
    }
    
    @Test
    public void testIsDeletable() {
        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setStudy(new Study());
        annotationGroup.getStudy().setStudyConfiguration(new StudyConfiguration());
        annotationGroup.setName("test");
        
        assertTrue(annotationGroup.isDeletable());
        annotationGroup.getAnnotationFieldDescriptors().add(new AnnotationFieldDescriptor());
        assertTrue(annotationGroup.isDeletable());
        
        annotationGroup.getStudy().getStudyConfiguration().addClinicalConfiguration(new DelimitedTextClinicalSourceConfiguration());
        assertFalse(annotationGroup.isDeletable());
        annotationGroup.getStudy().getStudyConfiguration().getClinicalConfigurationCollection().clear();
        assertTrue(annotationGroup.isDeletable());
        
        annotationGroup.getStudy().getStudyConfiguration().getImageDataSources().add(new ImageDataSourceConfiguration());
        assertFalse(annotationGroup.isDeletable());
    }
}
