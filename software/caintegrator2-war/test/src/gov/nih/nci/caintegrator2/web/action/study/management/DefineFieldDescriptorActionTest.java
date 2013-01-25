/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.NumericRangeMask;
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


public class DefineFieldDescriptorActionTest extends AbstractSessionBasedTest {
    
    private DefineClinicalFieldDescriptorAction action;
    private StudyManagementServiceStub studyManagementServiceStub;

    @Before
    public void setUp() {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext("study-management-action-test-config.xml", DefineFieldDescriptorActionTest.class);
        action = (DefineClinicalFieldDescriptorAction) context.getBean("defineClinicalFieldDescriptorAction");
        studyManagementServiceStub = (StudyManagementServiceStub) context.getBean("studyManagementService");
        studyManagementServiceStub.clear();
    }

    @Test
    public void testEditFileColumn() {
        assertEquals(Action.SUCCESS, action.editFieldDescriptor());
    }
    
    @Test
    public void testPrepare() {
        action.getFieldDescriptor().setId(1L);
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
        FileColumn fileColumn = new FileColumn();
        annotationFile.getColumns().add(fileColumn);
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fileColumn.setFieldDescriptor(fieldDescriptor);
        action.setFieldDescriptor(fieldDescriptor);
        assertEquals("Annotation", action.getFieldDescriptorType());
        action.setFieldDescriptorType(null);
        annotationFile.setIdentifierColumn(fileColumn);
        assertEquals(annotationFile.getIdentifierColumn().getFieldDescriptor(), action.getFieldDescriptor());
        assertEquals("Identifier", action.getFieldDescriptorType());
        action.setFieldDescriptorType(null);
        annotationFile.setTimepointColumn(fileColumn);
        assertEquals(annotationFile.getTimepointColumn().getFieldDescriptor(), action.getFieldDescriptor());
        assertEquals("Timepoint", action.getFieldDescriptorType());
        action.setFieldDescriptorType("Annotation");
        assertEquals("Annotation", action.getFieldDescriptorType());
    }
    
    @Test
    @SuppressWarnings("deprecation")
    public void testSaveColumnType() {
        AnnotationFile annotationFile = new AnnotationFile();
        FileColumn fileColumn = new FileColumn();
        annotationFile.getColumns().add(fileColumn);
        AnnotationFieldDescriptor fieldDescriptor = new AnnotationFieldDescriptor();
        fileColumn.setFieldDescriptor(fieldDescriptor);
        action.setFieldDescriptor(fieldDescriptor);
        action.setFieldDescriptorType("Timepoint");
        assertEquals(Action.SUCCESS, action.saveFieldDescriptorType());
        action.setFieldDescriptorType("Annotation");
        assertEquals(Action.SUCCESS, action.saveFieldDescriptorType());
    }

    @Test
    public void testAnnotationDataTypeFunctions() {
        action.setFieldDescriptor(new AnnotationFieldDescriptor());
        action.getFieldDescriptor().setDefinition(new AnnotationDefinition());
        // Assuming we will always have date, string, numeric, and possibly more later.
        assertTrue(action.getAnnotationDataTypes().length >= 3);
    }
    
    @Test
    public void testCreateNewDefinition() throws ValidationException, ParseException {
        action.setFieldDescriptor(new AnnotationFieldDescriptor());
        assertEquals(Action.SUCCESS, action.createNewDefinition());
        assertTrue(studyManagementServiceStub.createDefinitionCalled);
        assertFalse(action.isReadOnly());
        
    }
    
    @Test
    public void testIsColumnTypeAnnotation() {
        action.setFieldDescriptorType("Annotation");
        assertTrue(action.isColumnTypeAnnotation());
        action.setFieldDescriptorType("Identifier");
        assertFalse(action.isColumnTypeAnnotation());
    }
    
    @Test
    public void testIsPermissibleOn() {
        action.setFieldDescriptorType("Identifier");
        assertFalse(action.isPermissibleOn());
        action.setFieldDescriptorType("Annotation");
        action.setFieldDescriptor(new AnnotationFieldDescriptor());
        assertFalse(action.isPermissibleOn());
        action.getFieldDescriptor().setDefinition(new AnnotationDefinition());
        assertTrue(action.isPermissibleOn());
        
    }
    
    @Test
    public void testUpdateAnnotationDefinition() throws ParseException {
        action.setFieldDescriptorType("Annotation");
        action.setFieldDescriptor(new AnnotationFieldDescriptor());
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        
        AnnotationDefinition definition = new AnnotationDefinition();
        action.getFieldDescriptor().setDefinition(definition);
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
    
    @Test
    public void testUpdateAnnotationDefinitionWithNumericMasks() throws ParseException {
        action.setFieldDescriptorType("Annotation");
        action.setFieldDescriptor(new AnnotationFieldDescriptor());
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        
        AnnotationDefinition definition = new AnnotationDefinition();
        action.getFieldDescriptor().setDefinition(definition);
        definition.setDataType(AnnotationTypeEnum.STRING);
        MaxNumberMask maxNumberMask = new MaxNumberMask();
        NumericRangeMask numericRangeMask = new NumericRangeMask();
        action.getMaskForm().clear();
        action.getMaskForm().setMaxNumberMask(maxNumberMask);
        action.getMaskForm().setNumericRangeMask(numericRangeMask);
        action.getMaskForm().setHasMaxNumberMask(true);
        action.getMaskForm().setHasNumericRangeMask(true);
        assertEquals(false, action.isNumericMaskDisabled());
        action.setReadOnly(true);
        assertEquals(true, action.isNumericMaskDisabled());
        
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertTrue(action.getFieldDescriptor().getAnnotationMasks().isEmpty());
        
        definition.setDataType(AnnotationTypeEnum.NUMERIC);
        action.getPermissibleUpdateList().add("PERMVALUE");
        assertEquals(Action.ERROR, action.updateAnnotationDefinition());
        assertEquals(4, action.getActionErrors().size());
        action.clearActionErrors();
        action.setReadOnly(true);
        assertEquals(false, action.isNumericMaskDisabled());
        maxNumberMask.setMaxNumber(10d);
        numericRangeMask.setNumericRange(1);
        assertEquals(Action.SUCCESS, action.updateAnnotationDefinition());
        assertEquals(2, action.getFieldDescriptor().getAnnotationMasks().size());
    }
}
