/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;


/**
 * Indicates whether to search for up or down regulated genes in fold change query.
 */
public enum RangeTypeEnum {

    /**
     * Greater than or equal to.
     */
    GREATER_OR_EQUAL(">="),

    /**
     * Less than or equal to.
     */
    LESS_OR_EQUAL("<="),

    /**
     * Inside range of two numbers.
     */
    INSIDE_RANGE("Inside Range"),

    /**
     * Outside range of two numbers.
     */
    OUTSIDE_RANGE("Outside Range");

    private String value;

    private RangeTypeEnum(String value) {
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
