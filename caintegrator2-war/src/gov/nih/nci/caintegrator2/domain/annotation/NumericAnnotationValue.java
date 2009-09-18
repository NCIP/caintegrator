package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;

/**
 * 
 */
public class NumericAnnotationValue extends AbstractAnnotationValue {

    private static final long serialVersionUID = 1L;
    
    private Double numericValue;

    /**
     * @return the numericValue
     */
    public Double getNumericValue() {
        return numericValue;
    }

    /**
     * @param numericValue the numericValue to set
     */
    public void setNumericValue(Double numericValue) {
        this.numericValue = numericValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return numericValue != null ? numericValue.toString() : "";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationTypeEnum getValidAnnotationType() {
        return AnnotationTypeEnum.NUMERIC;
    }

}