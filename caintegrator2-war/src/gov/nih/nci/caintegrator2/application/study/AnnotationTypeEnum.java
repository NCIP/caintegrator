package gov.nih.nci.caintegrator2.application.study;


import java.util.HashMap;
import java.util.Map;

/**
 * Annotation of allowed values for <code>AnnotationDefintion.type</code>.
 */
public enum AnnotationTypeEnum {

    /**
     * Date type.
     */
    DATE("date"),

    /**
     * Numeric type.
     */
    NUMERIC("numeric"),
    
    /**
     * String type.
     */
    STRING("string");
    
    private static Map<String, AnnotationTypeEnum> valueToTypeMap = new HashMap<String, AnnotationTypeEnum>();

    private String value;
    
    private AnnotationTypeEnum(String value) {
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

    private static Map<String, AnnotationTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (AnnotationTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>AnnotationTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static AnnotationTypeEnum getByValue(String value) {
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
