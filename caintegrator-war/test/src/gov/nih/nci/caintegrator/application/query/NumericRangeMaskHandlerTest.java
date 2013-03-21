/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.domain.annotation.MaskedNumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonOperatorEnum;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Numeric Range Mask handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class NumericRangeMaskHandlerTest {
    private static final Double MASK_VALUE = 52d;
    private static final Double LESS_THAN_VALUE = 54d;
    private static final Double GREATER_THAN_VALUE = 50d;

    private NumericRangeMaskHandler handler;
    private NumericComparisonCriterion criterion;
    private NumericAnnotationValue numericAnnoation;
    private MaskedNumericAnnotationValue maskedAnnotation;


    /**
     * Unit test setup.
     */
    @Before
    public void setUp() {
        NumericRangeMask rangeMask = new NumericRangeMask();
        rangeMask.setNumericRange(5);

        handler = new NumericRangeMaskHandler(rangeMask);

        criterion = new NumericComparisonCriterion();
        criterion.setAnnotationFieldDescriptor(new AnnotationFieldDescriptor());
        criterion.setNumericValue(MASK_VALUE);

        numericAnnoation = new NumericAnnotationValue();
        numericAnnoation.setNumericValue(MASK_VALUE);

        maskedAnnotation = new MaskedNumericAnnotationValue(numericAnnoation);
    }

    /**
     * Tests that criterion that have been marked as final are not modified.
     */
    @Test
    public void finalMask() {
        criterion.setFinalMaskApplied(true);
        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) handler.createMaskedCriterion(criterion);
        assertEquals(criterion, resultingCriterion);
    }

    /**
     * Tests handling of the > range mask.
     */
    @Test
    public void greaterThanRange() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);
        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) handler.createMaskedCriterion(criterion);
        assertTrue(resultingCriterion.getNumericValue().equals(GREATER_THAN_VALUE));
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL, resultingCriterion.getNumericComparisonOperator());
    }

    /**
     * Tests handling of the >= range mask.
     */
    @Test
    public void greaterThanOrEqualRange() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) handler.createMaskedCriterion(criterion);
        assertTrue(resultingCriterion.getNumericValue().equals(GREATER_THAN_VALUE));
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL, resultingCriterion.getNumericComparisonOperator());
    }

    /**
     * Tests handling of the < range mask.
     */
    @Test
    public void lessThanRange() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);
        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) handler.createMaskedCriterion(criterion);
        assertTrue(resultingCriterion.getNumericValue().equals(LESS_THAN_VALUE));
        assertEquals(NumericComparisonOperatorEnum.LESSOREQUAL, resultingCriterion.getNumericComparisonOperator());
    }

    /**
     * Tests handling of the <= range mask.
     */
    @Test
    public void lessThanOrEqualRange() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) handler.createMaskedCriterion(criterion);
        assertTrue(resultingCriterion.getNumericValue().equals(LESS_THAN_VALUE));
        assertEquals(NumericComparisonOperatorEnum.LESSOREQUAL, resultingCriterion.getNumericComparisonOperator());
    }


    /**
     * Tests handling of the = range mask.
     */
    @Test
    public void equalsRange() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.EQUAL);
        CompoundCriterion resultingCriterion =
                (CompoundCriterion) handler.createMaskedCriterion(criterion);
        assertEquals(2, resultingCriterion.getCriterionCollection().size());
        for (AbstractCriterion crit : resultingCriterion.getCriterionCollection()) {
            NumericComparisonCriterion numericCriterion = (NumericComparisonCriterion) crit;
            if (NumericComparisonOperatorEnum.LESSOREQUAL.equals(numericCriterion.getNumericComparisonOperator())) {
                assertEquals(Double.valueOf(LESS_THAN_VALUE), numericCriterion.getNumericValue());
            }
            if (NumericComparisonOperatorEnum.GREATEROREQUAL.equals(numericCriterion.getNumericComparisonOperator())) {
                assertEquals(Double.valueOf(GREATER_THAN_VALUE), numericCriterion.getNumericValue());
            }
        }
        assertEquals(BooleanOperatorEnum.AND, resultingCriterion.getBooleanOperator());
    }

    /**
     * Tests handling of the != range mask.
     */
    @Test
    public void notEqualsRange() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.NOTEQUAL);
        CompoundCriterion resultingCriterion =
                (CompoundCriterion) handler.createMaskedCriterion(criterion);
        assertEquals(2, resultingCriterion.getCriterionCollection().size());
        for (AbstractCriterion crit : resultingCriterion.getCriterionCollection()) {
            NumericComparisonCriterion numericCriterion = (NumericComparisonCriterion) crit;
            if (NumericComparisonOperatorEnum.LESS == numericCriterion.getNumericComparisonOperator()) {
                assertEquals(Double.valueOf(GREATER_THAN_VALUE), numericCriterion.getNumericValue());
            }
            if (NumericComparisonOperatorEnum.GREATER == numericCriterion.getNumericComparisonOperator()) {
                assertEquals(Double.valueOf(LESS_THAN_VALUE), numericCriterion.getNumericValue());
            }
        }
        assertEquals(BooleanOperatorEnum.OR, resultingCriterion.getBooleanOperator());
    }

    /**
     * Tests masking of annotation values.
     */
    @Test
    public void annotationNoNumericValue() {
        maskedAnnotation.setNumericValue(null);
        MaskedNumericAnnotationValue resultingAnnotation =
                (MaskedNumericAnnotationValue) handler.retrieveMaskedValue(maskedAnnotation);
        assertEquals(maskedAnnotation, resultingAnnotation);

        numericAnnoation.setNumericValue(null);
        resultingAnnotation =
                (MaskedNumericAnnotationValue) handler.retrieveMaskedValue(numericAnnoation);
        assertNull(resultingAnnotation.getNumericValue());
    }

    /**
     * Tests masking of numeric annotation values that exceed the max value.
     */
    @Test
    public void maskNumericAnnotationValue() {
        MaskedNumericAnnotationValue resultingAnnotation =
                (MaskedNumericAnnotationValue) handler.retrieveMaskedValue(numericAnnoation);
        assertEquals(GREATER_THAN_VALUE, resultingAnnotation.getNumericValue(), 0);
        assertTrue(resultingAnnotation.isFinalMaskApplied());
        assertEquals("50-54", resultingAnnotation.getMaskedValue());
    }

    /**
     * Tests masking of numeric annotation values that do not exceed max value.
     */
    @Test
    public void unmaskedNumericAnnotationValue() {
        numericAnnoation.setNumericValue(0d);
        MaskedNumericAnnotationValue resultingAnnotation =
                (MaskedNumericAnnotationValue) handler.retrieveMaskedValue(numericAnnoation);
        assertTrue(resultingAnnotation.getNumericValue().equals(0d));
        assertEquals("0-4", resultingAnnotation.getMaskedValue());
    }

    /**
     * Tests masking of masked annotation values.
     */
    @Test
    public void maskMaskedAnnotationValue() {
        MaskedNumericAnnotationValue resultingAnnotation =
                (MaskedNumericAnnotationValue) handler.retrieveMaskedValue(maskedAnnotation);
        assertEquals(GREATER_THAN_VALUE, resultingAnnotation.getNumericValue().doubleValue(), 0);
        assertTrue(resultingAnnotation.isFinalMaskApplied());
        assertEquals("50-54", resultingAnnotation.getMaskedValue());
    }

    /**
     * Tests masking of masked annotation values that do not exceed max value.
     */
    @Test
    public void unmaskedMaskedNumericAnnotationValue() {
        maskedAnnotation.setNumericValue(0d);
        MaskedNumericAnnotationValue resultingAnnotation =
                (MaskedNumericAnnotationValue) handler.retrieveMaskedValue(maskedAnnotation);
        assertEquals(maskedAnnotation, resultingAnnotation);
        assertTrue(resultingAnnotation.getNumericValue().equals(0d));
        assertEquals("0-4", resultingAnnotation.getMaskedValue());
    }


}
