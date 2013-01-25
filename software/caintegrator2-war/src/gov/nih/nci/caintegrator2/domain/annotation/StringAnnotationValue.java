/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.DateUtil;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 *
 */
public class StringAnnotationValue extends AbstractAnnotationValue {

    private static final long serialVersionUID = 1L;

    private String stringValue;

    /**
     * Empty Constructor.
     */
    public StringAnnotationValue() {
        // Empty Constructor
    }

    /**
     * Converts the given annotation value to a new object, as well as moves it to the new
     * annotationDefinition.
     * @param value to use to update this object.
     * @param annotationDefinition is the new definition to move value to.
     */
    public StringAnnotationValue(AbstractAnnotationValue value, AnnotationDefinition annotationDefinition) {
        super(value, annotationDefinition);
    }

    /**
     * @return the stringValue
     */
    public String getStringValue() {
        return stringValue;
    }

    /**
     * @param stringValue the stringValue to set
     */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return stringValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AnnotationTypeEnum getValidAnnotationType() {
        return AnnotationTypeEnum.STRING;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void convertAnnotationValue(AnnotationDefinition annotationDefinition)
        throws ValidationException {
        switch (annotationDefinition.getDataType()) {
        case STRING:
            if (annotationDefinition.equals(this.getAnnotationDefinition())) {
                return;
            }
            StringAnnotationValue value = new StringAnnotationValue(this, annotationDefinition);
            value.setStringValue(stringValue);
            break;
        case NUMERIC:
            handleNumericType(annotationDefinition);
            break;
        case DATE:
            handleDateType(annotationDefinition);
            break;
        default:
            throw new IllegalArgumentException("Must input valid annotationType.");
        }
    }

    private void handleNumericType(AnnotationDefinition annotationDefinition) throws ValidationException {
        NumericAnnotationValue numericValue = new NumericAnnotationValue(this, annotationDefinition);
        if (StringUtils.isBlank(stringValue)) {
            return;
        }
        if (!NumberUtils.isNumber(stringValue)) {
            throw new ValidationException("Cannot convert String value '" + stringValue + "' to a number");
        }
        numericValue.setNumericValue(Double.valueOf(stringValue));
    }

    private void handleDateType(AnnotationDefinition annotationDefinition) throws ValidationException {
        DateAnnotationValue dateValue = new DateAnnotationValue(this, annotationDefinition);
        try {
            dateValue.setDateValue(DateUtil.createDate(stringValue));
        } catch (ParseException e) {
            throw new ValidationException("Cannot convert String value '" + stringValue + "' to a date", e);
        }
    }
}
