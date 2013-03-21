/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.ValidationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for String Annoation values.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class StringAnnotationValueTest {
    private static final String STRING_VALUE = "String Value";
    private StringAnnotationValue annotationValue;

    /**
     * Unit test set up.
     */
    @Before
    public void setUp() {
        annotationValue = new StringAnnotationValue();
        annotationValue.setStringValue(STRING_VALUE);
    }

    /**
     * Tests conversion from a string value back to a string value.
     * @throws ValidationException on unexpected error
     */
    @Test
    public void stringToSameString()  throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.STRING);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);
        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());

        StringAnnotationValue value =
                (StringAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertEquals(STRING_VALUE, value.getStringValue());
        assertEquals(annotationValue, value);
    }

    /**
     * Tests conversion from a string value back to a different string value.
     *
     * @throws ValidationException on unexpected error
     */
    @Test
    public void stringToDifferentString()  throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.STRING);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        AnnotationDefinition otherDefinition = new AnnotationDefinition();
        otherDefinition.setDataType(AnnotationTypeEnum.STRING);
        otherDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(otherDefinition);

        assertTrue(annotationDefinition.getAnnotationValueCollection().isEmpty());
        assertEquals(2, otherDefinition.getAnnotationValueCollection().size());

        StringAnnotationValue value =
                (StringAnnotationValue) CollectionUtils.get(otherDefinition.getAnnotationValueCollection(), 1);
        assertEquals(STRING_VALUE, value.getStringValue());
        assertNotSame(annotationValue, value);
    }

    /**
     * Tests conversion from an invalid string to numeric value.
     *
     * @throws ValidationException on the expected validation exception
     */
    @Test(expected = ValidationException.class)
    public void stringToNumericFailure() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.setStringValue("invalid");
        annotationValue.convertAnnotationValue(annotationDefinition);
    }

    /**
     * Tests conversion from an valid string to numeric value.
     *
     * @throws ValidationException on an unexpected validation exception
     */
    @Test
    public void stringToNumeric() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.setStringValue("1");
        annotationValue.convertAnnotationValue(annotationDefinition);

        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());
        NumericAnnotationValue value =
                (NumericAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertEquals(1, value.getNumericValue(), 0);
    }

    /**
     * Tests conversion from a blank string value to a numeric value.
     *
     * @throws ValidationException on unexpected error
     */
    @Test
    public void blankStringToNumeric()  throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setStringValue(StringUtils.EMPTY);
        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);
        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());

        NumericAnnotationValue value =
                (NumericAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertNull(value.getNumericValue());
    }

    /**
     * Tests conversion from an invalid string to date value.
     *
     * @throws ValidationException on the expected validation exception
     */
    @Test(expected = ValidationException.class)
    public void stringToDateFailure() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.setStringValue("invalid");
        annotationValue.convertAnnotationValue(annotationDefinition);
    }

    /**
     * Tests conversion from an valid string to date value.
     *
     * @throws ValidationException on an unexpected validation exception
     */
    @Test
    public void stringToDate() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.setStringValue("01/01/2001");
        annotationValue.convertAnnotationValue(annotationDefinition);

        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());
        DateAnnotationValue value =
                (DateAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertEquals("01/01/2001", value.toString());
    }
}
