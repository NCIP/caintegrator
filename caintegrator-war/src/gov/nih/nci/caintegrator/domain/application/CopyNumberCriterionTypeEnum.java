/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Copy number critrion type values for <code>CopyNumberAlterationCriterion.copyNumberCriterionType</code>.
 */
public enum CopyNumberCriterionTypeEnum {

    /**
     * Gene Name.
     */
    SEGMENT_VALUE("Segment Value"),

    /**
     * CGHcall Calls value.
     */
    CALLS_VALUE("Calls Value");

    private String value;

    private CopyNumberCriterionTypeEnum(String value) {
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
