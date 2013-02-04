/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.Set;

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
    void handleAnnotationValue(AbstractAnnotationValue annotationValue) {
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

    @Override
    void addDefinitionsToStudy(Set<AnnotationDefinition> annotationDefinitions) {
        Study study = sourceConfiguration.getStudyConfiguration().getStudy();
        
        for (AnnotationDefinition definition : annotationDefinitions) {
            if (!study.getSubjectAnnotationCollection().contains(definition)) {
                study.getSubjectAnnotationCollection().add(definition);
            }
        }
    }

}
