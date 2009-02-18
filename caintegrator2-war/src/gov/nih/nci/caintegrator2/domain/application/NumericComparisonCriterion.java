package gov.nih.nci.caintegrator2.domain.application;


/**
 * 
 */
public class NumericComparisonCriterion extends AbstractAnnotationCriterion {

    private static final long serialVersionUID = 1L;
    
    private NumericComparisonOperatorEnum numericComparisonOperator;
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
     * @return the numericComparisonOperator
     */
    public NumericComparisonOperatorEnum getNumericComparisonOperator() {
        return numericComparisonOperator;
    }

    /**
     * @param numericComparisonOperator the numericComparisonOperator to set
     */
    public void setNumericComparisonOperator(NumericComparisonOperatorEnum numericComparisonOperator) {
        this.numericComparisonOperator = numericComparisonOperator;
    }

}