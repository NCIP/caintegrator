/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;

/**
 * 
 */
public class MaskedNumericAnnotationValue extends NumericAnnotationValue {

    private static final long serialVersionUID = 1L;

    private String maskedValue;
    private boolean finalMaskApplied = false;
    
    /**
     * Constructor based on a given numeric value.
     * @param numericValue value.
     */
    public MaskedNumericAnnotationValue(NumericAnnotationValue numericValue) {
        setAnnotationDefinition(numericValue.getAnnotationDefinition());
        setImage(numericValue.getImage());
        setImageSeries(numericValue.getImageSeries());
        setNumericValue(numericValue.getNumericValue());
        setSampleAcquisition(numericValue.getSampleAcquisition());
        setSubjectAnnotation(numericValue.getSubjectAnnotation());
        maskedValue = numericValue.getNumericValue() == null ? "" : numericValue.toString();
    }
    
    /**
     * @return the maskedValue
     */
    public String getMaskedValue() {
        return maskedValue;
    }

    /**
     * @param maskedValue the maskedValue to set
     */
    public void setMaskedValue(String maskedValue) {
        this.maskedValue = maskedValue;
    }

    /**
     * @return the finalMaskApplied
     */
    public boolean isFinalMaskApplied() {
        return finalMaskApplied;
    }

    /**
     * @param finalMaskApplied the finalMaskApplied to set
     */
    public void setFinalMaskApplied(boolean finalMaskApplied) {
        this.finalMaskApplied = finalMaskApplied;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return maskedValue;
    }

}
