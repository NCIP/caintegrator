/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.StringAnnotationValue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

/**
 * 
 */
public class AnnotationValueUtilTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.AnnotationUtil#getDisplayString(gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue)}.
     * @throws ParseException 
     */
    @Test
    public void testGetDisplayString() throws ParseException {
        StringAnnotationValue val1 = new StringAnnotationValue();
        val1.setStringValue("ABC");
        assertTrue("ABC".equalsIgnoreCase(val1.toString()));
        
        NumericAnnotationValue val2 = new NumericAnnotationValue();
        assertTrue("".equalsIgnoreCase(val2.toString()));
        val2.setNumericValue(Double.valueOf(123.0));
        assertEquals("123", val2.toString());
        
        DateAnnotationValue val3 = new DateAnnotationValue();
        val3.setDateValue(null);
        assertTrue("".equalsIgnoreCase(val3.toString()));
        final SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        Date date = (Date)formatter.parse("10-11-2008");  
        val3.setDateValue(date);
        assertTrue(val3.toString().equalsIgnoreCase("10/11/2008"));
        
        DateAnnotationValue val4 = new DateAnnotationValue();
        final SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        date = (Date)formatter2.parse("10/11/2008");  
        val4.setDateValue(date);
        assertTrue(val4.toString().equalsIgnoreCase("10/11/2008"));
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.common.AnnotationUtil#getDistinctAnnotationValue(java.util.Collection)}.
     */
    @Test
    public void testGetAdditionalValue() {
        Collection <AbstractAnnotationValue> annotationValueCollection = new HashSet<AbstractAnnotationValue>();
        NumericAnnotationValue val1 = new NumericAnnotationValue();
        val1.setNumericValue(Double.valueOf(123));
        annotationValueCollection.add(val1);
        val1 = new NumericAnnotationValue();
        val1.setNumericValue(Double.valueOf(123));
        annotationValueCollection.add(val1);
        assertTrue(annotationValueCollection.size() == 2);
        Set<String> filterList = new HashSet<String>();
        List<String> dataValues = new ArrayList<String>();
        assertTrue(AnnotationUtil.getAdditionalValue(annotationValueCollection, dataValues, filterList).size() == 1);
        dataValues.add("345");
        assertTrue(AnnotationUtil.getAdditionalValue(annotationValueCollection, dataValues, filterList).size() == 2);
        filterList.add("123");
        assertTrue(AnnotationUtil.getAdditionalValue(annotationValueCollection, dataValues, filterList).size() == 1);
    }

}
