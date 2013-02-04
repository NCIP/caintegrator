/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A parameter belonging to an analysis method.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // see createValue()
public class AnalysisParameter implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private String description;
    private boolean required;
    private AnalysisParameterType type;
    private AbstractParameterValue defaultValue;
    private final List<String> choiceKeys = new ArrayList<String>();
    private final Map<String, AbstractParameterValue> choices = new HashMap<String, AbstractParameterValue>();
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }
    
    /**
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    /**
     * @return the type
     */
    public AnalysisParameterType getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(AnalysisParameterType type) {
        this.type = type;
    }
    
    /**
     * @return the defaultValue
     */
    public AbstractParameterValue getDefaultValue() {
        return defaultValue;
    }
    
    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(AbstractParameterValue defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a value instance that corresponds to the parameter.
     * 
     * @return the value.
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // instantiation by type requires these cases
    public AbstractParameterValue createValue() {
        AbstractParameterValue value;
        switch (getType()) {
        case INTEGER:
            value = new IntegerParameterValue();
            break;
        case STRING:
            value = new StringParameterValue();
            break;
        case FLOAT:
            value = new FloatParameterValue();
            break;
        case GENOMIC_DATA:
            value = new GenomicDataParameterValue();
            break;
        case SAMPLE_CLASSIFICATION:
            value = new SampleClassificationParameterValue();
            break;
        default:
            throw new IllegalStateException("Unsupported parameter type: " + getType());
        }
        value.setParameter(this);
        return value;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the choice matching the key given.
     * 
     * @param key the choice key
     * @return the matching choice
     */
    public AbstractParameterValue getChoice(String key) {
        return choices.get(key);
    }
    
    /**
     * Adds a new choice to the choice list for this parameter.
     * 
     * @param key the key for the choice
     * @param stringValue the value as a String
     */
    public void addChoice(String key, String stringValue) {
        choiceKeys.add(key);
        AbstractParameterValue value = createValue();
        value.setValueFromString(stringValue);
        choices.put(key, value);
    }

    AbstractParameterValue getDefaultValueCopy() {
        if (choiceKeys.isEmpty()) {
            return (AbstractParameterValue) getDefaultValue().clone();
        } else {
            return getDefaultValue();
        }
    }

    /**
     * @return the choiceKeys
     */
    public List<String> getChoiceKeys() {
        return choiceKeys;
    }

}
