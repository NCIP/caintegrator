/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.imaging;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class ImageSeries extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    
    private String identifier;
    private ImageSeriesAcquisition imageStudy;
    private Set<AbstractAnnotationValue> annotationCollection = new HashSet<AbstractAnnotationValue>();
    private Set<Image> imageCollection = new HashSet<Image>();
    
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
    
    /**
     * @return the imageCollection
     */
    public Set<Image> getImageCollection() {
        return imageCollection;
    }
    
    /**
     * @param imageCollection the imageCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setImageCollection(Set<Image> imageCollection) {
        this.imageCollection = imageCollection;
    }

}
