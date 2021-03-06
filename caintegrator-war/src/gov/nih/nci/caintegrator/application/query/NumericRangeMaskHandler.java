/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.MaskedNumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonOperatorEnum;

/**
 * Numeric Range Mask Handler.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class NumericRangeMaskHandler extends AbstractAnnotationMaskHandler<NumericComparisonCriterion> {
    private static final String RANGE_SYMBOL = "-";
    private final NumericRangeMask mask;

    /**
     * Constructor.
     * @param mask to handle.
     */
    public NumericRangeMaskHandler(NumericRangeMask mask) {
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
        int numericRange = mask.getNumericRange();
        Long lowNumber = retrieveLowNumber(maskedValue.getNumericValue(), numericRange);
        Long highNumber = (lowNumber + numericRange) - 1;
        maskedValue.setMaskedValue(lowNumber + RANGE_SYMBOL + highNumber);
        maskedValue.setNumericValue(Double.valueOf(lowNumber));
        maskedValue.setFinalMaskApplied(true);
        return maskedValue;
    }

    private Long retrieveLowNumber(Double numericValue, int numericRange) {
        Long roundedNumericValue = Math.round(numericValue);
        Long rangeDifference = roundedNumericValue % numericRange;
        return roundedNumericValue - rangeDifference;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected AbstractCriterion createMaskedCriterion(NumericComparisonCriterion originalCriterion) {
        NumericComparisonCriterion numericCriterion = originalCriterion;
        if (numericCriterion.isFinalMaskApplied()) {
            return numericCriterion;
        }
        int numericRange = mask.getNumericRange();
        Long lowNumber = retrieveLowNumber(numericCriterion.getNumericValue(),
                numericRange);
        Long highNumber = (lowNumber + numericRange) - 1;
        AbstractCriterion newCriterion =
                retrieveNewCriterion(originalCriterion, numericCriterion, lowNumber, highNumber);
        newCriterion.setFinalMaskApplied(true);
        return newCriterion;
    }

    private AbstractCriterion retrieveNewCriterion(AbstractCriterion originalCriterion,
            NumericComparisonCriterion numericCriterion, Long lowNumber, Long highNumber) {
        AbstractCriterion newCriterion;
        switch (numericCriterion.getNumericComparisonOperator()) {
        case GREATER:
            newCriterion = retrieveGreaterThanLowNumberCriterion(numericCriterion, lowNumber);
            break;
        case GREATEROREQUAL:
            newCriterion =  retrieveGreaterThanLowNumberCriterion(numericCriterion, lowNumber);
            break;
        case LESS:
            newCriterion =  retrieveLessThanHighNumberCriterion(numericCriterion, highNumber);
            break;
        case LESSOREQUAL:
            newCriterion =  retrieveLessThanHighNumberCriterion(numericCriterion, highNumber);
            break;
        case EQUAL:
            newCriterion =  handleEqualsOperator(numericCriterion, lowNumber, highNumber);
            break;
        case NOTEQUAL:
            newCriterion =  handleNotEqualsOperator(numericCriterion, lowNumber, highNumber);
            break;
        default:
            newCriterion =  originalCriterion;
            break;
        }
        return newCriterion;
    }

    private NumericComparisonCriterion retrieveGreaterThanLowNumberCriterion(
            NumericComparisonCriterion originalCriterion, Long lowNumber) {
        NumericComparisonCriterion newCriterion = new NumericComparisonCriterion();
        newCriterion.setNumericValue(Double.valueOf(lowNumber));
        newCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATEROREQUAL);
        newCriterion.setAnnotationFieldDescriptor(originalCriterion.getAnnotationFieldDescriptor());
        newCriterion.setEntityType(originalCriterion.getEntityType());
        return newCriterion;
    }

    private NumericComparisonCriterion retrieveLessThanHighNumberCriterion(NumericComparisonCriterion originalCriterion,
            Long highNumber) {
        NumericComparisonCriterion newCriterion = new NumericComparisonCriterion();
        newCriterion.setNumericValue(Double.valueOf(highNumber));
        newCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESSOREQUAL);
        newCriterion.setAnnotationFieldDescriptor(originalCriterion.getAnnotationFieldDescriptor());
        newCriterion.setEntityType(originalCriterion.getEntityType());
        return newCriterion;
    }

    private CompoundCriterion handleEqualsOperator(NumericComparisonCriterion originalCriterion,
            Long lowNumber, Long highNumber) {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.AND);
        compoundCriterion.getCriterionCollection().add(
                retrieveLessThanHighNumberCriterion(originalCriterion, highNumber));
        compoundCriterion.getCriterionCollection().add(
                retrieveGreaterThanLowNumberCriterion(originalCriterion, lowNumber));
        return compoundCriterion;
    }

    private CompoundCriterion handleNotEqualsOperator(NumericComparisonCriterion originalCriterion, Long lowNumber,
            Long highNumber) {
        CompoundCriterion compoundCriterion = new CompoundCriterion();
        compoundCriterion.setBooleanOperator(BooleanOperatorEnum.OR);
        NumericComparisonCriterion greaterThanHighNumberCriterion = retrieveLessThanHighNumberCriterion(
                originalCriterion, highNumber);
        greaterThanHighNumberCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.GREATER);
        NumericComparisonCriterion lowerThanLowNumberCriterion = retrieveGreaterThanLowNumberCriterion(
                originalCriterion, lowNumber);
        lowerThanLowNumberCriterion.setNumericComparisonOperator(NumericComparisonOperatorEnum.LESS);

        compoundCriterion.getCriterionCollection().add(greaterThanHighNumberCriterion);
        compoundCriterion.getCriterionCollection().add(lowerThanLowNumberCriterion);
        return compoundCriterion;
    }
}
