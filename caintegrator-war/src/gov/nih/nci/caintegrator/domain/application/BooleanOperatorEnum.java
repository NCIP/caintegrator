/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible boolean operators <code>CompoundCriterion.entityType</code>.
 */
public enum BooleanOperatorEnum {

    /**
     * Boolean and.
     */
    AND("and"),

    /**
     * Boolean or.
     */
    OR("or");
    
    private static Map<String, BooleanOperatorEnum> valueToTypeMap = new HashMap<String, BooleanOperatorEnum>();

    private String value;
    
    private BooleanOperatorEnum(String value) {
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

    private static Map<String, BooleanOperatorEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (BooleanOperatorEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>BooleanOperatorEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static BooleanOperatorEnum getByValue(String value) {
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
