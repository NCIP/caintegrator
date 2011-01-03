package gov.nih.nci.caintegrator2.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible job status values for <code>GenePatternAnalysisJob.status</code>.
 */
public enum AnalysisJobStatusEnum {

    /**
     * Not yet submitted, still in creation state.
     */
    NOT_SUBMITTED("Not Submitted", true, false),
    
    /**
     * Submitted and validated, ready to be run.
     */
    SUBMITTED("Submitted", false, false),

    /**
     * Processing Locally.
     */
    PROCESSING_LOCALLY("Processing Locally", false, false),
    
    /**
     * Processing Remotely.
     */
    PROCESSING_REMOTELY("Processing Remotely", false, false),
    
    /**
     * Error Connecting.
     */
    ERROR_CONNECTING("Error Connecting", true, true),
    
    /**
     * Error Connecting.
     */
    INVALID_PARAMETER("Invalid Parameter", true, true),
    
    /**
     * Local Error.
     */
    LOCAL_ERROR("Local Error", true, true),
    
    /**
     * Completed.
     */
    COMPLETED("Completed", true, false);
    
    private static Map<String, AnalysisJobStatusEnum> valueToTypeMap = 
                    new HashMap<String, AnalysisJobStatusEnum>();

    private String value;
    private boolean deletable;
    private boolean errorState;
    
    private AnalysisJobStatusEnum(String value, boolean deletable, boolean errorState) {
        this.value = value;
        this.deletable = deletable;
        this.errorState = errorState;
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

    /**
     * @return the deletable
     */
    public boolean isDeletable() {
        return deletable;
    }
    
    /**
     * 
     * @return if the status is in an error state.
     */
    public boolean isErrorState() {
        return errorState;
    }


    private static Map<String, AnalysisJobStatusEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (AnalysisJobStatusEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>JobStatusEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static AnalysisJobStatusEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>AssayType</code> value.
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
