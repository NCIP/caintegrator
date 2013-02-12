/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Possible entity type values for <code>AbstractAnnotationCriterion.entityType</code>.
 */
public enum GenomicCriterionTypeEnum {

    /**
     * Copy Number Type.
     */
    COPY_NUMBER("copyNumber"),
    
    /**
     * Gene Expression Type.
     */
    GENE_EXPRESSION("geneExpression");
    
    private static Map<String, GenomicCriterionTypeEnum> valueToTypeMap = 
        new HashMap<String, GenomicCriterionTypeEnum>();

    private String value;
    
    private GenomicCriterionTypeEnum(String value) {
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

    private static Map<String, GenomicCriterionTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (GenomicCriterionTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GenomicCriterionTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static GenomicCriterionTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>GenomicCriterionTypeEnum</code> value.
     * 
     * @param value the value to check;
     */
    public static void checkType(String value) {
        if (value != null && !getValueToTypeMap().containsKey(value)) {
            throw new IllegalArgumentException("No matching type for " + value);
        }
    }
}