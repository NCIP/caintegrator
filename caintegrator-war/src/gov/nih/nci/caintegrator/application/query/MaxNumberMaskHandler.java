/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.common.NumericUtil;
import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.MaskedNumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonOperatorEnum;

/**
 * Max Number Mask Handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class MaxNumberMaskHandler extends AbstractAnnotationMaskHandler<NumericComparisonCriterion> {
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
    protected AbstractCriterion createMaskedCriterion(NumericComparisonCriterion originalCriterion) {
        NumericComparisonCriterion abstractCriterion = originalCriterion;
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
