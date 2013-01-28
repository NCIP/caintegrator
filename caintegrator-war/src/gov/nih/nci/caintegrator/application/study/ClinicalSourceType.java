/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

/**
 * Used to indicate how annotation data were loaded.
 */
public enum ClinicalSourceType {
    
    /**
     * Clinical data will be loaded from an external CTODS server.
     */
    CTODS,
    
    /**
     * Clinical data will be loaded from a comma-separated value format text file.
     */
    DELIMITED_TEXT
    
}
