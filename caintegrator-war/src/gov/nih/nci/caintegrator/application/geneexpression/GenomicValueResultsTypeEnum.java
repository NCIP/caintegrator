/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public enum GenomicValueResultsTypeEnum {
    
    /**
     * Mean.
     */
    GENE_EXPRESSION("Expression Intensity"),

    /**
     * Median.
     */
    FOLD_CHANGE("Fold Change");
        
    private static Map<String, GenomicValueResultsTypeEnum> valueToTypeMap = 
                    new HashMap<String, GenomicValueResultsTypeEnum>();

    private String value;
    
    private GenomicValueResultsTypeEnum(String value) {
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

    private static Map<String, GenomicValueResultsTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (GenomicValueResultsTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GenomicValueResultsTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static GenomicValueResultsTypeEnum getByValue(String value) {
        if (!checkType(value)) {
            return null;
        }
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>AssayType</code> value.
     * 
     * @param value the value to check;
     * @return T/F value if it exists.
     */
    public static boolean checkType(String value) {
        if (value == null || !getValueToTypeMap().containsKey(value)) {
            return false;
        }
        return true;
    }

}
