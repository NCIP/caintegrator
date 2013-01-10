/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Used to indicate the type of data held by a particular annotation field.
 */
public enum AnnotationFieldType {
    
    /**
     * The Unique Identifier.
     */
    IDENTIFIER("identifier"),
    
    /**
     * An Annotation type.
     */
    ANNOTATION("annotation"),

    /**
     * A Timepoint type.
     */
    TIMEPOINT("timepoint");
    
    private static Map<String, AnnotationFieldType> valueToTypeMap = new HashMap<String, AnnotationFieldType>();

    private String value;
    
    private AnnotationFieldType(String value) {
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

    private static Map<String, AnnotationFieldType> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (AnnotationFieldType type : values()) {
                valueToTypeMap.put(type.getValue().toLowerCase(Locale.getDefault()), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>AnnotationFieldType</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static AnnotationFieldType getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value.toLowerCase(Locale.getDefault()));
    }

    /**
     * Checks to see that the value given is a legal <code>AssayType</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value == null || !getValueToTypeMap().containsKey(value.toLowerCase(Locale.getDefault()))) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }    
    
}
