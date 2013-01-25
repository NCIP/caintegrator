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
 * Annotation of allowed values for <code>ReporterList.type</code>.
 */
public enum ReporterTypeEnum {

    /**
     * Reporter for a SNP array.
     */
    DNA_ANALYSIS_REPORTER("dnaAnalysisReporter"),

    /**
     * Reporter for a transcript that is not the full gene sequence.
     */
    GENE_EXPRESSION_PROBE_SET("geneExpressionProbeSet"),

    /**
     * Gene-level reporter.
     */
    GENE_EXPRESSION_GENE("geneExpressionGeneLevel"),
    
    /**
     * Gistic genomic region reporter.
     */
    GISTIC_GENOMIC_REGION_REPORTER("gisticGenomicRegionReporter");
    
    private static Map<String, ReporterTypeEnum> valueToTypeMap = new HashMap<String, ReporterTypeEnum>();

    private String value;
    
    private ReporterTypeEnum(String value) {
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

    private static Map<String, ReporterTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ReporterTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator2.application.arraydata.ReporterTypeEnum@getValueToDisplayableMap()" 
     *              listKey="key" listValue="value"
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static Map<String, String> getValueToDisplayableMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET.getValue(), "Reporter Id");
        map.put(ReporterTypeEnum.GENE_EXPRESSION_GENE.getValue(), "Gene");
        return map;
    }
    
    /**
     * Returns the <code>ReporterTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ReporterTypeEnum getByValue(String value) {
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
