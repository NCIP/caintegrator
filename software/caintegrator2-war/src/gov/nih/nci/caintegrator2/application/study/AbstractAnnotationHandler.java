/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import java.util.Set;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;

/**
 * Used to load annotation values into various annotatable entities.
 */
abstract class AbstractAnnotationHandler {

    abstract void handleIdentifier(String identifier) throws ValidationException;

    abstract void handleAnnotationValue(AbstractAnnotationValue annotationValue);

    abstract void handleAnnotationValue(AbstractAnnotationValue annotationValue, String timepointValue);

    abstract void addDefinitionsToStudy(Set<AnnotationDefinition> annotationDefinitions);

}
