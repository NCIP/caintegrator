/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid.gistic;



/**
 * Annotation of allowed values for Refgene file parameter for GISTIC job.
 */
public enum GisticRefgeneFileEnum {

    /**
     * Human Hg16.
     */
    HUMAN_HG16("Human Hg16", "hg16_20070112.mat"),

    /**
     * Human Hg16.
     */
    HUMAN_HG17("Human Hg17", "hg17_20070131.mat"),

    /**
     * Human Hg16.
     */
    HUMAN_HG18("Human Hg18", "hg18_with_miR_20080407.mat");

    private final String value;
    private final String parameterValue;

    private GisticRefgeneFileEnum(String value, String parameterValue) {
        this.value = value;
        this.parameterValue = parameterValue;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the parameterValue
     */
    public String getParameterValue() {
        return parameterValue;
    }
}
