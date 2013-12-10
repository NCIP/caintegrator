/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;



/**
 * Possible mapping types for mapping imaging data to caI2 patients.
 */
public enum ImageDataSourceMappingTypeEnum {

    /**
     * Automatic mapping.
     */
    AUTO("Auto (No File Required)"),

    /**
     * Subject.
     */
    SUBJECT("By Subject"),

    /**
     * Image Series.
     */
    IMAGE_SERIES("By Image Series");

    private String value;

    private ImageDataSourceMappingTypeEnum(String value) {
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
