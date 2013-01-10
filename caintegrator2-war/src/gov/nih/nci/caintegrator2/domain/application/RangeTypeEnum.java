/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import java.util.HashMap;
import java.util.Map;

/**
 * Indicates whether to search for up or down regulated genes in fold change query.
 */
public enum RangeTypeEnum {

    /**
     * Greater than or equal to.
     */
    GREATER_OR_EQUAL(">="),

    /**
     * Less than or equal to.
     */
    LESS_OR_EQUAL("<="),

    /**
     * Inside range of two numbers.
     */
    INSIDE_RANGE("Inside Range"),
    
    /**
     * Outside range of two numbers.
     */
    OUTSIDE_RANGE("Outside Range");

    private static Map<String, RangeTypeEnum> valueToTypeMap = new HashMap<String, RangeTypeEnum>();

    private String value;
    
    private RangeTypeEnum(String value) {
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

    private static Map<String, RangeTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (RangeTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }

    /**
     * Returns the <code>RegulationTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static RangeTypeEnum getByValue(String value) {
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
