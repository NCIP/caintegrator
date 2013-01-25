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
 * Possible data types for genomic data.
 */
public enum GenomicDataSourceDataTypeEnum {

    /**
     * Expression.
     */
    EXPRESSION("Expression"),
    
    /**
     * CopyNumber.
     */
    COPY_NUMBER("Copy Number");
    
    private String value;
    
    private GenomicDataSourceDataTypeEnum(String value) {
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
    
    private static Map<String, GenomicDataSourceDataTypeEnum> valueToTypeMap = 
        new HashMap<String, GenomicDataSourceDataTypeEnum>();

    private static Map<String, GenomicDataSourceDataTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (GenomicDataSourceDataTypeEnum type : values()) {
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
        values.add(EXPRESSION.getValue());
        values.add(COPY_NUMBER.getValue());
        return values;
    }
    
    /**
     * Returns the <code>GenomicDataSourceDataTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static GenomicDataSourceDataTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>GenomicDataSourceDataTypeEnum</code> value.
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
