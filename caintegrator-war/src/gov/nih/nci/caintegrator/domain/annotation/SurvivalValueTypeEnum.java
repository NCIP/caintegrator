/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;



/**
 * Possible survival type values for <code>SurvivalValueDefinition.survivalValueType</code>.
 */
public enum SurvivalValueTypeEnum {

    /**
     * By Date.
     */
    DATE("By Date"),

    /**
     * By Length of time in study.
     */
    LENGTH_OF_TIME("By Length of time in study");

    private String value;

    private SurvivalValueTypeEnum(String value) {
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
