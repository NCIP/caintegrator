/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;

import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;

import org.junit.Before;
import org.junit.Test;



public class AbstractAnnotationValueTest {
    
    AnnotationDefinition stringAnnotationDefinition;
    AnnotationDefinition dateAnnotationDefinition;
    AnnotationDefinition numericAnnotationDefinition;

    @Before
    public void setUp() throws Exception {
        stringAnnotationDefinition = new AnnotationDefinition();
        stringAnnotationDefinition.setDataType(AnnotationTypeEnum.STRING);
        dateAnnotationDefinition = new AnnotationDefinition();
        dateAnnotationDefinition.setDataType(AnnotationTypeEnum.DATE);
        numericAnnotationDefinition = new AnnotationDefinition();
        numericAnnotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
    }
    
    private void clearDefinitionValues() {
        stringAnnotationDefinition.getAnnotationValueCollection().clear();
        numericAnnotationDefinition.getAnnotationValueCollection().clear();
        dateAnnotationDefinition.getAnnotationValueCollection().clear();
    }
    
    @Test
    public void testConvertStringAnnotationValue() throws ValidationException {
        // Convert to a Numeric type.
        StringAnnotationValue stringToNumeric = new StringAnnotationValue();
        stringToNumeric.setAnnotationDefinition(stringAnnotationDefinition);
        stringAnnotationDefinition.getAnnotationValueCollection().add(stringToNumeric);
        stringToNumeric.setStringValue("invalidValue");
        stringToNumeric.convertAnnotationValue(stringAnnotationDefinition);
        assertEquals(1, stringAnnotationDefinition.getAnnotationValueCollection().size());
        try {
            stringToNumeric.convertAnnotationValue(numericAnnotationDefinition);
            fail();
        } catch (ValidationException e) {
            clearDefinitionValues();
            stringToNumeric.setAnnotationDefinition(stringAnnotationDefinition);
            stringAnnotationDefinition.getAnnotationValueCollection().add(stringToNumeric);
        }
        stringToNumeric.setStringValue("1.23");
        stringToNumeric.convertAnnotationValue(numericAnnotationDefinition);
        assertEquals(Double.valueOf(1.23), ((NumericAnnotationValue)numericAnnotationDefinition.getAnnotationValueCollection().iterator().next()).getNumericValue());
        assertTrue(stringAnnotationDefinition.getAnnotationValueCollection().isEmpty());
        
        // Convert to a Date type.
        StringAnnotationValue stringToDate = new StringAnnotationValue();
        stringToDate.setAnnotationDefinition(stringAnnotationDefinition);
        stringAnnotationDefinition.getAnnotationValueCollection().add(stringToDate);
        stringToDate.setStringValue("invalidValue");
        try {
            stringToDate.convertAnnotationValue(dateAnnotationDefinition);
            fail();
        } catch (ValidationException e) {
            clearDefinitionValues();
            stringToDate.setAnnotationDefinition(stringAnnotationDefinition);
            stringAnnotationDefinition.getAnnotationValueCollection().add(stringToDate);
        }
        
        stringToDate.setStringValue("01/01/2001");
        stringToDate.convertAnnotationValue(dateAnnotationDefinition);
        assertTrue(stringAnnotationDefinition.getAnnotationValueCollection().isEmpty());
        assertEquals("01/01/2001", ((DateAnnotationValue)dateAnnotationDefinition.getAnnotationValueCollection().iterator().next()).toString());
        
    }
    
    @Test
    public void testConvertNumericAnnotationValue() throws ValidationException {
        NumericAnnotationValue numericValue = new NumericAnnotationValue();
        numericValue.setAnnotationDefinition(numericAnnotationDefinition);
        numericAnnotationDefinition.getAnnotationValueCollection().add(numericValue);
        numericValue.setNumericValue(Double.valueOf(1.23));
        numericValue.convertAnnotationValue(numericAnnotationDefinition);
        assertEquals(1, numericAnnotationDefinition.getAnnotationValueCollection().size());
        
        numericValue.convertAnnotationValue(stringAnnotationDefinition);
        assertEquals("1.23", ((StringAnnotationValue)stringAnnotationDefinition.getAnnotationValueCollection().iterator().next()).getStringValue());
        
        clearDefinitionValues();
        numericValue.setAnnotationDefinition(numericAnnotationDefinition);
        numericAnnotationDefinition.getAnnotationValueCollection().add(numericValue);
        try {
            numericValue.convertAnnotationValue(dateAnnotationDefinition);
            fail();
        } catch (ValidationException e) {
            // NOOP, should fail.
        }
    }
    
    @Test
    public void testConvertDateAnnotationValue() throws ValidationException, ParseException {
        DateAnnotationValue dateValue = new DateAnnotationValue();
        dateValue.setAnnotationDefinition(dateAnnotationDefinition);
        dateAnnotationDefinition.getAnnotationValueCollection().add(dateValue);
        dateValue.setDateValue(DateUtil.createDate("01/01/2001"));
        dateValue.convertAnnotationValue(dateAnnotationDefinition);
        assertEquals(1, dateAnnotationDefinition.getAnnotationValueCollection().size());
        
        dateValue.convertAnnotationValue(stringAnnotationDefinition);
        assertEquals("01/01/2001", ((StringAnnotationValue)stringAnnotationDefinition.getAnnotationValueCollection().iterator().next()).getStringValue());
        
        clearDefinitionValues();
        dateValue.setAnnotationDefinition(numericAnnotationDefinition);
        numericAnnotationDefinition.getAnnotationValueCollection().add(dateValue);
        try {
            dateValue.convertAnnotationValue(numericAnnotationDefinition);
            fail();
        } catch (ValidationException e) {
            // NOOP, should fail.
        }
    }
}
