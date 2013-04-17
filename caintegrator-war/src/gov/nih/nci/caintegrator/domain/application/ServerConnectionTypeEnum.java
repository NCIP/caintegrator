/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Possible server connection types.
 */
public enum ServerConnectionTypeEnum {

    /**
     * Subject type.
     */
    GRID("Grid"),

    /**
     * Sample type.
     */
    WEB("Web"),

    /**
     * Image Series type.
     */
    UNKNOWN("Server Type Unknown");

    private String value;

    private ServerConnectionTypeEnum(String value) {
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
