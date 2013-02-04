/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supported Platforms.
 */
public enum PlatformTypeEnum {

    /**
     * Affymetrix Gene Expression platform.
     */
    AFFYMETRIX_GENE_EXPRESSION("Affymetrix Gene Expression"),
    
    /**
     * Affymetrix SNP platform.
     */
    AFFYMETRIX_DNA_ANALYSIS("Affymetrix SNP"),
    
    /**
     * Agilent Gene Expression platform.
     */
    AGILENT_GENE_EXPRESSION("Agilent Gene Expression"),
    
    /**
     * Agilent Copy Number platform.
     */
    AGILENT_DNA_ANALYSIS("Agilent Copy Number");
    
    private static Map<String, PlatformTypeEnum> valueToTypeMap = new HashMap<String, PlatformTypeEnum>();

    private String value;
    
    private PlatformTypeEnum(String value) {
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

    private static Map<String, PlatformTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (PlatformTypeEnum type : values()) {
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
    public static PlatformTypeEnum getByValue(String value) {
        checkType(value);
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
     * list="@gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum@getValuesToDisplay()" 
     * 
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static List<String> getValuesToDisplay() {
        List<String> list = new ArrayList<String>();
        for (PlatformTypeEnum type : values()) {
            list.add(type.getValue());
        }
        return list;
    }
}
