package gov.nih.nci.caintegrator2.application.study;


import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Annotation of allowed values for <code>AnnotationDefintion.type</code>.
 */
@SuppressWarnings("unchecked") // For the "Class" operations.
public enum AnnotationTypeEnum  {

    /**
     * Date type.
     */
    DATE("date", Date.class),

    /**
     * Numeric type.
     */
    NUMERIC("numeric", Double.class),
    
    /**
     * String type.
     */
    STRING("string", String.class);
    
    private static Map<String, AnnotationTypeEnum> valueToTypeMap = new HashMap<String, AnnotationTypeEnum>();

    private String value;
    private Class classType;
    
    private AnnotationTypeEnum(String value, Class classType) {
        this.value = value;
        this.classType = classType;
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
                valueToTypeMap.put(type.getValue().toLowerCase(Locale.getDefault()), type);
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
        return getValueToTypeMap().get(value.toLowerCase(Locale.getDefault()));
    }

    /**
     * Checks to see that the value given is a legal <code>AssayType</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value == null || !getValueToTypeMap().containsKey(value.toLowerCase(Locale.getDefault()))) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }

    /**
     * @return the classType
     */
    public Class getClassType() {
        return classType;
    }

    /**
     * @param classType the classType to set
     */
    public void setClassType(Class classType) {
        this.classType = classType;
    }
}
