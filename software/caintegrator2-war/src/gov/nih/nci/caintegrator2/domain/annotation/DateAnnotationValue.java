package gov.nih.nci.caintegrator2.domain.annotation;

import java.util.Date;

/**
 * 
 */
public class DateAnnotationValue extends AbstractAnnotationValue {

    private static final long serialVersionUID = 1L;
    
    private Date dateValue;

    /**
     * @return the dateValue
     */
    public Date getDateValue() {
        return dateValue;
    }

    /**
     * @param dateValue the dateValue to set
     */
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

}