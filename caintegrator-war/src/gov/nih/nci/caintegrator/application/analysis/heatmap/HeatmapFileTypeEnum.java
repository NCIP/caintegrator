/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.heatmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the heatmap file type.
 */
public enum HeatmapFileTypeEnum {
    
    /**
     * Launch File.
     */
    LAUNCH_FILE("heatmapLaunch.jnlp"),
    
    /**
     * Genomic Data.
     */
    GENOMIC_DATA("heatmapGenomicData.txt"),
    
    /**
     * Calls Data.
     */
    CALLS_DATA("heatmapCallsData.txt"),
    
    /**
     * Layout.
     */
    LAYOUT("chr2genecount.dat"),
    
    /**
     * Annotations.
     */
    ANNOTATIONS("heatmapAnnotations.txt"),
    
    /**
     * Small Bins.
     */
    SMALL_BINS_FILE("bins10K.dat"),
    
    /**
     * Large Bins.
     */
    LARGE_BINS_FILE("bins200K.dat");
    
    private String filename;
    private static Map<String, HeatmapFileTypeEnum> valueToTypeMap = new HashMap<String, HeatmapFileTypeEnum>();
    
    private HeatmapFileTypeEnum(String filename) {
        this.filename = filename;
    }

    /**
     * @return the value
     */
    public String getFilename() {
        return filename;
    }


    private static Map<String, HeatmapFileTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (HeatmapFileTypeEnum type : values()) {
                valueToTypeMap.put(type.getFilename(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>HeatmapFileTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param filename the value to match
     * @return the matching type.
     */
    public static HeatmapFileTypeEnum getByFilename(String filename) {
        return getValueToTypeMap().get(filename);
    }
}
