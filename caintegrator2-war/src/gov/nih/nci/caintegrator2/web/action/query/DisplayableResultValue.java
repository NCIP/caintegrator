/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.application.ResultValue;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * Represents a value associated to a Row / Column.  (Wraps the <code>ResultValue</code> class).
 */
public class DisplayableResultValue {
    private static final Pattern URL_PATTERN = Pattern.compile("(?i)^(https?|ftp|file)://");
    private String displayString = "";
    private Date dateValue = null;
    private boolean dateType = false;
    private boolean numericType = false;
    private boolean urlType = false;

    /**
     * Empty Constructor.
     */
    public DisplayableResultValue() {
        // empty constructor
    }

    /**
     * Constructor which wraps the given ResultValue object.
     * @param resultValue - ResultValue associated with this object.
     */
    public DisplayableResultValue(ResultValue resultValue) {
        if (resultValue != null) {
            displayString = resultValue.toString();
            checkForSpecialValues(resultValue);
        }
    }

    private void checkForSpecialValues(ResultValue resultValue) {
        AnnotationTypeEnum dataType =
            resultValue.getColumn().getAnnotationFieldDescriptor().getDefinition().getDataType();
        if (AnnotationTypeEnum.DATE.equals(dataType)) {
            dateType = true;
            dateValue = getDateValue(resultValue);
        } else if (AnnotationTypeEnum.NUMERIC.equals(dataType)) {
            numericType = true;
        } else if (AnnotationTypeEnum.STRING.equals(dataType)
                && URL_PATTERN.matcher(resultValue.toString()).find()) {
            urlType = true;
        }
    }

    private Date getDateValue(ResultValue resultValue) {
        DateAnnotationValue dateAnnotationValue = (DateAnnotationValue) resultValue.getValue();
        return (dateAnnotationValue != null) ? dateAnnotationValue.getDateValue() : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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

    /**
     * @return the numericType
     */
    public boolean isNumericType() {
        return numericType;
    }

    /**
     * @return the urlType
     */
    public boolean isUrlType() {
        return urlType;
    }


}
