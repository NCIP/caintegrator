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
import gov.nih.nci.caintegrator.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonOperatorEnum;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Numeric Range Mask handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class MaxNumberMaskHandlerTest {
    private static final Double MAX_VALUE = 90d;
    private static final Double MASK_VALUE = 95d;

    private MaxNumberMaskHandler handler;
    private NumericComparisonCriterion criterion;
    private NumericAnnotationValue numericAnnoation;
    private MaskedNumericAnnotationValue maskedAnnotation;

    /**
     * Unit test setup.
     */
    @Before
    public void setUp() {
        MaxNumberMask maxNumberMask = new MaxNumberMask();
        maxNumberMask.setMaxNumber(MAX_VALUE);

        handler = new MaxNumberMaskHandler(maxNumberMask);

        criterion = new NumericComparisonCriterion();
        criterion.setAnnotationFieldDescriptor(new AnnotationFieldDescriptor());
        criterion.setNumericValue(MASK_VALUE);

        numericAnnoation = new NumericAnnotationValue();
        numericAnnoation.setNumericValue(MASK_VALUE);

        maskedAnnotation = new MaskedNumericAnnotationValue(numericAnnoation);
    }

    /**
     * Tests masking when criterion value is great than the maximum value.
     */
    @Test
    public void greaterThanMaxNumber() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);

        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) handler.createMaskedCriterion(criterion);
        assertTrue(resultingCriterion.getNumericValue().equals(MAX_VALUE));
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL, resultingCriterion.getNumericComparisonOperator());
    }

    /**
     * Tests masking when criterion value is great than the maximum value.
     */
    @Test
    public void greaterThanMaxNumberWithLess() {
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);

        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) handler.createMaskedCriterion(criterion);
        assertTrue(resultingCriterion.getNumericValue().equals(MAX_VALUE));
        assertEquals(NumericComparisonOperatorEnum.LESS, resultingCriterion.getNumericComparisonOperator());
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
        assertTrue(resultingAnnotation.getNumericValue().equals(MAX_VALUE));
        assertTrue(resultingAnnotation.isFinalMaskApplied());
        assertEquals("90+", resultingAnnotation.getMaskedValue());
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
        assertEquals("0", resultingAnnotation.getMaskedValue());
    }

    /**
     * Tests masking of masked annotation values.
     */
    @Test
    public void maskMaskedAnnotationValue() {
        MaskedNumericAnnotationValue resultingAnnotation =
                (MaskedNumericAnnotationValue) handler.retrieveMaskedValue(maskedAnnotation);
        assertTrue(resultingAnnotation.getNumericValue().equals(MAX_VALUE));
        assertTrue(resultingAnnotation.isFinalMaskApplied());
        assertEquals("90+", resultingAnnotation.getMaskedValue());
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
        assertEquals("95", resultingAnnotation.getMaskedValue());
    }

}