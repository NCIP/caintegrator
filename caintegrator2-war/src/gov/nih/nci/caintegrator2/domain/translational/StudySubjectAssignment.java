package gov.nih.nci.caintegrator2.domain.translational;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;

import java.util.Collection;

/**
 * 
 */
public class StudySubjectAssignment extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String identifier;
    private Collection<ImageSeriesAcquisition> imageStudyCollection;
    private Collection<SampleAcquisition> sampleAcquisitionCollection;
    private Collection<SubjectAnnotation> subjectAnnotationCollection;
    private Study study;
    private Subject subject;
    
    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * @return the imageStudyCollection
     */
    public Collection<ImageSeriesAcquisition> getImageStudyCollection() {
        return imageStudyCollection;
    }
    
    /**
     * @param imageStudyCollection the imageStudyCollection to set
     */
    public void setImageStudyCollection(Collection<ImageSeriesAcquisition> imageStudyCollection) {
        this.imageStudyCollection = imageStudyCollection;
    }
    
    /**
     * @return the sampleAcquisitionCollection
     */
    public Collection<SampleAcquisition> getSampleAcquisitionCollection() {
        return sampleAcquisitionCollection;
    }
    
    /**
     * @param sampleAcquisitionCollection the sampleAcquisitionCollection to set
     */
    public void setSampleAcquisitionCollection(Collection<SampleAcquisition> sampleAcquisitionCollection) {
        this.sampleAcquisitionCollection = sampleAcquisitionCollection;
    }
    
    /**
     * @return the subjectAnnotationCollection
     */
    public Collection<SubjectAnnotation> getSubjectAnnotationCollection() {
        return subjectAnnotationCollection;
    }
    
    /**
     * @param subjectAnnotationCollection the subjectAnnotationCollection to set
     */
    public void setSubjectAnnotationCollection(Collection<SubjectAnnotation> subjectAnnotationCollection) {
        this.subjectAnnotationCollection = subjectAnnotationCollection;
    }
    
    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }
    
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    
    /**
     * @return the subject
     */
    public Subject getSubject() {
        return subject;
    }
    
    /**
     * @param subject the subject to set
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}