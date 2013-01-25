/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;


import java.util.HashMap;
import java.util.Map;

/**
 * Genome Build.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // Check all known string
public enum GenomeBuildVersionEnum {

    /**
     * HG16.
     */
    HG16("hg16"),

    /**
     * HG17.
     */
    HG17("hg17"),

    /**
     * HG18.
     */
    HG18("hg18"),
    
    /**
     * HG19.
     */
    HG19("hg19");
    
    private static Map<String, GenomeBuildVersionEnum> valueToTypeMap = new HashMap<String, GenomeBuildVersionEnum>();

    private String value;
    
    private GenomeBuildVersionEnum(String value) {
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

    private static Map<String, GenomeBuildVersionEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (GenomeBuildVersionEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GenomeBuildTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static GenomeBuildVersionEnum getByValue(String value) {
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
     * @param genomeVersion the genome version
     * @return the enum of the known genome version else null
     */
    @SuppressWarnings("PMD.CyclomaticComplexity") // Check all known string
    public static GenomeBuildVersionEnum matchGenomVersion(String genomeVersion) {
        if (genomeVersion.contains("g16") || genomeVersion.equalsIgnoreCase("ncbi build 34")) {
            return HG16;
        } else if (genomeVersion.contains("g17") || genomeVersion.equalsIgnoreCase("ncbi build 35")) {
            return HG17;
        } else if (genomeVersion.contains("g18") || genomeVersion.equalsIgnoreCase("ncbi build 36.1")) {
            return HG18;
        } else if (genomeVersion.contains("g19")) {
            return HG19;
        }
        return null;
    }
}
