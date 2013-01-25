/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid.gistic;


import java.util.HashMap;
import java.util.Map;

/**
 * Annotation of allowed values for Refgene file parameter for GISTIC job.
 */
public enum GisticRefgeneFileEnum {

    /**
     * Human Hg16.
     */
    HUMAN_HG16("Human Hg16", "hg16_20070112.mat"),

    /**
     * Human Hg16.
     */
    HUMAN_HG17("Human Hg17", "hg17_20070131.mat"),

    /**
     * Human Hg16.
     */
    HUMAN_HG18("Human Hg18", "hg18_with_miR_20080407.mat");

    
    private static Map<String, GisticRefgeneFileEnum> valueToTypeMap = new HashMap<String, GisticRefgeneFileEnum>();

    private final String value;
    private final String parameterValue;
    
    private GisticRefgeneFileEnum(String value, String parameterValue) {
        this.value = value;
        this.parameterValue = parameterValue;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private static Map<String, GisticRefgeneFileEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (GisticRefgeneFileEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GisticRefgeneFileEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static GisticRefgeneFileEnum getByValue(String value) {
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

    /**
     * @return the parameterValue
     */
    public String getParameterValue() {
        return parameterValue;
    }
}
