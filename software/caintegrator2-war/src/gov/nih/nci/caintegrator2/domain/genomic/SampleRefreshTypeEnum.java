/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

/**
 * Sample refresh type.
 * @author mshestopalov
 *
 */
public enum SampleRefreshTypeEnum {

    /**
     * Delete on refresh.
     */
    DELETE_ON_REFRESH("Sample Deleted"),

    /**
     * Add on refresh.
     */
    ADD_ON_REFRESH("Sample Added"),

    /**
     * Update on refresh.
     */
    UPDATE_ON_REFRESH("Sample Updated"),

    /**
     * Unchanged.
     */
    UNCHANGED("Unchanged");

    private final String displayName;

    /**
     * Constructor.
     * @param displayName the display name of the enum
     */
    private SampleRefreshTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}
