/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

/**
 * Loads annotation into <code>StudySubjectAssignments</code>.
 */
class SubjectAnnotationHandler extends AbstractAnnotationHandler {

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
    void handleIdentifier(String identifier) {
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
