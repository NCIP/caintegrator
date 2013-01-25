/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.kmplot;


import java.util.HashMap;
import java.util.Map;

/**
 * Plot types.
 */
public enum PlotTypeEnum {

    /**
     * Annotation Based.
     */
    ANNOTATION_BASED("annotationBased"),

    /**
     * Gene Expression.
     */
    GENE_EXPRESSION("geneExpression"),
    /**
     * Query Based.
     */
    QUERY_BASED("queryBased"),
    /**
     * Genomic Query Based.
     */
    GENOMIC_QUERY_BASED("genomicQueryBased"),
    /**
     * Clinical Query Based.
     */
    CLINICAL_QUERY_BASED("clinicalQueryBased");
    
    private static Map<String, PlotTypeEnum> valueToTypeMap = new HashMap<String, PlotTypeEnum>();

    private String value;
    
    private PlotTypeEnum(String value) {
        setValue(value);
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

    private static Map<String, PlotTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (PlotTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Used in the JSP's to retrieve the displayable string version of the Enum values.
     * Ex usage: 
     * list="@gov.nih.nci.caintegrator2.application.kmplot.KMPlotTypeEnum@getValueToDisplayableMap()" 
     *              listKey="key" listValue="value"
     * @return HashMap of EnumeratedValue's String to Displayable String. 
     */
    public static Map<String, String> getValueToDisplayableMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(PlotTypeEnum.ANNOTATION_BASED.getValue(), "Annotation Based");
        map.put(PlotTypeEnum.GENE_EXPRESSION.getValue(), "Gene Expression Based");
        map.put(PlotTypeEnum.QUERY_BASED.getValue(), "Query Based");
        return map;
    }
    
    /**
     * Returns the <code>KMPlotTypeEnum</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static PlotTypeEnum getByValue(String value) {
        checkType(value);
        return getValueToTypeMap().get(value);
    }

    /**
     * Checks to see that the value given is a legal <code>AssayType</code> value.
     * 
     * @param value the value to check;
     * @return T/F depending if it's a valid type.
     */
    public static boolean checkType(String value) {
        if (value == null || !getValueToTypeMap().containsKey(value)) {
            return false;
        }
        return true;
    }
}
