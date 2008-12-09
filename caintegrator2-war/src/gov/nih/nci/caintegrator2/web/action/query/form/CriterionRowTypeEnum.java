package gov.nih.nci.caintegrator2.web.action.query.form;


import java.util.HashMap;
import java.util.Map;

/**
 * Query criteria types.
 */
public enum CriterionRowTypeEnum {

    /**
     * Clinical.
     */
    CLINICAL("Clinical"),

    /**
     * Clinical.
     */
    IMAGE_SERIES("Image Series"),

    /**
     * Gene Expression.
     */
    GENE_EXPRESSION("Gene Expression");
    
    private static Map<String, CriterionRowTypeEnum> valueToTypeMap = new HashMap<String, CriterionRowTypeEnum>();

    private final String value;
    
    private CriterionRowTypeEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private static Map<String, CriterionRowTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (CriterionRowTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>CriterionRowTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static CriterionRowTypeEnum getByValue(String value) {
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
