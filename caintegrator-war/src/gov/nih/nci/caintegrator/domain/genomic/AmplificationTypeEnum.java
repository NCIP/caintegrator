/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;



/**
 * Allowed values for <code>GisticGenomicRegionReporter.geneAmplificationType</code>.
 */
public enum AmplificationTypeEnum  {

    /**
     * Amplified type.
     */
    AMPLIFIED("Amplified"),

    /**
     * Deleted type.
     */
    DELETED("Deleted");

    private String value;

    private AmplificationTypeEnum(String value) {
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
