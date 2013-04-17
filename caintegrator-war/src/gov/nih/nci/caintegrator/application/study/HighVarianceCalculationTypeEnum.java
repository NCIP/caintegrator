/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;



/**
 * High variance calculation types for replicates in genomic data.  A percentage means the % threshold that a stdDev
 * can be away from the central tendency value.  A value, is simply the threshold of the stdDev as a number.
 */
public enum HighVarianceCalculationTypeEnum {

    /**
     * Percentage.
     */
    PERCENTAGE("Relative (Percentage)"),

    /**
     * Value.
     */
    VALUE("Normal (Value)");

    private String value;

    private HighVarianceCalculationTypeEnum(String value) {
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
