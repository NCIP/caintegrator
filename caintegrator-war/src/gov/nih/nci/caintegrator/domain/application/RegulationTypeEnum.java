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
public enum RegulationTypeEnum {

    /**
     * Up regulated.
     */
    UP("Up"),

    /**
     * Down regulated.
     */
    DOWN("Down"),

    /**
     * Up or down regulated.
     */
    UP_OR_DOWN("Up or Down"),

    /**
     * Up or down regulated.
     */
    UNCHANGED("Unchanged");

    private String value;

    private RegulationTypeEnum(String value) {
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
