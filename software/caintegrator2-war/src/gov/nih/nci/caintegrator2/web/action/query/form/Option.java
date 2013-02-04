/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

/**
 * A single option in a <code>OptionList</code>.
 * 
 * @param <E> the type of object held in the option.
 */
public class Option<E> {
    
    private final String key;
    private final String displayValue;
    private final E actualValue;

    Option(String key, String displayValue, E actualValue) {
        this.key = key;
        this.displayValue = displayValue;
        this.actualValue = actualValue;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the displayValue
     */
    public String getDisplayValue() {
        return displayValue;
    }

    E getActualValue() {
        return actualValue;
    }

}
