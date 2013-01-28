/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible survival type values for <code>SurvivalValueDefinition.survivalValueType</code>.
 */
public enum SurvivalValueTypeEnum {

    /**
     * By Date.
     */
    DATE("By Date"),

    /**
     * By Length of time in study.
     */
    LENGTH_OF_TIME("By Length of time in study");
    
    private static Map<String, SurvivalValueTypeEnum> valueToTypeMap = new HashMap<String, SurvivalValueTypeEnum>();

    private String value;
    
    private SurvivalValueTypeEnum(String value) {
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

    private static Map<String, SurvivalValueTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (SurvivalValueTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>SurvivalValueTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static SurvivalValueTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>SurvivalValueTypeEnum</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }
}
