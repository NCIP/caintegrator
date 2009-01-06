package gov.nih.nci.caintegrator2.domain.imaging;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;

import java.util.Collection;

/**
 * 
 */
public class Image extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String identifier;
    private ImageSeries series;
    private Collection<AbstractAnnotationValue> annotationCollection;
    
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
     * @return the series
     */
    public ImageSeries getSeries() {
        return series;
    }
    
    /**
     * @param series the series to set
     */
    public void setSeries(ImageSeries series) {
        this.series = series;
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

}