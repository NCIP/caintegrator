/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Possible boolean operators <code>CompoundCriterion.entityType</code>.
 */
public enum BooleanOperatorEnum {

    /**
     * Boolean and.
     */
    AND("and"),

    /**
     * Boolean or.
     */
    OR("or");

    private String value;

    private BooleanOperatorEnum(String value) {
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
