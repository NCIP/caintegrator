package gov.nih.nci.caintegrator2.application.study;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible entity type values for <code>AbstractAnnotationCriterion.entityType</code>.
 */
public enum EntityTypeEnum {

    /**
     * Subject type.
     */
    SUBJECT("subject"),

    /**
     * Sample type.
     */
    SAMPLE("sample"),
    
    /**
     * Image Series type.
     */
    IMAGESERIES("imageSeries"),
    
    /**
     * Image type.
     */
    IMAGE("image");
    
    private static Map<String, EntityTypeEnum> valueToTypeMap = new HashMap<String, EntityTypeEnum>();

    private String value;
    
    private EntityTypeEnum(String value) {
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

    private static Map<String, EntityTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (EntityTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>EntityTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static EntityTypeEnum getByValue(String value) {
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
