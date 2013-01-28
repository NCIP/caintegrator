/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Represents a single text operand field.
 *
 * @param <E> object type returned by the parameter.
 */
public class SelectListParameter<E> extends AbstractCriterionParameter {

    private final OptionList<E> optionList;
    private String selectedKey;
    private final ValueSelectedHandler<E> valueSelectedHandler;
    private boolean updateFormOnChange;

    SelectListParameter(int parameterIndex, int rowIndex, OptionList<E> options,
            ValueSelectedHandler<E> valueSelectedHandler, E initialValue) {
        super(parameterIndex, rowIndex);
        this.optionList = options;
        this.valueSelectedHandler = valueSelectedHandler;
        this.selectedKey = options.getKey(initialValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldType() {
        return SELECT_LIST;
    }

    /**
     * @return the option keys
     */
    public List<Option<E>> getOptions() {
        return optionList.getOptions();
    }

    /**
     * @return the value
     */
    public String getValue() {
        return selectedKey;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        if (!StringUtils.equals(getValue(), value)) {
            this.selectedKey = value;
            valueSelectedHandler.valueSelected(optionList.getActualValue(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void validate(ValidationAware action) {
        // no-op; no validations necessary on select list
    }

    /**
     * @return the updateFormOnChange
     */
    public boolean getUpdateFormOnChange() {
        return updateFormOnChange;
    }

    /**
     * @param updateFormOnChange the updateFormOnChange to set
     */
    public void setUpdateFormOnChange(boolean updateFormOnChange) {
        this.updateFormOnChange = updateFormOnChange;
    }

}
