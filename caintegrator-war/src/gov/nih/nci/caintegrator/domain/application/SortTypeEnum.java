/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;



/**
 * Sorting type for <code>ResultColumn.sortType</code>.
 */
public enum SortTypeEnum {

    /**
     * Ascending.
     */
    ASCENDING("Ascending"),

    /**
     * Descending.
     */
    DESCENDING("Descending"),

    /**
     * No Sort.
     */
    UNSORTED("No Sort");

    private String value;

    private SortTypeEnum(String value) {
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
