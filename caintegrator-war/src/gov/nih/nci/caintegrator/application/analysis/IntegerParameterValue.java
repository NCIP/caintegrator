/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import org.apache.commons.lang3.StringUtils;

/**
 * An integer value.
 */
public class IntegerParameterValue extends AbstractParameterValue {

    private static final long serialVersionUID = 1L;

    private Integer value;

    /**
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueFromString(String stringValue) {
        if (StringUtils.isBlank(stringValue)) {
            setValue(null);
        } else {
            setValue(Integer.parseInt(stringValue));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueAsString() {
        if (getValue() == null) {
            return "";
        } else {
            return getValue().toString();
        }
    }

}
