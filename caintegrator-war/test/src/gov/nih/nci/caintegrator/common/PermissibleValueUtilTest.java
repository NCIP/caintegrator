/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.common.DateUtil;
import gov.nih.nci.caintegrator.common.PermissibleValueUtil;
import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.annotation.StringAnnotationValue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

/**
 * 
 */
public class PermissibleValueUtilTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator.common.PermissibleValueUtil#getDisplayString(gov.nih.nci.caintegrator.domain.annotation.PermissibleValue)}.
     * @throws ParseException 
     */
    @Test
    public void testGetDisplayString() throws ParseException {
        PermissibleValue val1 = new PermissibleValue();
        val1.setValue("ABC");
        assertTrue("ABC".equalsIgnoreCase(val1.toString()));
        
        PermissibleValue val2 = new PermissibleValue();
        val2.setValue("123");
        assertEquals("123",val2.toString());
        
        PermissibleValue val3 = new PermissibleValue();
        Date date = DateUtil.createDate("10-11-2008");  
        val3.setValue(DateUtil.toString(date));
        assertEquals("10/11/2008", val3.toString());
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.common.PermissibleValueUtil#getDisplayPermissibleValue(java.util.Collection)}.
     */
    @Test
    public void testGetDisplayPermissibleValue() {
        Collection <PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        PermissibleValue val1 = new PermissibleValue();
        val1.setValue("123");
        permissibleValueCollection.add(val1);
        val1 = new PermissibleValue();
        val1.setValue("123");
        permissibleValueCollection.add(val1);
        assertTrue(permissibleValueCollection.size() == 2);
        assertTrue(PermissibleValueUtil.getDisplayPermissibleValue(permissibleValueCollection).size() == 1);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.common.PermissibleValueUtil#addNewValues(java.util.Collection, java.util.List)}.
     * @throws ParseException 
     */
    @Test
    public void testAddNewValues() throws ParseException {
        // Test StringPermissibleValue
        Collection <PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("ABC");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 1);
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 1);
        stringValues.add("DEF");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        stringValues = new ArrayList<String>();
        stringValues.add("123.0");
        stringValues.add("456.1");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> stringValues2 = new ArrayList<String>();
        stringValues2.add("456.1");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues2);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test DatePermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        stringValues = new ArrayList<String>();
        stringValues.add(getDisplayDate("11-10-2008"));
        stringValues.add(getDisplayDate("05/28/2008"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> stringValues4 = new ArrayList<String>();
        stringValues4.add(getDisplayDate("11-10-2008"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues4);
        assertTrue(permissibleValueCollection.size() == 2);
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.common.PermissibleValueUtil#removeValues(java.util.Collection, java.util.List)}.
     * @throws ParseException 
     */
    @Test
    public void testRemoveValues() throws ParseException {
        // Test StringPermissibleValue
        Set <PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> annotationValues = new ArrayList<String>();
        annotationValues.add("ABC");
        annotationValues.add("DEF");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        List<String> removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add("ABC");
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        annotationValues = new ArrayList<String>();
        annotationValues.add("123.1");
        annotationValues.add("456.1");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add("456.1");
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = new HashSet<PermissibleValue>();
        annotationValues = new ArrayList<String>();
        annotationValues.add(getDisplayDate("01-15-1995"));
        annotationValues.add(getDisplayDate("12-01-2007"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, annotationValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        removePermissibleValues = new ArrayList<String>();
        removePermissibleValues.add(DateUtil.toString(DateUtil.createDate("12-01-2007")));
        PermissibleValueUtil.removeValue(permissibleValueCollection, removePermissibleValues);
        assertTrue(permissibleValueCollection.size() == 1);
    }

    @Test
    public void testUpdate() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection;
        List<String> newStringValues;
        
        permissibleValueCollection = createStringPermissible();
        newStringValues = new ArrayList<String>();
        newStringValues.add("ABC");
        newStringValues.add("XYZ");
        PermissibleValueUtil.update(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, newStringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test NumericPermissibleValue
        permissibleValueCollection = createNumericPermissible();
        newStringValues = new ArrayList<String>();
        newStringValues.add("123.0");
        newStringValues.add("89.95");
        PermissibleValueUtil.update(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, newStringValues);
        assertTrue(permissibleValueCollection.size() == 2);
        
        // Test DatePermissibleValue
        permissibleValueCollection = createDatePermissible();
        newStringValues = new ArrayList<String>();
        newStringValues.add(getDisplayDate("01-15-1995"));
        newStringValues.add(getDisplayDate("11-02-2008"));
        PermissibleValueUtil.update(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, newStringValues);
        assertTrue(permissibleValueCollection.size() == 2);
    }
    
    @Test
    public void testRetrieveValuesNotPermissible() throws ParseException {
        AnnotationDefinition annotationDefinition1 = new AnnotationDefinition();
        annotationDefinition1.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition1.getPermissibleValueCollection().addAll(createNumericPermissible());

        NumericAnnotationValue validValue = new NumericAnnotationValue();
        validValue.setNumericValue(123.0);
        validValue.setAnnotationDefinition(annotationDefinition1);
        annotationDefinition1.getAnnotationValueCollection().add(validValue);
        
        Set<String> invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition1), annotationDefinition1);
        assertTrue(invalidValues.isEmpty());

        annotationDefinition1.setDataType(null);
        try {
            invalidValues = 
                PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition1), annotationDefinition1);
        } catch (Exception e) {
            assertEquals("Data Type for the Annotation Definition is unknown.", e.getMessage());
        }

        annotationDefinition1.setDataType(AnnotationTypeEnum.NUMERIC);
        NumericAnnotationValue invalidValue = new NumericAnnotationValue();
        invalidValue.setNumericValue(1234.0);
        invalidValue.setAnnotationDefinition(annotationDefinition1);
        annotationDefinition1.getAnnotationValueCollection().add(invalidValue);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition1), annotationDefinition1);
        assertTrue(invalidValues.size() == 1);
        assertTrue(invalidValues.iterator().next().equals("1234.0"));
        
        AnnotationDefinition annotationDefinition2 = new AnnotationDefinition();
        annotationDefinition2.setDataType(AnnotationTypeEnum.STRING);
        annotationDefinition2.getPermissibleValueCollection().addAll(createStringPermissible());
        
        StringAnnotationValue validValue2 = new StringAnnotationValue();
        validValue2.setStringValue("ABC");
        validValue2.setAnnotationDefinition(annotationDefinition2);
        annotationDefinition2.getAnnotationValueCollection().add(validValue2);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition2), annotationDefinition2);
        assertTrue(invalidValues.isEmpty());
        
        StringAnnotationValue invalidValue2 = new StringAnnotationValue();
        invalidValue2.setStringValue("ABCDEF");
        invalidValue2.setAnnotationDefinition(annotationDefinition2);
        annotationDefinition2.getAnnotationValueCollection().add(invalidValue2);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition2), annotationDefinition2);
        assertTrue(invalidValues.size() == 1);
        assertTrue(invalidValues.iterator().next().equals("ABCDEF"));

        AnnotationDefinition annotationDefinition3 = new AnnotationDefinition();
        annotationDefinition3.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinition3.getPermissibleValueCollection().addAll(createDatePermissible());
        DateAnnotationValue validValue3 = new DateAnnotationValue();
        validValue3.setDateValue(new Date());
        validValue3.setAnnotationDefinition(annotationDefinition3);
        annotationDefinition3.getAnnotationValueCollection().add(validValue3);
        invalidValues = 
            PermissibleValueUtil.retrieveValuesNotPermissible(retrieveValues(annotationDefinition3), annotationDefinition3);
        assertTrue(invalidValues.size() == 1);
    }
    
    private Set<Object> retrieveValues(AnnotationDefinition annotationDefinition) {
        Set<Object> objectValues = new HashSet<Object>();
        for (AbstractAnnotationValue value : annotationDefinition.getAnnotationValueCollection()) {
            if (value instanceof StringAnnotationValue) {
                objectValues.add(((StringAnnotationValue) value).getStringValue());
            } else if (value instanceof NumericAnnotationValue) {
                objectValues.add(((NumericAnnotationValue) value).getNumericValue());
            } else if (value instanceof DateAnnotationValue) {
                objectValues.add(((DateAnnotationValue) value).getDateValue());
            } 
        }
        return objectValues;
    }

    private Collection<PermissibleValue> createStringPermissible() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("ABC");
        stringValues.add("DEF");
        stringValues.add("GHI");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.STRING.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private Collection<PermissibleValue> createNumericPermissible() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add("123.0");
        stringValues.add("456.7");
        stringValues.add("789.0");
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.NUMERIC.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private Collection<PermissibleValue> createDatePermissible() throws ParseException {
        Collection<PermissibleValue> permissibleValueCollection = new HashSet<PermissibleValue>();
        List<String> stringValues = new ArrayList<String>();
        stringValues.add(getDisplayDate("01-15-1995"));
        stringValues.add(getDisplayDate("02-01-1987"));
        stringValues.add(getDisplayDate("12-15-2007"));
        PermissibleValueUtil.addNewValue(AnnotationTypeEnum.DATE.getValue(),
                permissibleValueCollection, stringValues);
        return permissibleValueCollection;
    }
    
    private String getDisplayDate(String date) throws ParseException {
        return DateUtil.toString(DateUtil.createDate(date));
    }
}
