/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.cabio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enum of different types of Genomic Annotations.
 */
public enum KeywordSearchPreferenceEnum {
    /**
     * Any keywords.
     */
    ANY("Any", "OR"),

    /**
     * All keywords.
     */
    ALL("All", "AND");
    
    private static Map<String, KeywordSearchPreferenceEnum> valueToTypeMap = 
        new HashMap<String, KeywordSearchPreferenceEnum>();

    private String value;
    private String logicalOperator;

    private KeywordSearchPreferenceEnum(String value, String logicalOperator) {
        this.value = value;
        this.logicalOperator = logicalOperator;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * @return the logicalOperator
     */
    public String getLogicalOperator() {
        return logicalOperator;
    }

    private static Map<String, KeywordSearchPreferenceEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (KeywordSearchPreferenceEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * @return List of Displayable Strings for this enum.
     */
    public static List<String> getDisplayableValues() {
        List<String> list = new ArrayList<String>();
        list.add(KeywordSearchPreferenceEnum.ANY.getValue());
        list.add(KeywordSearchPreferenceEnum.ALL.getValue());
        return list;
    }
    
    /**
     * Returns the <code>GenomicAnnotationEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static KeywordSearchPreferenceEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>AssayType</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }    
}
