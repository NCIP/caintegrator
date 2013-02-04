/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.List;

/**
 * Contains configuration information for file based annotation of <code>ImageSeries</code>.
 */
public class ImageAnnotationConfiguration extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    private ImageDataSourceConfiguration imageDataSourceConfiguration;
    private AnnotationFile annotationFile;
    private ServerConnectionProfile aimServerProfile = new ServerConnectionProfile();
    private ImageAnnotationUploadType uploadType = ImageAnnotationUploadType.FILE;
    
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
        if (isAimDataService()) {
            return true;  // TODO - Need implementation
        }
        return getAnnotationFile().isLoadable();
    }

    ImageSeries getImageSeries(String identifier) {
        return getImageDataSourceConfiguration().getStudyConfiguration().getImageSeries(identifier);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isCurrentlyLoaded() {
        if (isAimDataService()) {
            return false;  // TODO - Need implementation
        }
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
    
    /**
     * Gets all annotation descriptors for this image annotation configuration.
     * @return annotation field descriptor list.
     */
    public List<AnnotationFieldDescriptor> getAnnotationDescriptors() {
        return getAnnotationFile().getDescriptors();
    }

    /**
     * @return the aimServerProfile
     */
    public ServerConnectionProfile getAimServerProfile() {
        return aimServerProfile;
    }

    /**
     * @param aimServerProfile the aimServerProfile to set
     */
    public void setAimServerProfile(ServerConnectionProfile aimServerProfile) {
        this.aimServerProfile = aimServerProfile;
    }

    /**
     * @return the uploadType
     */
    public ImageAnnotationUploadType getUploadType() {
        return uploadType;
    }

    /**
     * @param uploadType the uploadType to set
     */
    public void setUploadType(ImageAnnotationUploadType uploadType) {
        this.uploadType = uploadType;
    }
    
    /**
     * @return true if using AIM Data Service
     */
    public boolean isAimDataService() {
        return ImageAnnotationUploadType.AIM.equals(uploadType);
    }
}
