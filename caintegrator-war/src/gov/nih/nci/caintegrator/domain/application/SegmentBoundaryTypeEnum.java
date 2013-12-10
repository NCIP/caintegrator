/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

/**
 * Segment boundary type values for <code>CopyNumberAlterationCriterion.segmentBoundaryType</code>.
 */
public enum SegmentBoundaryTypeEnum {

    /**
     * One or more.
     */
    ONE_OR_MORE("One or more"),

    /**
     * Inclusive.
     */
    INCLUSIVE("Inclusive");

    private String value;

    private SegmentBoundaryTypeEnum(String value) {
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
