/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.domain.translational.Timepoint;

/**
 * 
 */
public class SubjectAnnotation extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private AbstractAnnotationValue annotationValue;
    private StudySubjectAssignment studySubjectAssignment;
    private Timepoint timepoint;
    
    /**
     * @return the annotationValue
     */
    public AbstractAnnotationValue getAnnotationValue() {
        return annotationValue;
    }
    
    /**
     * @param annotationValue the annotationValue to set
     */
    public void setAnnotationValue(AbstractAnnotationValue annotationValue) {
        this.annotationValue = annotationValue;
    }
    
    /**
     * @return the studySubjectAssignment
     */
    public StudySubjectAssignment getStudySubjectAssignment() {
        return studySubjectAssignment;
    }
    
    /**
     * @param studySubjectAssignment the studySubjectAssignment to set
     */
    public void setStudySubjectAssignment(StudySubjectAssignment studySubjectAssignment) {
        this.studySubjectAssignment = studySubjectAssignment;
    }
    
    /**
     * @return the timepoint
     */
    public Timepoint getTimepoint() {
        return timepoint;
    }
    
    /**
     * @param timepoint the timepoint to set
     */
    public void setTimepoint(Timepoint timepoint) {
        this.timepoint = timepoint;
    }
    
    /**
     * Removes annotation value from definition.
     */
    public void removeValueFromDefinition() {
        if (annotationValue != null) {
            annotationValue.removeValueFromDefinition();
        }
    }

}
