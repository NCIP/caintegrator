package gov.nih.nci.caintegrator2.domain.application;

import java.util.Date;

/**
 * 
 */
public class DateComparisonCriterion extends AbstractAnnotationCriterion {

    private static final long serialVersionUID = 1L;
    
    private DateComparisonOperatorEnum dateComparisonOperator;
    private Date dateValue;
    
    /**
     * @return the DateComparisonOperator
     */
    public DateComparisonOperatorEnum getDateComparisonOperator() {
        return dateComparisonOperator;
    }
    
    /**
     * @param dateComparisonOperator the DateComparisonOperator to set
     */
    public void setDateComparisonOperator(DateComparisonOperatorEnum dateComparisonOperator) {
        this.dateComparisonOperator = dateComparisonOperator;
    }
    
    /**
     * @return the DateValue
     */
    public Date getDateValue() {
        return dateValue;
    }
    
    /**
     * @param dateValue the DateValue to set
     */
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

}