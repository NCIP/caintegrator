package gov.nih.nci.caintegrator2.domain.imaging;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;

import java.util.Collection;

/**
 * 
 */
public class ImageSeries extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    
    private String identifier;
    private ImageSeriesAcquisition imageStudy;
    private Collection<AbstractAnnotationValue> annotationCollection;
    private Collection<Image> imageCollection;
    
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
     * @return the imageStudy
     */
    public ImageSeriesAcquisition getImageStudy() {
        return imageStudy;
    }
    
    /**
     * @param imageStudy the imageStudy to set
     */
    public void setImageStudy(ImageSeriesAcquisition imageStudy) {
        this.imageStudy = imageStudy;
    }
    
    /**
     * @return the annotationCollection
     */
    public Collection<AbstractAnnotationValue> getAnnotationCollection() {
        return annotationCollection;
    }
    
    /**
     * @param annotationCollection the annotationCollection to set
     */
    public void setAnnotationCollection(Collection<AbstractAnnotationValue> annotationCollection) {
        this.annotationCollection = annotationCollection;
    }
    
    /**
     * @return the imageCollection
     */
    public Collection<Image> getImageCollection() {
        return imageCollection;
    }
    
    /**
     * @param imageCollection the imageCollection to set
     */
    public void setImageCollection(Collection<Image> imageCollection) {
        this.imageCollection = imageCollection;
    }

}