/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import gov.nih.nci.caintegrator2.common.NumericUtil;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.MaskedNumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;

/**
 * 
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See createMaskedCriterion function.
public class MaxNumberMaskHandler extends AbstractAnnotationMaskHandler {
    private static final String PLUS_SYMBOL = "+";
    private final MaxNumberMask mask;
    
    /**
     * Constructor.
     * @param mask to handle.
     */
    public MaxNumberMaskHandler(MaxNumberMask mask) {
        this.mask = mask;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractAnnotationValue retrieveMaskedValue(AbstractAnnotationValue originalValue) {
        MaskedNumericAnnotationValue maskedValue = createMaskedNumericValue(originalValue);
        if (maskedValue.isFinalMaskApplied() || maskedValue.getNumericValue() == null) {
            return maskedValue;
        }
        if (maskedValue.getNumericValue() >= mask.getMaxNumber()) {
            maskedValue.setMaskedValue(NumericUtil.formatDisplay(mask.getMaxNumber()) + PLUS_SYMBOL);
            maskedValue.setNumericValue(mask.getMaxNumber());
            maskedValue.setFinalMaskApplied(true);
        }
        return maskedValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.CyclomaticComplexity") // Must have something for all the different comparison types.
    protected AbstractCriterion createMaskedCriterion(AbstractCriterion originalCriterion) {
        if (!(originalCriterion instanceof NumericComparisonCriterion)) {
            throw new IllegalArgumentException(
                    "Invalid type, must be NumericComparisonCriterion to apply the MaxNumberMask");
        }
        NumericComparisonCriterion abstractCriterion = (NumericComparisonCriterion) originalCriterion;
        if (abstractCriterion.isFinalMaskApplied()) {
            return abstractCriterion;
        }
        if (abstractCriterion.getNumericValue() >= mask.getMaxNumber()) {
            if (NumericComparisonOperatorEnum.GREATER.equals(abstractCriterion.getNumericComparisonOperator())
                    || NumericComparisonOperatorEnum.GREATEROREQUAL.equals(abstractCriterion
                            .getNumericComparisonOperator())
                    || NumericComparisonOperatorEnum.EQUAL.equals(abstractCriterion.getNumericComparisonOperator())) {
                    abstractCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
                } else {
                    abstractCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);
                }
            abstractCriterion.setNumericValue(mask.getMaxNumber());
            abstractCriterion.setFinalMaskApplied(true);
        }
        return abstractCriterion;
    }

}
