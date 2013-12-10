/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;



/**
 * Possible central tendencies for genomic data.
 */
public enum CentralTendencyTypeEnum {

    /**
     * Mean.
     */
    MEAN("Mean"),

    /**
     * Median.
     */
    MEDIAN("Median");

    private String value;

    private CentralTendencyTypeEnum(String value) {
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
