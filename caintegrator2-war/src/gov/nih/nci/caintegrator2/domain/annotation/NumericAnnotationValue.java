package gov.nih.nci.caintegrator2.domain.annotation;

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

}