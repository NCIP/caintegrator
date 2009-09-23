package gov.nih.nci.caintegrator2.domain.annotation;

import java.text.DecimalFormat;

/**
 * 
 */
public class NumericPermissibleValue extends AbstractPermissibleValue {

    private static final long serialVersionUID = 1L;
    
    private Double highValue;
    private Integer isRangeValue;
    private Double lowValue;
    private Double numericValue;
    
    /**
     * @return the highValue
     */
    public Double getHighValue() {
        return highValue;
    }
    
    /**
     * @param highValue the highValue to set
     */
    public void setHighValue(Double highValue) {
        this.highValue = highValue;
    }
    
    /**
     * @return the isRangeValue
     */
    public Integer getIsRangeValue() {
        return isRangeValue;
    }
    
    /**
     * @param isRangeValue the isRangeValue to set
     */
    public void setIsRangeValue(Integer isRangeValue) {
        this.isRangeValue = isRangeValue;
    }
    
    /**
     * @return the lowValue
     */
    public Double getLowValue() {
        return lowValue;
    }
    
    /**
     * @param lowValue the lowValue to set
     */
    public void setLowValue(Double lowValue) {
        this.lowValue = lowValue;
    }

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
        return numericValue != null ? new DecimalFormat(NumericAnnotationValue.DECIMAL_FORMAT).format(numericValue) 
                : "";
    }

}