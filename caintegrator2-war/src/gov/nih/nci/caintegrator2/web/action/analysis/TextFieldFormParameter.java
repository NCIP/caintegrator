/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import com.opensymphony.xwork2.ValidationAware;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;

/**
 * Represents a parameter rendered as a free-form text field in the UI.
 */
public class TextFieldFormParameter extends AbstractAnalysisFormParameter {

    TextFieldFormParameter(GenePatternAnalysisForm form, AbstractParameterValue parameterValue) {
        super(form, parameterValue);
        stringValue = parameterValue.getValueAsString();
    }

    private String stringValue;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayType() {
        return "textfield";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return stringValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String value) {
        stringValue = value;
        getParameterValue().setValueFromString(value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void validateValue(String fieldName, ValidationAware action) {
        switch (getParameter().getType()) {
        case FLOAT:
            validateFloat(fieldName, action);
            break;
        case INTEGER:
            validateInteger(fieldName, action);
            break;
        default:
            // no-op
        }
    }

    private void validateFloat(String fieldName, ValidationAware action) {
        try {
            Float.parseFloat(stringValue);
        } catch (NumberFormatException e) {
            action.addFieldError(fieldName, getParameter().getName() + " requires a floating point numeric value");
        }
    }

    private void validateInteger(String fieldName, ValidationAware action) {
        try {
            Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            action.addFieldError(fieldName, getParameter().getName() + " requires an integer value");
        }
    }

}
