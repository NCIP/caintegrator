/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.mask.AbstractAnnotationMask;
import gov.nih.nci.caintegrator.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator.domain.annotation.mask.NumericRangeMask;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Utilities for generating the appropriate criterion for a heterogeneous set of masks.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class MaskHandlerUtils {

    /**
     * Default constructor.
     */
    private MaskHandlerUtils() {
        //Intentionally left blank.
    }

    /**
     * Generates the appropriate annotation value from the given set of masks.
     * @param masks the masks
     * @param annoationValue the original annotation value
     * @return the masked annotation value
     */
    public static AbstractAnnotationValue retrieveMaskedValue(Set<AbstractAnnotationMask> masks,
            AbstractAnnotationValue annoationValue) {
        AbstractAnnotationValue maskedValue = annoationValue;
        List<AbstractAnnotationMask> sortedMasks = new ArrayList<AbstractAnnotationMask>(masks);
        Collections.sort(sortedMasks);
        for (AbstractAnnotationMask mask : sortedMasks) {
            if (mask instanceof MaxNumberMask) {
                maskedValue = new MaxNumberMaskHandler((MaxNumberMask) mask).retrieveMaskedValue(maskedValue);
            } else if (mask instanceof NumericRangeMask) {
                maskedValue = new NumericRangeMaskHandler((NumericRangeMask) mask).retrieveMaskedValue(maskedValue);
            }
        }
        return maskedValue;
    }

    /**
     * Generates the appropriate masked criterion from the given set of masks.
     * @param masks the masks
     * @param criterion the original criterion
     * @return the masked criterion
     */
    public static AbstractCriterion createMaskedCriterion(Set<AbstractAnnotationMask> masks,
            AbstractCriterion criterion) {
        List<AbstractAnnotationMask> sortedMasks = new ArrayList<AbstractAnnotationMask>(masks);
        Collections.sort(sortedMasks);
        AbstractCriterion abstractCriterion = criterion;
        for (AbstractAnnotationMask mask : sortedMasks) {
            if (mask instanceof MaxNumberMask) {
                abstractCriterion = new MaxNumberMaskHandler((MaxNumberMask) mask)
                        .createMaskedCriterion((NumericComparisonCriterion) abstractCriterion);
            } else if (mask instanceof NumericRangeMask) {
                abstractCriterion = new NumericRangeMaskHandler((NumericRangeMask) mask)
                .createMaskedCriterion((NumericComparisonCriterion) abstractCriterion);
            }
        }
        abstractCriterion.setFinalMaskApplied(false);
        return abstractCriterion;
    }

}
