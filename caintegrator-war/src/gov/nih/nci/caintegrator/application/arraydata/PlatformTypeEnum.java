/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;


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
    AFFYMETRIX_GENE_EXPRESSION("Affymetrix Gene Expression", "Expression"),
    
    /**
     * Affymetrix SNP platform.
     */
    AFFYMETRIX_SNP("Affymetrix SNP", "SNP"),
    
    /**
     * Affymetrix Copy Number platform.
     */
    AFFYMETRIX_COPY_NUMBER("Affymetrix Copy Number", "Copy Number"),
    
    /**
     * Agilent Gene Expression platform.
     */
    AGILENT_GENE_EXPRESSION("Agilent Gene Expression", "Expression"),
    
    /**
     * Agilent Copy Number platform.
     */
    AGILENT_COPY_NUMBER("Agilent Copy Number", "Copy Number");
    
    private static Map<String, PlatformTypeEnum> valueToTypeMap = new HashMap<String, PlatformTypeEnum>();

    private String value;
    private String dataType;
    
    private PlatformTypeEnum(String value, String dataType) {
        this.value = value;
        this.dataType = dataType;
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

    /**
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
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
     * list="@gov.nih.nci.caintegrator.application.arraydata.PlatformTypeEnum@getValuesToDisplay()" 
     * 
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static List<String> getValuesToDisplay() {
        List<String> list = new ArrayList<String>();
        for (PlatformTypeEnum type : values()) {
            if (!PlatformTypeEnum.AFFYMETRIX_SNP.equals(type)) { // TODO - For now disable the SNP type.
                list.add(type.getValue());
            }
        }
        return list;
    }
    
    /**
     * 
     * @return boolean is a gene expression data type.
     */
    public boolean isGeneExpression() {
        return PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getDataType().equals(getDataType());
    }
    
    /**
     * 
     * @return boolean is a copy number data type
     */
    public boolean isCopyNumber() {
        return PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getDataType().equals(getDataType());
    }
}
