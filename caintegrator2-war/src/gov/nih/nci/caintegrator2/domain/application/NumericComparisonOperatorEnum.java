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
 * Possible entity type values for <code>NumericComparisonCriterion.numericComparisonOperator</code>.
 */
public enum NumericComparisonOperatorEnum {

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
    
    private static Map<String, NumericComparisonOperatorEnum> valueToTypeMap = 
                                        new HashMap<String, NumericComparisonOperatorEnum>();

    private String value;
    
    private NumericComparisonOperatorEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the value
     */
    public String valueOf() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    private static Map<String, NumericComparisonOperatorEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (NumericComparisonOperatorEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>NumericComparisonOperatorEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static NumericComparisonOperatorEnum getByValue(String value) {
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
