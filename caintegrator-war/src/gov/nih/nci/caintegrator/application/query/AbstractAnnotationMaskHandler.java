/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.MaskedNumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.mask.AbstractAnnotationMask;
import gov.nih.nci.caintegrator.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public abstract class AbstractAnnotationMaskHandler {
    
    static AbstractAnnotationValue retrieveMaskedValue(Set<AbstractAnnotationMask> masks,
            AbstractAnnotationValue originalValue) {
        AbstractAnnotationValue maskedValue = originalValue;
        List<AbstractAnnotationMask> sortedMasks = new ArrayList<AbstractAnnotationMask>(masks);
        Collections.sort(sortedMasks);
        for (AbstractAnnotationMask mask : sortedMasks) {
            if (mask instanceof MaxNumberMask) {
                maskedValue = new MaxNumberMaskHandler((MaxNumberMask) mask).retrieveMaskedValue(maskedValue);
            } else if (mask instanceof NumericRangeMask) {
                maskedValue = new NumericRangeMaskHandler((NumericRangeMask) mask).retrieveMaskedValue(maskedValue);
            } else {
                throw new IllegalStateException("Undefined mask used.");
            }
        }
        return maskedValue;
    }
    
    static AbstractCriterion createMaskedCriterion(Set<AbstractAnnotationMask> masks,
            AbstractCriterion originalValue) {
        List<AbstractAnnotationMask> sortedMasks = new ArrayList<AbstractAnnotationMask>(masks);
        Collections.sort(sortedMasks);
        AbstractCriterion abstractCriterion = originalValue;
        for (AbstractAnnotationMask mask : sortedMasks) {
            if (mask instanceof MaxNumberMask) {
                abstractCriterion = new MaxNumberMaskHandler((MaxNumberMask) mask)
                        .createMaskedCriterion(abstractCriterion);
            } else if (mask instanceof NumericRangeMask) {
                abstractCriterion = new NumericRangeMaskHandler((NumericRangeMask) mask)
                        .createMaskedCriterion(abstractCriterion);
            } else {
                throw new IllegalStateException("Undefined mask used.");
            }
        }
        abstractCriterion.setFinalMaskApplied(false);
        return abstractCriterion;
    }

    /**
     * Retrieves the masked value from the original value.
     * @param originalValue to mask.
     * @return masked value.
     */
    protected abstract AbstractAnnotationValue retrieveMaskedValue(AbstractAnnotationValue originalValue);
    
    /**
     * Retrieves the criterion after the masks have been applied.
     * @param originalCriterion to mask.
     * @return the new criterion to use.
     */
    protected abstract AbstractCriterion createMaskedCriterion(AbstractCriterion originalCriterion);
    
    /**
     * Creates a masked numeric annotation value from an abstract value.
     * @param originalValue to convert to masked numeric value.
     * @return masked numeric value.
     */
    protected MaskedNumericAnnotationValue createMaskedNumericValue(AbstractAnnotationValue originalValue) {
        if (originalValue instanceof MaskedNumericAnnotationValue) {
            return (MaskedNumericAnnotationValue) originalValue;
        } else if (originalValue instanceof NumericAnnotationValue) {
            return new MaskedNumericAnnotationValue((NumericAnnotationValue) originalValue);
        } 
        throw new IllegalArgumentException("Datatype incompatible, must be a Numeric typed value.");
    }
}
