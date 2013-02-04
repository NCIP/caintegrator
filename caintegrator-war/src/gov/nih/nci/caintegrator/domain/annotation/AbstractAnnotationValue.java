/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;

import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.imaging.Image;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeries;

/**
 * 
 */
public abstract class AbstractAnnotationValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private SubjectAnnotation subjectAnnotation;
    private AnnotationDefinition annotationDefinition;
    private ImageSeries imageSeries;
    private SampleAcquisition sampleAcquisition;
    private Image image;
    
    /**
     * Default empty constructor.
     */
    public AbstractAnnotationValue() { 
        // Empty Constructor
    }
    
    /**
     * Converts the given annotation value to a a new object, as well as moves it to the new 
     * annotationDefinition.
     * @param oldValue to use to create this object.
     * @param annotationDefinition is the new definition to move value to.
     */
    public AbstractAnnotationValue(AbstractAnnotationValue oldValue, AnnotationDefinition annotationDefinition) {
        convertAnnotationDefinition(oldValue, annotationDefinition);
        convertOldValue(oldValue);
    }
    
    private void convertAnnotationDefinition(AbstractAnnotationValue oldValue, 
            AnnotationDefinition newAnnotationDefinition) {
        oldValue.getAnnotationDefinition().getAnnotationValueCollection().remove(oldValue);
        this.annotationDefinition = newAnnotationDefinition;
        annotationDefinition.getAnnotationValueCollection().add(this);
    }

    private void convertOldValue(AbstractAnnotationValue value) {
        subjectAnnotation = value.getSubjectAnnotation();
        if (subjectAnnotation != null) {
            subjectAnnotation.setAnnotationValue(this);
        }
        imageSeries = value.getImageSeries();
        if (imageSeries != null) {
            imageSeries.getAnnotationCollection().remove(value);
            imageSeries.getAnnotationCollection().add(this);
        }
        sampleAcquisition = value.getSampleAcquisition();
        if (sampleAcquisition != null) {
            sampleAcquisition.getAnnotationCollection().remove(value);
            sampleAcquisition.getAnnotationCollection().add(this);
        }
        image = value.getImage();
        if (image != null) {
            image.getAnnotationCollection().remove(value);
            image.getAnnotationCollection().add(this);
        }
    }
    
    /**
     * Removes value from definition.
     */
    public void removeValueFromDefinition() {
        if (annotationDefinition != null) {
            annotationDefinition.getAnnotationValueCollection().remove(this);
        }
    }
    
    /**
     * @return the subjectAnnotation
     */
    public SubjectAnnotation getSubjectAnnotation() {
        return subjectAnnotation;
    }
    
    /**
     * @param subjectAnnotation the subjectAnnotation to set
     */
    public void setSubjectAnnotation(SubjectAnnotation subjectAnnotation) {
        this.subjectAnnotation = subjectAnnotation;
    }
    
    /**
     * @return the annotationDefinition
     */
    public AnnotationDefinition getAnnotationDefinition() {
        return annotationDefinition;
    }
    
    /**
     * @param annotationDefinition the annotationDefinition to set
     */
    public void setAnnotationDefinition(AnnotationDefinition annotationDefinition) {
        this.annotationDefinition = annotationDefinition;
    }
    
    /**
     * @return the imageSeries
     */
    public ImageSeries getImageSeries() {
        return imageSeries;
    }
    
    /**
     * @param imageSeries the imageSeries to set
     */
    public void setImageSeries(ImageSeries imageSeries) {
        this.imageSeries = imageSeries;
    }
    
    /**
     * @return the sampleAcquisition
     */
    public SampleAcquisition getSampleAcquisition() {
        return sampleAcquisition;
    }
    
    /**
     * @param sampleAcquisition the sampleAcquisition to set
     */
    public void setSampleAcquisition(SampleAcquisition sampleAcquisition) {
        this.sampleAcquisition = sampleAcquisition;
    }
    
    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }
    
    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Method to retrieve valid annotation type for the value.
     * @return AnnotationType associated with value.
     */
    public abstract AnnotationTypeEnum getValidAnnotationType();
    
    /**
     * This function does 2 things:
     * Converts the current annotation value to the data type of the given AnnotationDefinition.
     * *If the type is the same as the current type then it will not convert.
     * *If the type is different, it will create a new AnnotationValue and convert itself into that type.
     * Moves the value to the given AnnotationDefinition.
     * *If the annotationDefinition is the same as the current annotationDefinition it will not move
     * 
     * NOTE: If the given annotationDefinition is different than current AnnotationDefinition then the current value 
     * will be removed from the old annotationDefinition (and subsequently deleted).
     * @param newAnnotationDefinition to move the value to.
     * @throws ValidationException if conversion is not valid.
     */
    public abstract void convertAnnotationValue(AnnotationDefinition newAnnotationDefinition) 
    throws ValidationException;

}
