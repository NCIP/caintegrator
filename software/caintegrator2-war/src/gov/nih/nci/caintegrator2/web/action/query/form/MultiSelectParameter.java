/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Represents a single text operand field.
 * 
 * @param <E> object type returned by the parameter.
 */
@SuppressWarnings({ "PMD.ArrayIsStoredDirectly", "PMD.MethodReturnsInternalArray" })    // array handling necessary
public class MultiSelectParameter<E> extends AbstractCriterionParameter {
    
    private final OptionList<E> optionList;
    private String[] selectedKeys;
    private final ValuesSelectedHandler<E> valuesSelectedHandler;

    MultiSelectParameter(int parameterIndex, int rowIndex, OptionList<E> options, 
            ValuesSelectedHandler<E> valuesSelectedHandler, Collection<E> initialSelections) {
        super(parameterIndex, rowIndex);
        this.optionList = options;
        this.valuesSelectedHandler = valuesSelectedHandler;
        this.selectedKeys = options.getKeys(initialSelections);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldType() {
        return MULTI_SELECT;
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
    public String[] getValues() {
        return selectedKeys;
    }

    /**
     * @param values the values to set
     */
    public void setValues(String[] values) {
        if (!Arrays.equals(selectedKeys, values)) {
            List<E> actualValues = optionList.getActualValues(values);
            valuesSelectedHandler.valuesSelected(actualValues);
            this.selectedKeys = optionList.getKeys(actualValues);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void validate(ValidationAware action) {
        // no-op; no validations necessary on multi-select
    }

}
