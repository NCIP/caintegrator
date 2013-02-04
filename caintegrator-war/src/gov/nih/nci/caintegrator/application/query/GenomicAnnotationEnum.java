/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum of different types of Genomic Annotations.
 */
public enum GenomicAnnotationEnum {
    /**
     * Gene Name annotation type.
     */
    GENE_NAME("Gene Name"),

    /**
     * Fole Change annotation type.
     */
    FOLD_CHANGE("Fold Change");
    
    private static Map<String, GenomicAnnotationEnum> valueToTypeMap = new HashMap<String, GenomicAnnotationEnum>();

    private String value;
    
    private GenomicAnnotationEnum(String value) {
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

    private static Map<String, GenomicAnnotationEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (GenomicAnnotationEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GenomicAnnotationEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static GenomicAnnotationEnum getByValue(String value) {
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
