/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;



/**
 * Supported Platforms.
 */
public enum PlatformChannelTypeEnum {

    /**
     * One-Color channel platform.
     */
    ONE_COLOR("One-Color"),

    /**
     * Two-Color channel platform.
     */
    TWO_COLOR("Two-Color");

    private String value;

    private PlatformChannelTypeEnum(String value) {
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
