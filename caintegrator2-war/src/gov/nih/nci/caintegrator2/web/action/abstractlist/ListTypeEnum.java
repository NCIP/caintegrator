/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.abstractlist;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible job type values for <code>AbstractPersistedAnalysisJob.jobType</code>.
 */
public enum ListTypeEnum {

    /**
     * Gene.
     */
    GENE("Gene List"),
    
    /**
     * Subject.
     */
    SUBJECT("Subject List");
    
    private static Map<String, ListTypeEnum> valueToTypeMap = 
                    new HashMap<String, ListTypeEnum>();

    private String value;
    
    private ListTypeEnum(String value) {
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

    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * @return HashMap of EnumeratedValue's String to Displayable String.
     */
    public static Map<String, ListTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ListTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>AnalysisJobTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ListTypeEnum getByValue(String value) {
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

    /**
     * @param valueToTypeMap the valueToTypeMap to set
     */
    public static void setValueToTypeMap(Map<String, ListTypeEnum> valueToTypeMap) {
        ListTypeEnum.valueToTypeMap = valueToTypeMap;
    }
}
