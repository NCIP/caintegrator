/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationFileStub;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;

public class EditClinicalSourceActionTest extends AbstractSessionBasedTest {

    private EditClinicalSourceAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", EditClinicalSourceActionTest.class); 
        action = (EditClinicalSourceAction) context.getBean("editClinicalSourceAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
        setupActionVariables();
    }

    @Test
    public void testExecute() {
        assertEquals(Action.SUCCESS, action.execute());
    }

    @Test
    public void testPrepare() {
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
        assertEquals(2, action.getDisplayableFields().size());
        assertEquals(2, action.getSelectableAnnotationGroups().size());
    }
    
    @Test
    public void testSave() {
        action.prepare();
        
        action.getDisplayableFields().get(0).setAnnotationGroupName("subjectGroup");
        assertEquals(Action.SUCCESS, action.save());
        assertEquals("subjectGroup", action.getDisplayableFields().get(0).getFieldDescriptor().getAnnotationGroup().getName());
    }

    private void setupActionVariables() {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setName("subjectGroup");
        AnnotationGroup annotationGroup2 = new AnnotationGroup();
        annotationGroup2.setName("imageSeriesGroup");
        studyConfiguration.getStudy().getAnnotationGroups().add(annotationGroup);
        studyConfiguration.getStudy().getAnnotationGroups().add(annotationGroup2);
        action.setStudyConfiguration(studyConfiguration);
        
        action.getClinicalSource().setId(1L);
        action.getClinicalSource().setAnnotationFile(createAnnotationFile());
    }
    
    private AnnotationFile createAnnotationFile() {
        AnnotationDefinition subjectDef1 = new AnnotationDefinition();
        subjectDef1.setId(Long.valueOf(1));
        AnnotationDefinition subjectDef2 = new AnnotationDefinition();
        subjectDef2.setId(Long.valueOf(2));
        
        DelimitedTextClinicalSourceConfiguration clinicalConf = new DelimitedTextClinicalSourceConfiguration();
        AnnotationFileStub annotationFile = new AnnotationFileStub();
        clinicalConf.setAnnotationFile(annotationFile);

        addColumn(annotationFile, subjectDef1);
        addColumn(annotationFile, subjectDef2);
        
        return annotationFile;
    }
    
    private void addColumn(AnnotationFile annotationFile, AnnotationDefinition subjectDef) {
        FileColumn column = new FileColumn();
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fieldDescriptor.setShownInBrowse(true);
        fieldDescriptor.setDefinition(subjectDef);
        column.setFieldDescriptor(fieldDescriptor);
        column.setAnnotationFile(annotationFile);
        annotationFile.getColumns().add(column);
    }
}
