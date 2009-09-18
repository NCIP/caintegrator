package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.common.DateUtil;

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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return dateValue != null ? DateUtil.toString(dateValue) : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationTypeEnum getValidAnnotationType() {
        return AnnotationTypeEnum.DATE;
    }

}