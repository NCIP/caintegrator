/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;

import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.common.DateUtil;

import java.util.Date;

/**
 * 
 */
public class DateAnnotationValue extends AbstractAnnotationValue {

    private static final long serialVersionUID = 1L;
    
    private Date dateValue;
    
    /**
     * Default empty constructor.
     */
    public DateAnnotationValue() { 
        // Empty Constructor
    }
    
    /**
     * Converts the given annotation value to a a new object, as well as moves it to the new 
     * annotationDefinition.
     * @param value to use to update this object.
     * @param annotationDefinition is the new definition to move value to.
     */
    public DateAnnotationValue(AbstractAnnotationValue value, AnnotationDefinition annotationDefinition) {
        super(value, annotationDefinition);
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAnnotationValue(AnnotationDefinition annotationDefinition) 
    throws ValidationException {
        switch (annotationDefinition.getDataType()) {
            case DATE:
                if (annotationDefinition.equals(this.getAnnotationDefinition())) {
                    return;
                }
                DateAnnotationValue dateAnnotationValue = new DateAnnotationValue(this, annotationDefinition);
                dateAnnotationValue.setDateValue(dateValue);
                break;
            case STRING:
                StringAnnotationValue stringAnnotationValue = new StringAnnotationValue(this, annotationDefinition);
                stringAnnotationValue.setStringValue(this.toString());
                break;
            case NUMERIC:
                throw new ValidationException("Cannot convert Date value '" + dateValue + "' to a number");
            default:
                throw new IllegalArgumentException("Must input valid annotationType.");
        }
    }
}
