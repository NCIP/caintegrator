/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to hold and manage options in Struts 2 select lists and check box lists.
 *
 * @param <E> type of objects maintained in the list.
 */
class OptionList<E> {
    
    private final List<Option<E>> options = new ArrayList<Option<E>>();
    private final Map<String, Option<E>> keyToOptionMap = new HashMap<String, Option<E>>();
    private final Map<E, Option<E>> actualValueToOptionMap = new HashMap<E, Option<E>>();
    
    void addOption(String displayedValue, E actualValue) {
        addOption(displayedValue, displayedValue, actualValue);
    }
    
    void addOption(String key, String displayedValue, E actualValue) {
        Option<E> option = new Option<E>(key, displayedValue, actualValue);
        options.add(option);
        keyToOptionMap.put(key, option);
        actualValueToOptionMap.put(actualValue, option);
    }
    
    String getKey(E actualValue) {
        if (actualValueToOptionMap.containsKey(actualValue)) {
            return actualValueToOptionMap.get(actualValue).getKey();
        } else {
            return null;
        }
    }

    List<E> getActualValues(String[] selectedKeys) {
        List<E> actualValues = new ArrayList<E>(selectedKeys.length);
        for (String key : selectedKeys) {
            if (keyToOptionMap.containsKey(key)) {
                actualValues.add(keyToOptionMap.get(key).getActualValue());
            }
        }
        return actualValues;
    }
    
    List<E> getAllActualValues() {
        List<E> actualValues = new ArrayList<E>();
        for (String key : keyToOptionMap.keySet()) {
            actualValues.add(keyToOptionMap.get(key).getActualValue());
        }
        return actualValues;
    }
    
    String[] getKeys(Collection<E> actualValues) {
        List<String> selectedKeys = new ArrayList<String>();
        for (E actualValue : actualValues) {
            selectedKeys.add(getKey(actualValue));
        }
        return selectedKeys.toArray(new String[selectedKeys.size()]);
    }

    E getActualValue(String key) {
       return keyToOptionMap.get(key) == null ? null : keyToOptionMap.get(key).getActualValue();
    }

    List<Option<E>> getOptions() {
        return options;
    }
    
}
