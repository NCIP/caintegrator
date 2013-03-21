/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.ValidationException;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Numeric Annotation values.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class NumericAnnotationValueTest {
    private NumericAnnotationValue annotationValue;

    /**
     * Unit test set up.
     */
    @Before
    public void setUp() {
        annotationValue = new NumericAnnotationValue();
        annotationValue.setNumericValue(1d);
    }

    /**
     * Tests conversion from a numeric value back to the same numeric value.
     *
     * @throws ValidationException on unexpected error
     */
    @Test
    public void numericToSameNumeric()  throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);
        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());

        NumericAnnotationValue value =
                (NumericAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertEquals(annotationValue, value);
        assertEquals(1d, value.getNumericValue(), 0);
    }

    /**
     * Tests conversion from a numeric value back to a different numeric value.
     *
     * @throws ValidationException on unexpected error
     */
    @Test
    public void numericToDifferentNumeric()  throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        AnnotationDefinition otherDefinition = new AnnotationDefinition();
        otherDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        otherDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(otherDefinition);

        assertTrue(annotationDefinition.getAnnotationValueCollection().isEmpty());
        assertEquals(2, otherDefinition.getAnnotationValueCollection().size());
        NumericAnnotationValue value =
                (NumericAnnotationValue) CollectionUtils.get(otherDefinition.getAnnotationValueCollection(), 1);
        assertEquals(1, value.getNumericValue(), 0);
        assertNotSame(annotationValue, value);
    }

    /**
     * Tests conversion from a numeric to a string value.
     *
     * @throws ValidationException on an unexpected validation exception
     */
    @Test
    public void numericToString() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.STRING);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);

        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());
        StringAnnotationValue value =
                (StringAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertEquals("1", value.getStringValue());
    }

    /**
     * Tests conversion from a numeric to date value.
     *
     * @throws ValidationException on the expected validation exception
     */
    @Test(expected = ValidationException.class)
    public void numericToDate() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);
    }
}
