/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.geneexpression;



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
}
