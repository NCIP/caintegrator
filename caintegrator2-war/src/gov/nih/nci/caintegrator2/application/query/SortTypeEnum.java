package gov.nih.nci.caintegrator2.application.query;


import java.util.HashMap;
import java.util.Map;

/**
 * Sorting type for <code>ResultColumn.sortType</code>.
 */
public enum SortTypeEnum {

    /**
     * Ascending.
     */
    ASCENDING("ascending"),

    /**
     * Descending.
     */
    DESCENDING("descending");
    
    private static Map<String, SortTypeEnum> valueToTypeMap = new HashMap<String, SortTypeEnum>();

    private String value;
    
    private SortTypeEnum(String value) {
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

    private static Map<String, SortTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (SortTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>SortTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static SortTypeEnum getByValue(String value) {
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
