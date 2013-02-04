/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import org.apache.commons.lang.StringUtils;

/**
 * A float value.
 */
public class FloatParameterValue extends AbstractParameterValue {

    private static final long serialVersionUID = 1L;
    
    private Float value;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueFromString(String stringValue) {
        if (StringUtils.isBlank(stringValue)) {
            setValue(null);
        } else {
            setValue(Float.parseFloat(stringValue));
        }
    }

    /**
     * @return the value
     */
    public Float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Float value) {
        this.value = value;
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
