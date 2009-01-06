package gov.nih.nci.caintegrator2.domain.application;

/**
 * 
 */
public class NumericComparisonCriterion extends AbstractAnnotationCriterion {

    private static final long serialVersionUID = 1L;
    
    private String numericComparisonOperator;
    private Double numericValue;
    
    /**
     * @return the numericComparisonOperator
     */
    public String getNumericComparisonOperator() {
        return numericComparisonOperator;
    }
    
    /**
     * @param numericComparisonOperator the numericComparisonOperator to set
     */
    public void setNumericComparisonOperator(String numericComparisonOperator) {
        this.numericComparisonOperator = numericComparisonOperator;
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

}