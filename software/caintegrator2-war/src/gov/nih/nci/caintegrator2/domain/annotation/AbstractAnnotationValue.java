package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.Image;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;

/**
 * 
 */
public abstract class AbstractAnnotationValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private SubjectAnnotation subjectAnnotation;
    private AnnotationDefinition annotationDefinition;
    private ImageSeries imageSeries;
    private SampleAcquisition sampleAcquisition;
    private AbstractPermissibleValue boundedValue;
    private Image image;
    
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
     * @return the boundedValue
     */
    public AbstractPermissibleValue getBoundedValue() {
        return boundedValue;
    }
    
    /**
     * @param boundedValue the boundedValue to set
     */
    public void setBoundedValue(AbstractPermissibleValue boundedValue) {
        this.boundedValue = boundedValue;
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

}