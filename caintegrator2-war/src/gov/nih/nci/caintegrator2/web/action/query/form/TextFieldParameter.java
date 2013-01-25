/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Represents a single text operand field.
 */
public class TextFieldParameter extends AbstractCriterionParameter {

    private String value;
    private ValueHandler valueHandler;

    TextFieldParameter(int parameterIndex, int rowIndex, String initialValue) {
        super(parameterIndex, rowIndex);
        this.value = initialValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldType() {
        return TEXT_FIELD;
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
        if (!StringUtils.equals(getValue(), value.trim())) {
            this.value = value.trim();
            if (getValueHandler().isValid(getValue())) {
                getValueHandler().valueChanged(value);
            }
        }
    }

    private ValueHandler getValueHandler() {
        return valueHandler;
    }

    void setValueHandler(ValueHandler valueHandler) {
        this.valueHandler = valueHandler;
    }

    @Override
    void validate(ValidationAware action) {
        getValueHandler().validate(getFormFieldName(), value, action);
    }

}
