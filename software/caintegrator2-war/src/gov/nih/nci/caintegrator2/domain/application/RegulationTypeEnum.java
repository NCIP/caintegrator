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
 * Indicates whether to search for up or down regulated genes in fold change query.
 */
public enum RegulationTypeEnum {

    /**
     * Up regulated.
     */
    UP("Up"),

    /**
     * Down regulated.
     */
    DOWN("Down"),

    /**
     * Up or down regulated.
     */
    UP_OR_DOWN("Up or Down"),
    
    /**
     * Up or down regulated.
     */
    UNCHANGED("Unchanged");

    private static Map<String, RegulationTypeEnum> valueToTypeMap = new HashMap<String, RegulationTypeEnum>();

    private String value;
    
    private RegulationTypeEnum(String value) {
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

    private static Map<String, RegulationTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (RegulationTypeEnum type : values()) {
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
    public static RegulationTypeEnum getByValue(String value) {
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
