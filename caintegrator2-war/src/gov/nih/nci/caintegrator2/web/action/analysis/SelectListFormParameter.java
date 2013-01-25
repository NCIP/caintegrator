/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.analysis.AbstractParameterValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a parameter rendered as a free-form text field in the UI.
 */
public class SelectListFormParameter extends AbstractAnalysisFormParameter {

    private Map<AbstractParameterValue, String> displayValueMap;

    SelectListFormParameter(GenePatternAnalysisForm form, AbstractParameterValue parameterValue) {
        super(form, parameterValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayType() {
        return "select";
    }

    /**
     * @return the available choices
     */
    public Collection<String> getChoices() {
        return getParameter().getChoiceKeys();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return getDisplayValue(getParameterValue());
    }

    private String getDisplayValue(AbstractParameterValue parameterValue) {
        return getDisplayValueMap().get(parameterValue);
    }

    private Map<AbstractParameterValue, String> getDisplayValueMap() {
        if (displayValueMap == null) {
            displayValueMap = new HashMap<AbstractParameterValue, String>();
            for (String key : getParameter().getChoiceKeys()) {
                displayValueMap.put(getParameter().getChoice(key), key);
            }
        }
        return displayValueMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(String value) {
        AbstractParameterValue parameterValue = getParameter().getChoice(value);
        setParameterValue(parameterValue);
        getForm().getInvocation().setParameterValue(getParameter(), parameterValue);
    }
}
