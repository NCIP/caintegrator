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
import gov.nih.nci.caintegrator.common.DateUtil;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for String Annoation values.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class DateAnnotationValueTest {
    private DateAnnotationValue annotationValue;
    private final Date firstDayOf2001 = DateUtils.truncate(new Date(), Calendar.YEAR);

    /**
     * Unit test set up.
     */
    @Before
    public void setUp() {
        annotationValue = new DateAnnotationValue();
        annotationValue.setDateValue(firstDayOf2001);
    }

    /**
     * Tests conversion from a date value back to the same date value.
     *
     * @throws ValidationException on unexpected error
     */
    @Test
    public void dateToSameDate()  throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);
        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());

        DateAnnotationValue value =
                (DateAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertEquals(firstDayOf2001, value.getDateValue());
        assertEquals(annotationValue, value);
    }

    /**
     * Tests conversion from a date value back to a different date value.
     *
     * @throws ValidationException on unexpected error
     */
    @Test
    public void dateToDifferentDate()  throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.DATE);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        AnnotationDefinition otherDefinition = new AnnotationDefinition();
        otherDefinition.setDataType(AnnotationTypeEnum.DATE);
        otherDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(otherDefinition);
        assertTrue(annotationDefinition.getAnnotationValueCollection().isEmpty());
        assertEquals(2, otherDefinition.getAnnotationValueCollection().size());

        DateAnnotationValue value =
                (DateAnnotationValue) CollectionUtils.get(otherDefinition.getAnnotationValueCollection(), 1);
        assertEquals(firstDayOf2001, value.getDateValue());
        assertNotSame(annotationValue, value);
    }

    /**
     * Tests conversion from an valid date to string value.
     *
     * @throws ValidationException on an unexpected validation exception
     */
    @Test
    public void dateToString() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.STRING);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);

        assertEquals(1, annotationDefinition.getAnnotationValueCollection().size());
        StringAnnotationValue value =
                (StringAnnotationValue) annotationDefinition.getAnnotationValueCollection().iterator().next();
        assertEquals(DateUtil.toString(firstDayOf2001), value.toString());
    }

    /**
     * Tests conversion from a date to a numeric value.
     *
     * @throws ValidationException on the expected validation exception
     */
    @Test(expected = ValidationException.class)
    public void dateToNumeric() throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setDataType(AnnotationTypeEnum.NUMERIC);
        annotationDefinition.getAnnotationValueCollection().add(annotationValue);

        annotationValue.setAnnotationDefinition(annotationDefinition);
        annotationValue.convertAnnotationValue(annotationDefinition);
    }
}
