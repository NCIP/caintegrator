package gov.nih.nci.caintegrator2.application.arraydata;


import java.util.HashMap;
import java.util.Map;

/**
 * Annotation of allowed values for <code>ReporterSet.type</code>.
 */
public enum ReporterTypeEnum {

    /**
     * Reporter for a transcript that is not the full gene sequence.
     */
    GENE_EXPRESSION_PROBE_SET("geneExpressionProbeSet"),

    /**
     * Gene-level reporter.
     */
    GENE_EXPRESSION_GENE("geneExpressionGeneLevel");
    
    private static Map<String, ReporterTypeEnum> valueToTypeMap = new HashMap<String, ReporterTypeEnum>();

    private String value;
    
    private ReporterTypeEnum(String value) {
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

    private static Map<String, ReporterTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ReporterTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>ReporterTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ReporterTypeEnum getByValue(String value) {
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
