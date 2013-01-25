/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;


import java.util.HashMap;
import java.util.Map;

/**
 * Different calculation types for the GeneExpressionPlotTypes.
 */
public enum PlotCalculationTypeEnum {

    /**
     * Mean.
     */
    MEAN("mean"),

    /**
     * Median.
     */
    MEDIAN("median"),
    
    /**
     * Log2 Intensity.
     */
    LOG2_INTENSITY("log2Intensity"),
    
    /**
     * Box Whisker Log2 Intensity.
     */
    BOX_WHISKER_LOG2_INTENSITY("boxWhiskerLog2Intensity");
    
    private static Map<String, PlotCalculationTypeEnum> valueToTypeMap = 
                    new HashMap<String, PlotCalculationTypeEnum>();

    private String value;
    
    private PlotCalculationTypeEnum(String value) {
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

    private static Map<String, PlotCalculationTypeEnum> getValueToTypeMap() {
        if (valueToTypeMap.isEmpty()) {
            for (PlotCalculationTypeEnum type : values()) {
                valueToTypeMap.put(type.getValue(), type);
            }
        }
        return valueToTypeMap;
    }
    
    /**
     * Returns the <code>GeneExpressionPlotCalculationType</code> corresponding to the given value. Returns null
     * for null value.
     * 
     * @param value the value to match
     * @return the matching type.
     */
    public static PlotCalculationTypeEnum getByValue(String value) {
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
