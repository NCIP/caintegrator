/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class SampleAcquisition extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private StudySubjectAssignment assignment;
    private Sample sample;
    private Set<AbstractAnnotationValue> annotationCollection = new HashSet<AbstractAnnotationValue>();;
    private Timepoint timepoint;
    
    /**
     * @return the assignment
     */
    public StudySubjectAssignment getAssignment() {
        return assignment;
    }
    
    /**
     * @param assignment the assignment to set
     */
    public void setAssignment(StudySubjectAssignment assignment) {
        this.assignment = assignment;
    }
    
    /**
     * @return the sample
     */
    public Sample getSample() {
        return sample;
    }
    
    /**
     * @param sample the sample to set
     */
    public void setSample(Sample sample) {
        this.sample = sample;
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
     * @return the study
     */
    public Study getStudy() {
        if (getAssignment() != null) {
            return getAssignment().getStudy();
        } else {
            return null;
        }
    }

    /**
     * @return the annotationCollection
     */
    public Set<AbstractAnnotationValue> getAnnotationCollection() {
        return annotationCollection;
    }

    /**
     * @param annotationCollection the annotationCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setAnnotationCollection(Set<AbstractAnnotationValue> annotationCollection) {
        this.annotationCollection = annotationCollection;
    }

}
