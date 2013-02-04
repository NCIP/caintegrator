/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;

/**
 * Contains configuration information for file based annotation of <code>ImageSeries</code>.
 */
public class ImageAnnotationConfiguration extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    private ImageDataSourceConfiguration imageDataSourceConfiguration;
    private AnnotationFile annotationFile;
    
    /**
     * Creates a new instance.
     */
    public ImageAnnotationConfiguration() {
        super();
    }
    
    ValidationResult validate() {
        return getAnnotationFile().validate();
    }
    
    ImageAnnotationConfiguration(AnnotationFile annotationFile,
            ImageDataSourceConfiguration imageDataSourceConfiguration) {
        this.annotationFile = annotationFile;
        this.imageDataSourceConfiguration = imageDataSourceConfiguration;
    }

    /**
     * @return the annotationFile
     */
    public AnnotationFile getAnnotationFile() {
        return annotationFile;
    }

    /**
     * @param annotationFile the annotationFile to set
     */
    public void setAnnotationFile(AnnotationFile annotationFile) {
        this.annotationFile = annotationFile;
    }

    void reLoadAnnontation() throws ValidationException {
        if (isCurrentlyLoaded()) {
            getAnnotationFile().loadAnnontation(new ImageAnnotationHandler(this));
        }
    }
    
    void loadAnnontation() throws ValidationException {
        getAnnotationFile().loadAnnontation(new ImageAnnotationHandler(this));
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLoadable() {
        return getAnnotationFile().isLoadable();
    }

    ImageSeries getImageSeries(String identifier) {
        return getImageDataSourceConfiguration().getStudyConfiguration().getImageSeries(identifier);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCurrentlyLoaded() {
        return Boolean.valueOf(getAnnotationFile().getCurrentlyLoaded());
    }

    /**
     * @return Description
     */
    public String getDescription() {
        return getAnnotationFile().getFile().getName();
    }

    /**
     * @return the imageDataSourceConfiguration
     */
    public ImageDataSourceConfiguration getImageDataSourceConfiguration() {
        return imageDataSourceConfiguration;
    }

    /**
     * @param imageDataSourceConfiguration the imageDataSourceConfiguration to set
     */
    public void setImageDataSourceConfiguration(ImageDataSourceConfiguration imageDataSourceConfiguration) {
        this.imageDataSourceConfiguration = imageDataSourceConfiguration;
    }
}
