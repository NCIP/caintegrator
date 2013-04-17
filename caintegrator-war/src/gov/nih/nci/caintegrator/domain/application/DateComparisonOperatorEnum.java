/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Possible entity type values for <code>DateComparisonCriterion.dateComparisonOperator</code>.
 */
public enum DateComparisonOperatorEnum {

    /**
     * Greater Than.
     */
    GREATER(">"),

    /**
     * Greater Than or Equal To.
     */
    GREATEROREQUAL(">="),

    /**
     * Equal To.
     */
    EQUAL("=="),

    /**
     * Less Than.
     */
    LESS("<"),

    /**
     * Less Than or Equal To.
     */
    LESSOREQUAL("<="),

    /**
     * Not Equal.
     */
    NOTEQUAL("!=");

    private String value;

    private DateComparisonOperatorEnum(String value) {
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
