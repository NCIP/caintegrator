/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Contains configuration information for retrieving images, etc. from NCIA.
 */
public class ImageDataSourceConfiguration extends AbstractCaIntegrator2Object implements TimeStampable {
    /**
     * For the "Automatic" mapping.
     */
    public static final String AUTOMATIC_MAPPING = "Automatic";
    private static final long serialVersionUID = 1L;
    private StudyConfiguration studyConfiguration;
    private ImageAnnotationConfiguration imageAnnotationConfiguration;
    private ServerConnectionProfile serverProfile = new ServerConnectionProfile();
    private List<ImageSeriesAcquisition> imageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
    private String collectionName;
    private String mappingFileName;
    private Status status;
    private String statusDescription;
    private Date lastModifiedDate;
    
    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }

    /**
     * @return the serverProfile
     */
    public ServerConnectionProfile getServerProfile() {
        return serverProfile;
    }

    /**
     * @param serverProfile the serverProfile to set
     */
    @SuppressWarnings("unused")
    private void setServerProfile(ServerConnectionProfile serverProfile) {
        this.serverProfile = serverProfile;
    }

    /**
     * @return the collectionName
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * @param collectionName the collectionName to set
     */
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    /**
     * @return the imageSeriesAcquisitions
     */
    public List<ImageSeriesAcquisition> getImageSeriesAcquisitions() {
        return imageSeriesAcquisitions;
    }

    /**
     * @param imageSeriesAcquisitions the imageSeriesAcquisitions to set
     */
    @SuppressWarnings("unused")
    private void setImageSeriesAcquisitions(List<ImageSeriesAcquisition> imageSeriesAcquisitions) {
        this.imageSeriesAcquisitions = imageSeriesAcquisitions;
    }

    /**
     * @return the mappingFileName
     */
    public String getMappingFileName() {
        return mappingFileName;
    }

    /**
     * @param mappingFileName the fileName to set
     */
    public void setMappingFileName(String mappingFileName) {
        this.mappingFileName = mappingFileName;
    }

    /**
     * @return the imageAnnotationConfiguration
     */
    public ImageAnnotationConfiguration getImageAnnotationConfiguration() {
        return imageAnnotationConfiguration;
    }

    /**
     * @param imageAnnotationConfiguration the imageAnnotationConfiguration to set
     */
    public void setImageAnnotationConfiguration(ImageAnnotationConfiguration imageAnnotationConfiguration) {
        this.imageAnnotationConfiguration = imageAnnotationConfiguration;
    }
    
    /**
     * List of image series acquisitions that are mapped for this source.
     * @return mapped image series acquisitions.
     */
    public List<ImageSeriesAcquisition> getMappedImageSeriesAcquisitions() {
        List<ImageSeriesAcquisition> mappedImageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
        for (ImageSeriesAcquisition acquisition : imageSeriesAcquisitions) {
            if (acquisition.getAssignment() != null) {
                mappedImageSeriesAcquisitions.add(acquisition);
            }
        }
        return mappedImageSeriesAcquisitions;
    }
    
    /**
     * List of image series acquisitions that are unmapped for this source.
     * @return unmapped image series acquisitions.
     */
    public List<ImageSeriesAcquisition> getUnmappedImageSeriesAcquisitions() {
        List<ImageSeriesAcquisition> unmappedImageSeriesAcquisitions = new ArrayList<ImageSeriesAcquisition>();
        for (ImageSeriesAcquisition acquisition : imageSeriesAcquisitions) {
            if (acquisition.getAssignment() == null) {
                unmappedImageSeriesAcquisitions.add(acquisition);
            }
        }
        return unmappedImageSeriesAcquisitions;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayableLastModifiedDate() {
        return DateUtil.getDisplayableTimeStamp(lastModifiedDate); 
    }

    /**
     * Delete mapping file and set the status to NOT_MAPPED.
     */
    public void deleteMappingFile() {
        setMappingFileName(null);
        getImageSeriesAcquisitions().clear();
        setStatus(Status.NOT_MAPPED);
    }

}
