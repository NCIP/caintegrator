/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

/**
 * Used to indicate the type of data held by a particular annotation field.
 */
public enum AnnotationFieldType {
    
    /**
     * The Unique Identifier.
     */
    IDENTIFIER,
    
    /**
     * A text type field.
     */
    TEXT,
    
    /**
     * A choice type field.
     */
    CHOICE
    
}
