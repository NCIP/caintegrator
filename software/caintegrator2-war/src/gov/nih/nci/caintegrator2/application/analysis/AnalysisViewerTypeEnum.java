/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the analysis viewer type.
 */
public enum AnalysisViewerTypeEnum {
    
    /**
     * Heatmap.
     */
    HEATMAP("heatmap"),
    
    /**
     * IGV.
     */
    IGV("igv");
    
    private String value;
    private static Map<String, AnalysisViewerTypeEnum> valueToTypeMap = new HashMap<String, AnalysisViewerTypeEnum>();
    
    private AnalysisViewerTypeEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }


    private static Map<String, AnalysisViewerTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (AnalysisViewerTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>AnalysisViewerTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static AnalysisViewerTypeEnum getByValue(String value) {
        return getValueToTypeMap().get(value);
    }
}
