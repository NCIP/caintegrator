/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.MaskedNumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator.domain.application.AbstractComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;

/**
 * Base class for handling mask values.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 * @param <T> the desired criterion type
 */
public abstract class AbstractAnnotationMaskHandler<T extends AbstractComparisonCriterion> {

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
    protected abstract AbstractCriterion createMaskedCriterion(T originalCriterion);
}
