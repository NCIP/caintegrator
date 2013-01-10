/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.translational;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.DateAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.NumericAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class StudySubjectAssignment extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String identifier;
    private Set<ImageSeriesAcquisition> imageStudyCollection = new HashSet<ImageSeriesAcquisition>();
    private Set<SampleAcquisition> sampleAcquisitionCollection = new HashSet<SampleAcquisition>();
    private Set<SubjectAnnotation> subjectAnnotationCollection = new HashSet<SubjectAnnotation>();
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

    /**
     * @return the imageStudyCollection
     */
    public Set<ImageSeriesAcquisition> getImageStudyCollection() {
        return imageStudyCollection;
    }

    /**
     * @param imageStudyCollection the imageStudyCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setImageStudyCollection(Set<ImageSeriesAcquisition> imageStudyCollection) {
        this.imageStudyCollection = imageStudyCollection;
    }

    /**
     * @return the sampleAcquisitionCollection
     */
    public Set<SampleAcquisition> getSampleAcquisitionCollection() {
        return sampleAcquisitionCollection;
    }

    /**
     * @param sampleAcquisitionCollection the sampleAcquisitionCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setSampleAcquisitionCollection(Set<SampleAcquisition> sampleAcquisitionCollection) {
        this.sampleAcquisitionCollection = sampleAcquisitionCollection;
    }

    /**
     * @return the subjectAnnotationCollection
     */
    public Set<SubjectAnnotation> getSubjectAnnotationCollection() {
        return subjectAnnotationCollection;
    }

    /**
     * @param subjectAnnotationCollection the subjectAnnotationCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setSubjectAnnotationCollection(Set<SubjectAnnotation> subjectAnnotationCollection) {
        this.subjectAnnotationCollection = subjectAnnotationCollection;
    }
    
    /**
     * @return whether all collections are empty
     */
    public boolean isObsolete() {
        return (imageStudyCollection.isEmpty()
                && sampleAcquisitionCollection.isEmpty()
                && subjectAnnotationCollection.isEmpty());
    }
    
    /**
     * For a given definition (for a datatype of Date), will return the DateAnnotationValue.
     * @param definition to retrieve value for.
     * @return date annotation value corresponding to this subject and definition.
     */
    public DateAnnotationValue getDateAnnotation(AnnotationDefinition definition) {
        if (!AnnotationTypeEnum.DATE.equals(definition.getDataType())) {
            throw new IllegalArgumentException("Definition must be of Date type.");
        }
        return (DateAnnotationValue) getAnnotation(definition);
    }
    
    /**
     * For a given definition (for a datatype of Numeric), will return the NumericAnnotationValue.
     * @param definition to retrieve value for.
     * @return date annotation value corresponding to this subject and definition.
     */
    public NumericAnnotationValue getNumericAnnotation(AnnotationDefinition definition) {
        if (!AnnotationTypeEnum.NUMERIC.equals(definition.getDataType())) {
            throw new IllegalArgumentException("Definition must be of Numeric type.");
        }
        return (NumericAnnotationValue) getAnnotation(definition);
    }
    
    /**
     * Checks to see if there's a value for the given definition.
     * @param definition to check value for.
     * @return T/F if there is a value for the definition.
     */
    public boolean hasValueForDefinition(AnnotationDefinition definition) {
        for (SubjectAnnotation subjectAnnotation : getSubjectAnnotationCollection()) {
            if (definition.equals(subjectAnnotation.getAnnotationValue().getAnnotationDefinition())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Value as a string for the given definition.
     * @param definition to get value for.
     * @return string for the value.
     */
    public String getAnnotationValueAsString(AnnotationDefinition definition) {
        return getAnnotation(definition).toString();
    }
    
    private AbstractAnnotationValue getAnnotation(AnnotationDefinition definition) {
        for (SubjectAnnotation subjectAnnotation : getSubjectAnnotationCollection()) {
            if (definition.equals(subjectAnnotation.getAnnotationValue().getAnnotationDefinition())) {
                return subjectAnnotation.getAnnotationValue();
            }
        }
        throw new IllegalArgumentException("Subject " + getIdentifier() 
                + " doesn't have an annotation value for " + definition.getDisplayName());
    }


}
