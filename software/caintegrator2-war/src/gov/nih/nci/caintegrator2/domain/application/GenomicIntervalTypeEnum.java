/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import java.util.HashMap;
import java.util.Map;

/**
 * Genomic Interval values for <code>CopyNumberAlterationCriterion.genomicIntervalType</code>.
 */
public enum GenomicIntervalTypeEnum {

    /**
     * Gene Name.
     */
    GENE_NAME("Gene Name"),
    
    /**
     * Chromosome Coordinates.
     */
    CHROMOSOME_COORDINATES("Chromosome Coordinates");
    
    private static Map<String, GenomicIntervalTypeEnum> valueToTypeMap = 
        new HashMap<String, GenomicIntervalTypeEnum>();
    
    private String value;
    
    
    private GenomicIntervalTypeEnum(String value) {
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


    private static Map<String, GenomicIntervalTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (GenomicIntervalTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GenomicIntervalTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static GenomicIntervalTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>GenomicIntervalTypeEnum</code> value.
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
