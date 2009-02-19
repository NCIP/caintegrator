package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.common.DateUtil;

/**
 * 
 */
public class DatePermissibleValue extends AbstractPermissibleValue {

    private static final long serialVersionUID = 1L;
    
    private java.util.Date dateValue;

    /**
     * @return the dateValue
     */
    public java.util.Date getDateValue() {
        return dateValue;
    }

    /**
     * @param dateValue the dateValue to set
     */
    public void setDateValue(java.util.Date dateValue) {
        this.dateValue = dateValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return DateUtil.toString(dateValue);
    }

}