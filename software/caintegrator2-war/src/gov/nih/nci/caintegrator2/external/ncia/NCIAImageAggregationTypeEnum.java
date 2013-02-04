/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible aggregation type values for <code>NCIAImageAggregator.imageAggregationType</code>.
 */
public enum NCIAImageAggregationTypeEnum {
    
    /**
     * Patient type, (currently unused).
     */
    PATIENT("patient"),
    /**
     * Image Study type.
     */
    IMAGESTUDY("imageStudy"),
    /**
     * Image Series type.
     */
    IMAGESERIES("imageSeries"),
    
    /**
     * Image type. (currently unused).
     */
    IMAGE("image");
    
    private static Map<String, NCIAImageAggregationTypeEnum> valueToTypeMap = 
                            new HashMap<String, NCIAImageAggregationTypeEnum>();

    private String value;
    
    private NCIAImageAggregationTypeEnum(String value) {
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

    private static Map<String, NCIAImageAggregationTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (NCIAImageAggregationTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>NCIAImageAggregationTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static NCIAImageAggregationTypeEnum getByValue(String value) {
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
