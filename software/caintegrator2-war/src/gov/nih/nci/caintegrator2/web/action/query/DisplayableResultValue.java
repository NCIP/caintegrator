/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;

import java.util.Date;

/**
 * Represents a value associated to a Row / Column.  (Wraps the <code>ResultValue</code> class).
 */
public class DisplayableResultValue {
    
    private String displayString = "";
    private Date dateValue = null;
    private boolean dateType = false;

    /**
     * Empty Constructor.
     */
    @SuppressWarnings("PMD.UncommentedEmptyConstructor")
    public DisplayableResultValue() { }

    /**
     * Constructor which wraps the given string.
     * @param value - A string value.
     */
    public DisplayableResultValue(String value) {
        displayString = value;
    }
    
    /**
     * Constructor which wraps the given ResultValue object.
     * @param resultValue - ResultValue associated with this object.
     */
    public DisplayableResultValue(ResultValue resultValue) {
        if (resultValue != null) {
            if (AnnotationTypeEnum.DATE.equals(
                resultValue.getColumn().getAnnotationDefinition().getDataType())) {
                dateType = true;
                dateValue = getDateValue(resultValue);
            }
            displayString = resultValue.toString();
        }
    }
    
    private Date getDateValue(ResultValue resultValue) {
        DateAnnotationValue dateAnnotationValue = (DateAnnotationValue) resultValue.getValue();
        return (dateAnnotationValue != null) ? dateAnnotationValue.getDateValue() : null;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
        return displayString;
    }
    
    /**
     * Gets the date value.
     * @return - date value.
     */
    public Date getDateValue() {
        return dateValue;
    }
    
    /**
     * Determines if this is a date type.
     * @return - T/F value.
     */
    public boolean isDateType() {
        return dateType;
    }

}
