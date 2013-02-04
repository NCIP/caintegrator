/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible entity type values for <code>DateComparisonCriterion.dateComparisonOperator</code>.
 */
public enum DateComparisonOperatorEnum {

    /**
     * Greater Than.
     */
    GREATER(">"),

    /**
     * Greater Than or Equal To.
     */
    GREATEROREQUAL(">="),
    
    /**
     * Equal To.
     */
    EQUAL("=="),
    
    /**
     * Less Than.
     */
    LESS("<"),
    
    /**
     * Less Than or Equal To.
     */
    LESSOREQUAL("<="),
    
    /**
     * Not Equal.
     */
    NOTEQUAL("!=");
    
    private static Map<String, DateComparisonOperatorEnum> valueToTypeMap = 
                                        new HashMap<String, DateComparisonOperatorEnum>();

    private String value;
    
    private DateComparisonOperatorEnum(String value) {
        this.value = value;
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

    private static Map<String, DateComparisonOperatorEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (DateComparisonOperatorEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>DateComparisonOperatorEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static DateComparisonOperatorEnum getByValue(String value) {
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
