/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Possible mapping types for mapping imaging data to caI2 patients.
 */
public enum ImageDataSourceMappingTypeEnum {

    /**
     * Automatic mapping.
     */
    AUTO("Auto (No File Required)"),
    
    /**
     * Subject.
     */
    SUBJECT("By Subject"),

    /**
     * Image Series.
     */
    IMAGE_SERIES("By Image Series");
    
    private static Map<String, ImageDataSourceMappingTypeEnum> valueToTypeMap = 
        new HashMap<String, ImageDataSourceMappingTypeEnum>();

    private String value;
    
    private ImageDataSourceMappingTypeEnum(String value) {
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

    private static Map<String, ImageDataSourceMappingTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ImageDataSourceMappingTypeEnum type : values()) {
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
        values.add(AUTO.getValue());
        values.add(SUBJECT.getValue());
        values.add(IMAGE_SERIES.getValue());
        return values;
    }
    
    /**
     * Returns the <code>ImageDataSourceMappingTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ImageDataSourceMappingTypeEnum getByValue(String value) {
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
