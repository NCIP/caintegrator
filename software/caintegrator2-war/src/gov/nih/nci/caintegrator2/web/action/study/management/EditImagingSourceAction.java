/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceMappingTypeEnum;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.ncia.NCIAFacade;
import gov.nih.nci.caintegrator2.web.ajax.IImagingDataSourceAjaxUpdater;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 */
public class EditImagingSourceAction extends AbstractImagingSourceAction {
    
    private static final long serialVersionUID = 1L;

    private File imageAnnotationFile;
    private File imageClinicalMappingFile;
    private String imageAnnotationFileFileName;
    private String imageClinicalMappingFileFileName;
    private ImageDataSourceMappingTypeEnum mappingType = ImageDataSourceMappingTypeEnum.AUTO;
    private IImagingDataSourceAjaxUpdater updater;
    private boolean createNewAnnotationDefinition = false;
    private boolean cancelAction = false;
    private NCIAFacade nciaFacade;

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (!cancelAction) {
            fixUrlFromInternetExplorer();
            prepareValueStack();
        }
    }
    
    private boolean validateAddSource() {
        validateMappingFile();
        if (StringUtils.isBlank(getImageSourceConfiguration().getCollectionName())) {
            addFieldError("imageSourceConfiguration.collectionName", "Collection Name is required.");
        }
        if (StringUtils.isBlank(getImageSourceConfiguration().getServerProfile().getUrl())) {
            addFieldError("imageSourceConfiguration.serverProfile.url", "URL is required.");
        }
        if (!checkErrors()) {
            return false;
        }
        checkConnection();
        return checkErrors();
    }

    private void checkConnection() {
        ImageDataSourceConfiguration imageSourceToTest = createNewImagingSource();
        try {
            nciaFacade.validateImagingSourceConnection(imageSourceToTest.getServerProfile(), imageSourceToTest
                    .getCollectionName());
        } catch (ConnectionException e) {
            addFieldError("imageSourceConfiguration.serverProfile.url", "Unable to connect to the server.");
        } catch (InvalidImagingCollectionException e) {
            addActionError(e.getMessage());
        }
    }

    private boolean validateMappingFile() {
        if (imageClinicalMappingFile == null && !ImageDataSourceMappingTypeEnum.AUTO.equals(mappingType)) {
            addFieldError("imageClinicalMappingFile", "Image to Clinical Mapping File is required");
        }
        return checkErrors();
    }
    
    private boolean validateAddImageAnnotations() {
        if (imageAnnotationFile == null) {
            addFieldError("imageAnnotationFile", "Must specify annotation file");
        }
        return checkErrors();
    }

    private boolean checkErrors() {
        if (!getFieldErrors().isEmpty() || !getActionErrors().isEmpty()) {
            return false;
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        return SUCCESS;
    }
    
    /**
     * {@inheritDoc}
     */
    protected boolean isFileUpload() {
        return true;
    }
    
    /**
     * @return String.
     */
    public String saveImagingSource() {
        if (!validateAddSource()) {
            return INPUT;
        }
        if (getImageSourceConfiguration().getId() != null) {
            ImageDataSourceConfiguration newImagingSource = createNewImagingSource();
            delete();
            setImageSourceConfiguration(newImagingSource);
        }
        getImageSourceConfiguration().getServerProfile().setHostname(
                Cai2Util.getHostNameFromUrl(getImageSourceConfiguration().getServerProfile().getUrl()));
        return runAsynchronousJob(false);
    }

    private String runAsynchronousJob(boolean mapOnly) {
        storeImageMappingFileName();      
        File newMappingFile = null;
        try {
            newMappingFile = storeTemporaryMappingFile();
        } catch (IOException e) {
            addActionError("Unable to save uploaded file.");
            return INPUT;
        }
        getImageSourceConfiguration().setStatus(Status.PROCESSING);
        if (!mapOnly) {
            getStudyManagementService().addImageSourceToStudy(getStudyConfiguration(), getImageSourceConfiguration());
        }
        getStudyManagementService().daoSave(getImageSourceConfiguration());
        setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(), 
                LogEntry.getSystemLogSave(getImageSourceConfiguration()));
        updater.runJob(getImageSourceConfiguration().getId(), newMappingFile, mappingType, mapOnly);
        return SUCCESS;
    }

    private File storeTemporaryMappingFile() throws IOException {
        if (!ImageDataSourceMappingTypeEnum.AUTO.equals(mappingType)) {
            return getStudyManagementService().saveFileToStudyDirectory(getStudyConfiguration(), 
                    getImageClinicalMappingFile());
        }
        return null;
    }
    
    /**
     * Delete an imaging source file.
     * @return string
     */
    public String delete() {
        try {
            setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                    LogEntry.getSystemLogDeleteImagingSource(getImageSourceConfiguration()));
            getStudyManagementService().delete(getStudyConfiguration(), getImageSourceConfiguration());
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            return ERROR;
        }
        return SUCCESS;
    }
    
    private ImageDataSourceConfiguration createNewImagingSource() {
        ImageDataSourceConfiguration configuration = new ImageDataSourceConfiguration();
        ServerConnectionProfile newProfile = configuration.getServerProfile();
        ServerConnectionProfile oldProfile = getImageSourceConfiguration().getServerProfile();
        newProfile.setUrl(oldProfile.getUrl());
        newProfile.setHostname(oldProfile.getHostname());
        newProfile.setPort(oldProfile.getPort());
        newProfile.setUsername(oldProfile.getUsername());
        newProfile.setPassword(oldProfile.getPassword());
        configuration.setCollectionName(getImageSourceConfiguration().getCollectionName());
        return configuration;
    }

    /**
     * Adds annotation file to the imaging source.
     * @return struts result.
     */
    public String addImageAnnotations() {
        if (!validateAddImageAnnotations()) {
            return INPUT;
        }
        try {
            getImageSourceConfiguration().setImageAnnotationConfiguration(
                    getStudyManagementService().addImageAnnotationFile(getImageSourceConfiguration(),
                            getImageAnnotationFile(), getImageAnnotationFileFileName(),
                            createNewAnnotationDefinition));
            setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                    LogEntry.getSystemLogAdd(getImageSourceConfiguration()));
            getStudyManagementService().save(getStudyConfiguration());
        } catch (ValidationException e) {
            addFieldError("imageAnnotationFile", "Invalid file: " + e.getResult().getInvalidMessage());
            return INPUT;
        } catch (IOException e) {
            return ERROR;
        }
        return SUCCESS;
    }
    
    /**
     * Loads image annotations.
     * @return struts result.
     */
    public String loadImageAnnotations() {
        try {
            getStudyManagementService().loadImageAnnotation(getImageSourceConfiguration());
            setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                    LogEntry.getSystemLogLoad(getImageSourceConfiguration()));
        } catch (ValidationException e) {
            addActionError(e.getResult().getInvalidMessage());
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * Maps the image series acquisition to clinical subjects.
     * @return struts result.
     */
    public String mapImagingSource() {
        if (!validateMappingFile()) {
            return INPUT;
        }
        return runAsynchronousJob(true);
    }

    private void storeImageMappingFileName() {
        if (!StringUtils.isBlank(imageClinicalMappingFileFileName)) {
            getImageSourceConfiguration().setMappingFileName(imageClinicalMappingFileFileName);
        } else {
            getImageSourceConfiguration().setMappingFileName(ImageDataSourceConfiguration.AUTOMATIC_MAPPING);
        }
    }
    
    /**
     * This is because the editable-select for internet explorer submits an extra URL after comma.
     * ex: "http://url, http://url" instead of just "http://url".
     */
    private void fixUrlFromInternetExplorer() {
       if (!StringUtils.isBlank(getImageSourceConfiguration().getServerProfile().getUrl())) {
           getImageSourceConfiguration().getServerProfile().setUrl(
                    Pattern.compile(",\\s.*").matcher(getImageSourceConfiguration().getServerProfile().getUrl())
                            .replaceAll(""));
       }
    }

    /**
     * @return the Imaging File
     */
    public File getImageAnnotationFile() {
        return imageAnnotationFile;
    }

    /**
     * @param imageAnnotationFile
     *            the imageAnnotationFile to set
     */
    public void setImageAnnotationFile(File imageAnnotationFile) {
        this.imageAnnotationFile = imageAnnotationFile;
    }

    /**
     * @return ImagingFileFileName
     */
    public String getImageAnnotationFileFileName() {
        return imageAnnotationFileFileName;
    }

    /**
     * @param imageAnnotationFileFileName
     *            the ImagingFileFileName to set
     */
    public void setImageAnnotationFileFileName(String imageAnnotationFileFileName) {
        this.imageAnnotationFileFileName = imageAnnotationFileFileName;
    }

    /**
     * @return the imageClinicalMappingFile
     */
    public File getImageClinicalMappingFile() {
        return imageClinicalMappingFile;
    }

    /**
     * @param imageClinicalMappingFile
     *            the imageClinicalMappingFile to set
     */
    public void setImageClinicalMappingFile(File imageClinicalMappingFile) {
        this.imageClinicalMappingFile = imageClinicalMappingFile;
    }

    /**
     * @return the mappingType
     */
    public String getMappingType() {
        if (mappingType != null) {
            return mappingType.getValue();
        }
        return "";
    }

    /**
     * @param mappingType the mappingType to set
     */
    public void setMappingType(String mappingType) {
        if (StringUtils.isBlank(mappingType)) {
            this.mappingType = null;
        } else {
            this.mappingType = ImageDataSourceMappingTypeEnum.getByValue(mappingType);
        }
    }

    /**
     * @return the imageClinicalMappingFileFileName
     */
    public String getImageClinicalMappingFileFileName() {
        return imageClinicalMappingFileFileName;
    }

    /**
     * @param imageClinicalMappingFileFileName the imageClinicalMappingFileFileName to set
     */
    public void setImageClinicalMappingFileFileName(String imageClinicalMappingFileFileName) {
        this.imageClinicalMappingFileFileName = imageClinicalMappingFileFileName;
    }

    /**
     * @return the updater
     */
    public IImagingDataSourceAjaxUpdater getUpdater() {
        return updater;
    }

    /**
     * @param updater the updater to set
     */
    public void setUpdater(IImagingDataSourceAjaxUpdater updater) {
        this.updater = updater;
    }

    /**
     * @return available NBIA services.
     */
    public Set<String> getNbiaServices() {
        return GridDiscoveryServiceJob.getGridNbiaServices().keySet();
    }
    
    /**
     * @return the cancelAction
     */
    public boolean isCancelAction() {
        return cancelAction;
    }

    /**
     * @param cancelAction the cancelAction to set
     */
    public void setCancelAction(boolean cancelAction) {
        this.cancelAction = cancelAction;
    }

    /**
     * @return the createNewAnnotationDefinition
     */
    public boolean isCreateNewAnnotationDefinition() {
        return createNewAnnotationDefinition;
    }

    /**
     * @param createNewAnnotationDefinition the createNewAnnotationDefinition to set
     */
    public void setCreateNewAnnotationDefinition(boolean createNewAnnotationDefinition) {
        this.createNewAnnotationDefinition = createNewAnnotationDefinition;
    }

    /**
     * @return the nciaFacade
     */
    public NCIAFacade getNciaFacade() {
        return nciaFacade;
    }

    /**
     * @param nciaFacade the nciaFacade to set
     */
    public void setNciaFacade(NCIAFacade nciaFacade) {
        this.nciaFacade = nciaFacade;
    }

}
