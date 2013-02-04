/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public enum ImageAnnotationUploadType {

    /**
     * Upload annotation file.
     */
    FILE("Upload Annotation File"),
    
    /**
     * AIM Data Service.
     */
    AIM("Use AIM Data Service");
    
    private String value;
    
    private ImageAnnotationUploadType(String value) {
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
    
    private static Map<String, ImageAnnotationUploadType> valueToTypeMap = 
        new HashMap<String, ImageAnnotationUploadType>();

    private static Map<String, ImageAnnotationUploadType> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ImageAnnotationUploadType type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used to retrieve all string values (by the JSP for display purposes).
     * @return List of all string values which represent the ENUM values.
     */
    public static List<String> getStringValues() {
        List<String> values = new ArrayList<String>();
        for (ImageAnnotationUploadType type : values()) {
            values.add(type.getValue());
        }
        return values;
    }
    
    /**
     * Returns the <code>ImageAnnotationUploadType</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ImageAnnotationUploadType getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>ImageAnnotationUploadType</code> value.
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
