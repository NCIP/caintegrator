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
 * Possible server connection types.
 */
public enum ServerConnectionTypeEnum {

    /**
     * Subject type.
     */
    GRID("Grid"),

    /**
     * Sample type.
     */
    WEB("Web"),
    
    /**
     * Image Series type.
     */
    UNKNOWN("Server Type Unknown");
    
    
    private static Map<String, ServerConnectionTypeEnum> valueToTypeMap 
                                                        = new HashMap<String, ServerConnectionTypeEnum>();

    private String value;
    
    private ServerConnectionTypeEnum(String value) {
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

    private static Map<String, ServerConnectionTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ServerConnectionTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>ServerConnectionTypeEnum</code> corresponding to the given value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ServerConnectionTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>ServerConnectionTypeEnum</code> value.
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
