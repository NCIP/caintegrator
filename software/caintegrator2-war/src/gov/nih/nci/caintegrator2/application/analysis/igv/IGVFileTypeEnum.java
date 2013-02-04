/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.igv;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for the igv file type.
 */
public enum IGVFileTypeEnum {
    
    /**
     * Session.
     */
    SESSION("igvSession.xml"),
    
    /**
     * Sample Classification.
     */
    SAMPLE_CLASSIFICATION("sampleInfo.txt"),
    
    /**
     * Expression.
     */
    GENE_EXPRESSION("igvGeneExpression.gct"),
    
    /**
     * Segmentation.
     */
    SEGMENTATION("igvSegmentation.seg"),
    
    /**
     * Segmentation using calls.
     */
    SEGMENTATION_CALLS("igvSegmentation_calls.seg");
    
    private String filename;
    private static Map<String, IGVFileTypeEnum> valueToTypeMap = new HashMap<String, IGVFileTypeEnum>();
    
    private IGVFileTypeEnum(String filename) {
        this.filename = filename;
    }

    /**
     * @return the value
     */
    public String getFilename() {
        return filename;
    }


    private static Map<String, IGVFileTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (IGVFileTypeEnum type : values()) {
                valueToTypeMap.put(type.getFilename(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>IGVFileTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param filename the value to match
     * @return the matching type.
     */
    public static IGVFileTypeEnum getByFilename(String filename) {
        return getValueToTypeMap().get(filename);
    }
}
