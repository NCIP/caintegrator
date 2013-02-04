/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationFile;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.StudyManagementServiceStub;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.Action;


public class DefineImagingFileColumnActionTest extends AbstractSessionBasedTest {

    private DefineImagingFileColumnAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", DefineFileColumnActionTest.class); 

        action = (DefineImagingFileColumnAction) context.getBean("defineImagingFileColumnAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testEditFileColumn() {
        assertEquals(Action.SUCCESS, action.editFileColumn());
    }
    
    @Test
    public void testPrepare() {
        action.getFileColumn().setId(1L);
        action.prepare();
        assertTrue(studyManagementServiceStub.getRefreshedStudyEntityCalled);
    }
    
    
    @Test
    public void testSelectDefintion() {
        action.getDefinitions().add(null);
        assertEquals(Action.SUCCESS, action.selectDefinition());
        assertTrue(studyManagementServiceStub.setDefinitionCalled);
        
        studyManagementServiceStub.clear();
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.selectDefinition());
        assertFalse(action.getActionErrors().isEmpty());
        
    }

    
    @Test
    public void testSelectDataElement() {
        action.getDataElements().add(new CommonDataElement());
        assertEquals(Action.SUCCESS, action.selectDataElement());
        assertTrue(studyManagementServiceStub.setDataElementCalled);
        
        studyManagementServiceStub.clear();
        studyManagementServiceStub.throwValidationException = true;
        assertEquals(Action.ERROR, action.selectDataElement());
        assertFalse(action.getActionErrors().isEmpty());
        
        studyManagementServiceStub.clear();
        action.clearErrorsAndMessages();
        studyManagementServiceStub.throwConnectionException = true;
        assertEquals(Action.ERROR, action.selectDataElement());
        assertFalse(action.getActionErrors().isEmpty());
        
    }
    
    @Test
    @SuppressWarnings("deprecation")
    public void testSetColumnType() {
        AnnotationFile annotationFile = new AnnotationFile();
        action.getFileColumn().setAnnotationFile(annotationFile);
        assertEquals("Annotation", action.getColumnType());
        action.setColumnType(null);
        annotationFile.setIdentifierColumn(action.getFileColumn());
        assertEquals(annotationFile.getIdentifierColumn(), action.getFileColumn());
        assertEquals("Identifier", action.getColumnType());
        action.setColumnType(null);
        annotationFile.setTimepointColumn(action.getFileColumn());
        assertEquals(annotationFile.getTimepointColumn(), action.getFileColumn());
        assertEquals("Timepoint", action.getColumnType());
        action.setColumnType("Annotation");
        assertEquals("Annotation", action.getColumnType());
    }
    
    @Test
    @SuppressWarnings("deprecation")
    public void testSaveColumnType() {
        AnnotationFile annotationFile = new AnnotationFile();
        action.getFileColumn().setAnnotationFile(annotationFile);
        action.setColumnType("Timepoint");
        assertEquals(Action.SUCCESS, action.saveColumnType());
        action.setColumnType("Annotation");
        assertEquals(Action.SUCCESS, action.saveColumnType());
    }

    @Test
    public void testAnnotationDataTypeFunctions() {
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        action.getFileColumn().getFieldDescriptor().setDefinition(new AnnotationDefinition());
        // Assuming we will always have date, string, numeric, and possibly more later.
        assertTrue(action.getAnnotationDataTypes().length >= 3);
    }
    
    @Test
    public void testCreateNewDefinition() throws ValidationException, ParseException {
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        assertEquals(Action.SUCCESS, action.createNewDefinition());
        assertTrue(studyManagementServiceStub.createDefinitionCalled);
        assertFalse(action.isReadOnly());
    }
    
    @Test
    public void testIsColumnTypeAnnotation() {
        action.setColumnType("Annotation");
        assertTrue(action.isColumnTypeAnnotation());
        action.setColumnType("Identifier");
        assertFalse(action.isColumnTypeAnnotation());
    }
    
    @Test
    public void testIsPermissibleOn() {
        action.setColumnType("Identifier");
        assertFalse(action.isPermissibleOn());
        action.setColumnType("Annotation");
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        assertFalse(action.isPermissibleOn());
        action.getFileColumn().getFieldDescriptor().setDefinition(new AnnotationDefinition());
        assertTrue(action.isPermissibleOn());
    }
    
    @Test
    public void testUpdateAnnotationDefinition() throws ParseException {
        action.setColumnType("Annotation");
        action.setFileColumn(new FileColumn());
        action.getFileColumn().setFieldDescriptor(new AnnotationFieldDescriptor());
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        
        AnnotationDefinition definition = new AnnotationDefinition();
        action.getFileColumn().getFieldDescriptor().setDefinition(definition);
        definition.setDataType(AnnotationTypeEnum.DATE);
        
        Collection<PermissibleValue> permissibleValueCollection =  new HashSet<PermissibleValue>();
        definition.getPermissibleValueCollection().addAll(permissibleValueCollection);
        List<String> stringValues = new ArrayList<String>();
        action.setPermissibleUpdateList(stringValues);
        stringValues.add("10-05-2004");
        stringValues.add("01/02/1999");
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertEquals(2, definition.getPermissibleValueCollection().size());
        stringValues.add("11-10-2008");
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertEquals(3, definition.getPermissibleValueCollection().size());
        
        stringValues.add("XYZ");
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertEquals(definition.getPermissibleValueCollection().size(), 0);
    }

}
