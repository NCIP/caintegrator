/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible job type values for <code>AbstractPersistedAnalysisJob.jobType</code>.
 */
public enum AnalysisJobTypeEnum {

    /**
     * Gene Pattern.
     */
    GENE_PATTERN("Gene Pattern", "Gene Pattern"),
    
    /**
     * Comparative Marker Selection.
     */
    CMS("Comparative Marker Selection", "Gene Pattern - Grid"),

    /**
     * PCA.
     */
    PCA("Principal Component Analysis", "Gene Pattern - Grid"),

    /**
     * GISTIC.
     */
    GISTIC("Gistic", "Gene Pattern - ");
    
    private static Map<String, AnalysisJobTypeEnum> valueToTypeMap = 
                    new HashMap<String, AnalysisJobTypeEnum>();

    private String value;
    private String type;
    
    private AnalysisJobTypeEnum(String value, String type) {
        this.value = value;
        this.type = type;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    private static Map<String, AnalysisJobTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (AnalysisJobTypeEnum type : values()) {
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
    public static AnalysisJobTypeEnum getByValue(String value) {
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
