/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Possible wild card type values for <code>StringComparisonCriterion.wildCardType</code>.
 */
public enum WildCardTypeEnum {

    /**
     * No Wild Card.
     */
    WILDCARD_OFF("wildCardOff"),

    /**
     * Wild Card after the given string.
     */
    WILDCARD_BEFORE_STRING("wildCardBeforeString"),

    /**
     * Wild Card before the given string.
     */
    WILDCARD_AFTER_STRING("wildCardAfterString"),

    /**
     * Wild Card before and after the given string.
     */
    WILDCARD_BEFORE_AND_AFTER_STRING("wildCardBeforeAndAfterString"),

    /**
     * Not equal to the given string.
     */
    NOT_EQUAL_TO("notEqualTo");

    private String value;

    private WildCardTypeEnum(String value) {
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
