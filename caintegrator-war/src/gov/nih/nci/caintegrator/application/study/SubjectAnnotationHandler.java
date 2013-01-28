/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.domain.translational.Timepoint;

import org.apache.commons.lang3.StringUtils;

/**
 * Loads annotation into <code>StudySubjectAssignments</code>.
 */
class SubjectAnnotationHandler extends AbstractAnnotationHandler {
    private static final int MAX_IDENTIFIER_LENGTH = 50;
    private final DelimitedTextClinicalSourceConfiguration sourceConfiguration;
    private StudySubjectAssignment currentSubjectAssignment;

    SubjectAnnotationHandler(DelimitedTextClinicalSourceConfiguration sourceConfiguration) {
        super();
        this.sourceConfiguration = sourceConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleIdentifier(String identifier) throws ValidationException {
        if (StringUtils.length(identifier) > MAX_IDENTIFIER_LENGTH) {
            throw new ValidationException("Identifiers can only be up to " + MAX_IDENTIFIER_LENGTH
                    + " characters in length.");
        }
        currentSubjectAssignment = sourceConfiguration.getStudyConfiguration().getOrCreateSubjectAssignment(identifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleAnnotationValue(AbstractAnnotationValue annotationValue) throws ValidationException {
        if (currentSubjectAssignment.hasValueForDefinition(annotationValue.getAnnotationDefinition())) {
            throw new ValidationException("Value already loaded: Subject " + currentSubjectAssignment.getIdentifier()
                    + " already has a value for " + annotationValue.getAnnotationDefinition().getDisplayName());
        }
        createAnnotation(annotationValue);
    }

    private SubjectAnnotation createAnnotation(AbstractAnnotationValue annotationValue) {
        SubjectAnnotation subjectAnnotation = new SubjectAnnotation();
        subjectAnnotation.setAnnotationValue(annotationValue);
        currentSubjectAssignment.getSubjectAnnotationCollection().add(subjectAnnotation);
        return subjectAnnotation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void handleAnnotationValue(AbstractAnnotationValue annotationValue, String timepointValue) {
        Timepoint timepoint = sourceConfiguration.getStudyConfiguration().getOrCreateTimepoint(timepointValue);
        createAnnotation(annotationValue).setTimepoint(timepoint);
    }

}
