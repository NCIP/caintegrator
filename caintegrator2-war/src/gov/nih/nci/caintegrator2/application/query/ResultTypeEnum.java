package gov.nih.nci.caintegrator2.application.query;


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
