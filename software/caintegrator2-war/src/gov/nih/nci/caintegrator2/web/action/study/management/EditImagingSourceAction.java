/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
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

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class EditImagingSourceAction extends AbstractImagingSourceAction {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_WEB_URL = "https://imaging.nci.nih.gov/ncia";
    private static final String NBIA_WEB_URL_STRING = "https?://.*/ncia$";
    private static final Pattern NBIA_WEB_URL_PATTERN = Pattern.compile(NBIA_WEB_URL_STRING);
    private File imageClinicalMappingFile;
    private String imageClinicalMappingFileFileName;
    private ImageDataSourceMappingTypeEnum mappingType = ImageDataSourceMappingTypeEnum.AUTO;
    private IImagingDataSourceAjaxUpdater updater;
    private NCIAFacade nciaFacade;

    private boolean validateAddSource() {
        validateMappingFile();
        if (StringUtils.isBlank(getImageSourceConfiguration().getCollectionName())) {
            addFieldError("imageSourceConfiguration.collectionName",
                    getText("struts.messages.error.name.required", getArgs("Collection")));
        }
        if (StringUtils.isBlank(getImageSourceConfiguration().getServerProfile().getUrl())) {
            addFieldError("imageSourceConfiguration.serverProfile.url",
                    getText("struts.messages.error.url.required", getArgs("")));
        }
        validateWebUrl();
        if (!checkErrors()) {
            return false;
        }
        checkConnection();
        return checkErrors();
    }

    private boolean validateWebUrl() {
        if (StringUtils.isBlank(getImageSourceConfiguration().getServerProfile().getWebUrl())
                || !NBIA_WEB_URL_PATTERN.matcher(
                        getImageSourceConfiguration().getServerProfile().getWebUrl()).find()) {
            addFieldError("imageSourceConfiguration.serverProfile.webUrl",
                    getText("struts.messages.error.url.invalid",
                            getArgs("http[s]://imaging.url[:port]/ncia")));
        }
        return checkErrors();
    }

    private void checkConnection() {
        ImageDataSourceConfiguration imageSourceToTest = createNewImagingSource();
        try {
            nciaFacade.validateImagingSourceConnection(imageSourceToTest.getServerProfile(), imageSourceToTest
                    .getCollectionName());
        } catch (ConnectionException e) {
            addFieldError("imageSourceConfiguration.serverProfile.url",
                    getText("struts.messages.error.unable.to.connect"));
        } catch (InvalidImagingCollectionException e) {
            addActionError(e.getMessage());
        } catch (Exception e) {
            addActionError(getText("struts.messages.exception.unexpected", getArgs(e.getMessage())));
        }
    }

    private boolean validateMappingFile() {
        if (imageClinicalMappingFile == null && !ImageDataSourceMappingTypeEnum.AUTO.equals(mappingType)) {
            addFieldError("imageClinicalMappingFile", getText("struts.messages.error.imaging.mapping.file.required"));
        }
        return checkErrors();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (getImageSourceConfiguration().getId() == null) {
            getImageSourceConfiguration().getServerProfile().setWebUrl(DEFAULT_WEB_URL);
        }
        if (!getStudyConfiguration().hasLoadedClinicalDataSource()) {
            addActionError(getText("struts.messages.error.study.no.subject"));
        }
        return SUCCESS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fixUrlFromInternetExplorer() {
       if (!StringUtils.isBlank(getImageSourceConfiguration().getServerProfile().getUrl())) {
           getImageSourceConfiguration().getServerProfile().setUrl(
               Cai2Util.fixUrlForEditableSelect(getImageSourceConfiguration().getServerProfile().getUrl()));
       }
       if (!StringUtils.isBlank(getImageSourceConfiguration().getServerProfile().getWebUrl())) {
           getImageSourceConfiguration().getServerProfile().setWebUrl(
               Cai2Util.fixUrlForEditableSelect(getImageSourceConfiguration().getServerProfile().getWebUrl()));
       }
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
            addActionError(getText("struts.messages.error.uploading.unable.to.save"));
            return INPUT;
        }
        getImageSourceConfiguration().setStatus(Status.PROCESSING);
        if (!mapOnly) {
            getStudyManagementService().addImageSourceToStudy(getStudyConfiguration(), getImageSourceConfiguration());
        }
        getStudyManagementService().daoSave(getImageSourceConfiguration());
        setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                LogEntry.getSystemLogSave(getImageSourceConfiguration()));
        updater.runJob(getImageSourceConfiguration().getId(), newMappingFile, mappingType, mapOnly, false);
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
        newProfile.setWebUrl(oldProfile.getWebUrl());
        newProfile.setHostname(oldProfile.getHostname());
        newProfile.setPort(oldProfile.getPort());
        newProfile.setUsername(oldProfile.getUsername());
        newProfile.setPassword(oldProfile.getPassword());
        configuration.setCollectionName(getImageSourceConfiguration().getCollectionName());
        return configuration;
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

    /**
     * Saves the image source only (web URL in particular).
     * @return struts result.
     */
    public String updateImagingSource() {
        if (!validateWebUrl()) {
            return INPUT;
        }
        getStudyManagementService().daoSave(getImageSourceConfiguration());
        return SUCCESS;
    }

    private void storeImageMappingFileName() {
        if (!StringUtils.isBlank(imageClinicalMappingFileFileName)) {
            getImageSourceConfiguration().setMappingFileName(imageClinicalMappingFileFileName);
        } else {
            getImageSourceConfiguration().setMappingFileName(ImageDataSourceConfiguration.AUTOMATIC_MAPPING);
        }
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
