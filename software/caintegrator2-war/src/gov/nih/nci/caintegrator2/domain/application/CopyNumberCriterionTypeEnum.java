package gov.nih.nci.caintegrator2.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Copy number critrion type values for <code>CopyNumberAlterationCriterion.copyNumberCriterionType</code>.
 */
public enum CopyNumberCriterionTypeEnum {

    /**
     * Gene Name.
     */
    SEGMENT_VALUE("Segment Value"),
    
    /**
     * Chromosome Coordinates.
     */
    CALLS_VALUE("Calls Value");
    
    private static Map<String, CopyNumberCriterionTypeEnum> valueToTypeMap = 
        new HashMap<String, CopyNumberCriterionTypeEnum>();
    
    private String value;
    
    
    private CopyNumberCriterionTypeEnum(String value) {
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


    private static Map<String, CopyNumberCriterionTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (CopyNumberCriterionTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GenomicIntervalTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static CopyNumberCriterionTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>GenomicIntervalTypeEnum</code> value.
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
