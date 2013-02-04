/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enum of different types of Genomic Annotations.
 */
public enum CaBioSearchTypeEnum {
    /**
     * Search on keywords.
     */
    GENE_KEYWORDS("Gene Keywords", "fullName"),

    /**
     * Search on gene symbol.
     */
    GENE_SYMBOL("Gene Symbol", "symbol", "hugoSymbol"),
    
    /**
     * Search on pathways.
     */
    PATHWAYS("Pathways", "name", "displayValue", "description"),
    
    /**
     * Search on pathways.
     */
    GENE_ALIAS("Gene Alias", "name"),
    
    /**
     * Search on Database cross reference.
     */
    DATABASE_CROSS_REF("Database Cross Reference Identifier", "crossReferenceId");
    
    private static Map<String, CaBioSearchTypeEnum> valueToTypeMap = 
        new HashMap<String, CaBioSearchTypeEnum>();

    private String value;
    private List<String> searchableAttributes = new ArrayList<String>();
    
    private CaBioSearchTypeEnum(String value, String... searchableAttributes) {
        this.value = value;
        this.searchableAttributes.addAll(Arrays.asList(searchableAttributes));
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the geneAttributeValue
     */
    public List<String> getSearchableAttributes() {
        return searchableAttributes;
    }

    private static Map<String, CaBioSearchTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (CaBioSearchTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * @return List of Displayable Strings for this enum.
     */
    public static List<String> getDisplayableValues() {
        List<String> list = new ArrayList<String>();
        list.add(CaBioSearchTypeEnum.GENE_KEYWORDS.getValue());
        list.add(CaBioSearchTypeEnum.GENE_SYMBOL.getValue());
        list.add(CaBioSearchTypeEnum.GENE_ALIAS.getValue());
        list.add(CaBioSearchTypeEnum.DATABASE_CROSS_REF.getValue());
        list.add(CaBioSearchTypeEnum.PATHWAYS.getValue());
        return list;
    }
    
    /**
     * Returns the <code>GenomicAnnotationEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static CaBioSearchTypeEnum getByValue(String value) {
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
