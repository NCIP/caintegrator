/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;


/**
 * Enum for the expression type.
 */
public enum ExpressionTypeEnum {

    /**
     * Fold Change.
     */
    FOLD_CHANGE("By Fold Change"),

    /**
     * Expression Level.
     */
    EXPRESSION_LEVEL("By Expression Level");

    private String value;

    private ExpressionTypeEnum(String value) {
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
