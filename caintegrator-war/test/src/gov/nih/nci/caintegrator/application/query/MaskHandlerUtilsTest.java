/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.domain.annotation.MaskedNumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.mask.AbstractAnnotationMask;
import gov.nih.nci.caintegrator.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonOperatorEnum;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

/**
 * Tests for the mask handler utilities.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class MaskHandlerUtilsTest {
    private static final Double MAX_VALUE = 90d;
    private static final Double MASK_VALUE = 95d;

    private Set<AbstractAnnotationMask> masks;
    private NumericComparisonCriterion criterion;
    private NumericAnnotationValue numericAnnotation;
    private MaskedNumericAnnotationValue maskedAnnotation;


    /**
     * Unit test setup.
     */
    @Before
    public void setUp() {
        MaxNumberMask maxNumberMask = new MaxNumberMask();
        maxNumberMask.setMaxNumber(MAX_VALUE);

        NumericRangeMask rangeMask = new NumericRangeMask();
        rangeMask.setNumericRange(5);

        masks = Sets.newHashSet();
        masks.add(rangeMask);
        masks.add(rangeMask);

        criterion = new NumericComparisonCriterion();
        criterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);
        criterion.setAnnotationFieldDescriptor(new AnnotationFieldDescriptor());
        criterion.setNumericValue(MASK_VALUE);

        numericAnnotation = new NumericAnnotationValue();
        numericAnnotation.setNumericValue(MASK_VALUE);

        maskedAnnotation = new MaskedNumericAnnotationValue(numericAnnotation);
    }

    /**
     * Tests retrieval of the new criterion without any provided masks.
     */
    @Test
    public void noMasks() {
        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) MaskHandlerUtils.createMaskedCriterion(
                        Sets.<AbstractAnnotationMask>newHashSet(), criterion);
        assertEquals(criterion, resultingCriterion);
    }

    /**
     * Tests retrieval of new criterion with masks.
     */
    @Test
    public void withMasks() {
        NumericComparisonCriterion resultingCriterion =
                (NumericComparisonCriterion) MaskHandlerUtils.createMaskedCriterion(masks, criterion);
        assertEquals(Double.valueOf(MASK_VALUE), resultingCriterion.getNumericValue());
        assertEquals(NumericComparisonOperatorEnum.GREATEROREQUAL, resultingCriterion.getNumericComparisonOperator());
    }

    /**
     * Tests the retrieval of new annotation without masks.
     */
    @Test
    public void annotationNoMasks() {
        Set<AbstractAnnotationMask> noMasks = Sets.newHashSet();
        MaskedNumericAnnotationValue resultingMaskedValue =
                (MaskedNumericAnnotationValue) MaskHandlerUtils.retrieveMaskedValue(noMasks, maskedAnnotation);
        assertEquals(maskedAnnotation, resultingMaskedValue);

        NumericAnnotationValue resultingNumericValue =
                (NumericAnnotationValue) MaskHandlerUtils.retrieveMaskedValue(noMasks, numericAnnotation);
        assertEquals(numericAnnotation, resultingNumericValue);
    }

    /**
     * Tests the retrieval of new annotation with masks.
     */
    @Test
    public void annoationWithMasks() {
        MaskedNumericAnnotationValue resultingMaskedValue =
                (MaskedNumericAnnotationValue) MaskHandlerUtils.retrieveMaskedValue(masks, maskedAnnotation);
        assertTrue(resultingMaskedValue.isFinalMaskApplied());
        assertEquals(MASK_VALUE, resultingMaskedValue.getNumericValue(), 0);
        assertEquals("95-99", resultingMaskedValue.getMaskedValue());
    }

}
