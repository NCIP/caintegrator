/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;


/**
 * Enum for the analysis viewer type.
 */
public enum AnalysisViewerTypeEnum {

    /**
     * Heatmap.
     */
    HEATMAP("heatmap"),

    /**
     * IGV.
     */
    IGV("igv");

    private String value;

    private AnalysisViewerTypeEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
