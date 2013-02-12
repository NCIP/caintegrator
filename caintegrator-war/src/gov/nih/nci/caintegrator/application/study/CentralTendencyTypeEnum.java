/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Possible central tendencies for genomic data.
 */
public enum CentralTendencyTypeEnum {

    /**
     * Mean.
     */
    MEAN("Mean"),
    
    /**
     * Median.
     */
    MEDIAN("Median");
    
    private String value;
    
    private CentralTendencyTypeEnum(String value) {
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
    
    private static Map<String, CentralTendencyTypeEnum> valueToTypeMap = 
        new HashMap<String, CentralTendencyTypeEnum>();

    private static Map<String, CentralTendencyTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (CentralTendencyTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used to retrieve all string values (by the JSP for display purposes).
     * @return List of all string values which represent the ENUM values.
     */
    public static List<String> getStringValues() {
        List<String> values = new ArrayList<String>();
        for (CentralTendencyTypeEnum type : values()) {
            values.add(type.getValue());
        }
        return values;
    }
    
    /**
     * Returns the <code>CentralTendencyTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static CentralTendencyTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>CentralTendencyTypeEnum</code> value.
     * 
     * @param value the value to check;
     * @return T/F value depending on if is a valid type.
     */
    public static boolean checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            return false;
        }
        return true;
    }
}