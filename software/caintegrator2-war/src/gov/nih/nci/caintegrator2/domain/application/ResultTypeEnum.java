/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import java.util.HashMap;
import java.util.Map;

/**
 * Query result types.
 */
public enum ResultTypeEnum {

    /**
     * Clinical.
     */
    CLINICAL("clinical"),

    /**
     * Genomic data.
     */
    GENOMIC("genomic");
    
    private static Map<String, ResultTypeEnum> valueToTypeMap = new HashMap<String, ResultTypeEnum>();

    private String value;
    
    private ResultTypeEnum(String value) {
        setValue(value);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    private static Map<String, ResultTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ResultTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator2.application.query.ResultTypeEnum@getValueToDisplayableMap()" 
     *              listKey="key" listValue="value"
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static Map<String, String> getValueToDisplayableMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ResultTypeEnum.CLINICAL.getValue(), "Clinical");
        map.put(ResultTypeEnum.GENOMIC.getValue(), "Genomic");
        return map;
    }
    
    /**
     * Returns the <code>ResultTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ResultTypeEnum getByValue(String value) {
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
