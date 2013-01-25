/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Query result types.
 */
public enum ResultTypeEnum {

    /**
     * Clinical.
     */
    CLINICAL("clinical"),

    /**
     * Gene Expression data.
     */
    GENE_EXPRESSION("geneExpression", ReporterTypeEnum.GENE_EXPRESSION_GENE, 
            ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET),
    
    /**
     * Copy Number.
     */
     COPY_NUMBER("copyNumber", ReporterTypeEnum.DNA_ANALYSIS_REPORTER, 
             ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER),
    
    /**
     * IGV viewer.
     */
    IGV_VIEWER("igvViewer"),
    
    /**
     * Heat Map viewer.
     */
    HEATMAP_VIEWER("heatmapViewer");
    
    private static Map<String, ResultTypeEnum> valueToTypeMap = new HashMap<String, ResultTypeEnum>();

    private String value;
    private List<ReporterTypeEnum> associatedReporterTypes = new ArrayList<ReporterTypeEnum>();
    
    private ResultTypeEnum(String value, ReporterTypeEnum... associatedReporterTypes) {
        setValue(value);
        this.associatedReporterTypes.addAll(Arrays.asList(associatedReporterTypes));
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    private void setValue(String value) {
        this.value = value;
    }

    private static Map<String, ResultTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (ResultTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator2.application.query.ResultTypeEnum@getValueToDisplayableMap()" 
     *              listKey="key" listValue="value"
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static Map<String, String> getValueToDisplayableMap() {
        Map<String, String> map = new LinkedHashMap<String, String>();
        map.put(ResultTypeEnum.CLINICAL.getValue(), "Annotation");
        map.put(ResultTypeEnum.GENE_EXPRESSION.getValue(), "Gene Expression");
        map.put(ResultTypeEnum.COPY_NUMBER.getValue(), "Copy Number");
        map.put(ResultTypeEnum.IGV_VIEWER.getValue(), "Integrative Genomics Viewer");
        map.put(ResultTypeEnum.HEATMAP_VIEWER.getValue(), "Heat Map Viewer");
        return map;
    }
    
    /**
     * Returns the <code>ResultTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static ResultTypeEnum getByValue(String value) {
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
    
    /**
     * Function to determine if the given reporterType is associated with this result type.
     * @param reporterType to check against.
     * @return T/F if reporter type matches this result type.
     */
    public boolean isReporterMatch(ReporterTypeEnum reporterType) {
        return associatedReporterTypes.contains(reporterType);
    }
}
