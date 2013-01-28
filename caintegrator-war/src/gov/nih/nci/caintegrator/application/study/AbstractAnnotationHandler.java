/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;

/**
 * Used to load annotation values into various annotatable entities.
 */
abstract class AbstractAnnotationHandler {

    abstract void handleIdentifier(String identifier) throws ValidationException;

    abstract void handleAnnotationValue(AbstractAnnotationValue annotationValue) throws ValidationException;

    abstract void handleAnnotationValue(AbstractAnnotationValue annotationValue, String timepointValue) 
        throws ValidationException;

}
