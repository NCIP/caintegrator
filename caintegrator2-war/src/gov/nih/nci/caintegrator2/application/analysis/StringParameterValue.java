/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

/**
 * A String value.
 */
public class StringParameterValue extends AbstractParameterValue {
    
    private static final long serialVersionUID = 1L;
    
    private String value;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueFromString(String stringValue) {
        setValue(stringValue);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValueAsString() {
        return getValue();
    }

}
