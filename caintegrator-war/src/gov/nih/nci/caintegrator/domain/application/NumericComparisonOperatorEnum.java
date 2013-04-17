/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Possible entity type values for <code>NumericComparisonCriterion.numericComparisonOperator</code>.
 */
public enum NumericComparisonOperatorEnum {

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

    private NumericComparisonOperatorEnum(String value) {
        this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the value
     */
    public String valueOf() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
