package gov.nih.nci.caintegrator2.domain.genomic;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Allowed values for <code>GisticGenomicRegionReporter.geneAmplificationType</code>.
 */
public enum AmplificationTypeEnum  {

    /**
     * Amplified type.
     */
    AMPLIFIED("Amplified"),

    /**
     * Deleted type.
     */
    DELETED("Deleted");
    
    private static Map<String, AmplificationTypeEnum> valueToTypeMap = new HashMap<String, AmplificationTypeEnum>();

    private String value;
    
    private AmplificationTypeEnum(String value) {
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

    private static Map<String, AmplificationTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (AmplificationTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue().toLowerCase(Locale.getDefault()), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>AmplificationTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static AmplificationTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value.toLowerCase(Locale.getDefault()));
    }

    /**
     * Checks to see that the value given is a legal <code>AmplificationTypeEnum</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value == null || !getValueToTypeMap().containsKey(value.toLowerCase(Locale.getDefault()))) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }
}
