package gov.nih.nci.caintegrator2.application.arraydata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supported Platforms.
 */
public enum PlatformVendorEnum {

    /**
     * Affymetrix platform.
     */
    AFFYMETRIX("Affymetrix", 2, 3),
    
    /**
     * Agilent platform.
     */
    AGILENT("Agilent", 5, 5);
    
    private static Map<String, PlatformVendorEnum> valueToTypeMap = new HashMap<String, PlatformVendorEnum>();

    private String value;
    private int sampleMappingColumns;
    private int copyNumberMappingColumns;
    
    private PlatformVendorEnum(String value, int sampleMappingColumns, int copyNumberMappingColumns) {
        this.value = value;
        this.sampleMappingColumns = sampleMappingColumns;
        this.copyNumberMappingColumns = copyNumberMappingColumns;
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

    private static Map<String, PlatformVendorEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (PlatformVendorEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>PlatformTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static PlatformVendorEnum getByValue(String value) {
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum@getValuesToDisplay()" 
     * 
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static List<String> getValuesToDisplay() {
        List<String> list = new ArrayList<String>();
        for (PlatformVendorEnum vendor : values()) {
            list.add(vendor.getValue());
        }
        return list;
    }

    /**
     * @return the sampleMappingColumns
     */
    public int getSampleMappingColumns() {
        return sampleMappingColumns;
    }

    /**
     * @param sampleMappingColumns the sampleMappingColumns to set
     */
    public void setSampleMappingColumns(int sampleMappingColumns) {
        this.sampleMappingColumns = sampleMappingColumns;
    }

    /**
     * @return the copyNumberMappingColumns
     */
    public int getCopyNumberMappingColumns() {
        return copyNumberMappingColumns;
    }

    /**
     * @param copyNumberMappingColumns the copyNumberMappingColumns to set
     */
    public void setCopyNumberMappingColumns(int copyNumberMappingColumns) {
        this.copyNumberMappingColumns = copyNumberMappingColumns;
    }
}