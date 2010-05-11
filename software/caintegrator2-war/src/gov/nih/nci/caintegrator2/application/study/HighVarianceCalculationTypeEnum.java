package gov.nih.nci.caintegrator2.application.study;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * High variance calculation types for replicates in genomic data.  A percentage means the % threshold that a stdDev 
 * can be away from the central tendency value.  A value, is simply the threshold of the stdDev as a number.
 */
public enum HighVarianceCalculationTypeEnum {

    /**
     * Percentage.
     */
    PERCENTAGE("Percentage"),
    
    /**
     * Value.
     */
    VALUE("Value");
    
    private String value;
    
    private HighVarianceCalculationTypeEnum(String value) {
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
    
    private static Map<String, HighVarianceCalculationTypeEnum> valueToTypeMap = 
        new HashMap<String, HighVarianceCalculationTypeEnum>();

    private static Map<String, HighVarianceCalculationTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (HighVarianceCalculationTypeEnum type : values()) {
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
        for (HighVarianceCalculationTypeEnum type : values()) {
            values.add(type.getValue());
        }
        return values;
    }
    
    /**
     * Returns the <code>HighVarianceCalculationTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static HighVarianceCalculationTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>HighVarianceCalculationTypeEnum</code> value.
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
