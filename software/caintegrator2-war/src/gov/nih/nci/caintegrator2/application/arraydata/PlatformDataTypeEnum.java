package gov.nih.nci.caintegrator2.application.arraydata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Possible data types for genomic data.
 */
public enum PlatformDataTypeEnum {

    /**
     * Expression.
     */
    EXPRESSION("Expression"),
    
    /**
     * CopyNumber.
     */
    COPY_NUMBER("Copy Number"),
    
    /**
     * SNP.
     */
    SNP("SNP");
    
    private String value;
    
    private PlatformDataTypeEnum(String value) {
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
    
    private static Map<String, PlatformDataTypeEnum> valueToTypeMap = 
        new HashMap<String, PlatformDataTypeEnum>();

    private static Map<String, PlatformDataTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (PlatformDataTypeEnum type : values()) {
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
        for (PlatformDataTypeEnum type : values()) {
            if (!PlatformDataTypeEnum.SNP.equals(type)) { // TODO - For now disable the SNP data type.
                values.add(type.getValue());
            }
        }
        return values;
    }
    
    /**
     * Returns the <code>GenomicDataSourceDataTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static PlatformDataTypeEnum getByValue(String value) {
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
