/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.ImageAnnotationUploadType;
import gov.nih.nci.caintegrator2.application.study.LogEntry;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.aim.AIMFacade;
import gov.nih.nci.caintegrator2.web.ajax.IImagingDataSourceAjaxUpdater;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class EditImagingSourceAnnotationsAction extends AbstractImagingSourceAction {

    private static final long serialVersionUID = 1L;
    private List<DisplayableAnnotationFieldDescriptor> displayableFields =
        new ArrayList<DisplayableAnnotationFieldDescriptor>();
    private final List<AnnotationGroup> selectableAnnotationGroups = new ArrayList<AnnotationGroup>();
    private final Map<String, AnnotationGroup> annotationGroupNameToGroupMap = new HashMap<String, AnnotationGroup>();
    private File imageAnnotationFile;
    private String imageAnnotationFileFileName;
    private ImageAnnotationUploadType uploadType = ImageAnnotationUploadType.FILE;
    private ServerConnectionProfile aimServerProfile = new ServerConnectionProfile();
    private boolean createNewAnnotationDefinition = false;
    private AIMFacade aimFacade;
    private IImagingDataSourceAjaxUpdater updater;
    private boolean aimReload = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (getImageSourceConfiguration().getImageAnnotationConfiguration() != null) {
            if (getImageSourceConfiguration().getImageAnnotationConfiguration().isAimDataService()) {
                aimReload = true;
            } else {
                setupAnnotationGroups();
                setupDisplayableFields();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (!isAimReload()) {
            fixUrlFromInternetExplorer();
        }
        prepareValueStack();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fixUrlFromInternetExplorer() {
       if (!StringUtils.isBlank(getAimServerProfile().getUrl())) {
           getAimServerProfile().setUrl(Cai2Util.fixUrlForEditableSelect(getAimServerProfile().getUrl()));
       }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFileUpload() {
        return true;
    }

    private void setupAnnotationGroups() {
        selectableAnnotationGroups.clear();
        List<AnnotationGroup> sortedAnnotationGroups = getStudy().getSortedAnnotationGroups();
        for (AnnotationGroup group : sortedAnnotationGroups) {
            group = getStudyManagementService().getRefreshedEntity(group);
            selectableAnnotationGroups.add(group);
            annotationGroupNameToGroupMap.put(group.getName(), group);
        }
    }

    private void setupDisplayableFields() {
        displayableFields.clear();
        for (FileColumn fileColumn : getImageSourceConfiguration().getImageAnnotationConfiguration().
                getAnnotationFile().getColumns()) {
            displayableFields.add(new DisplayableAnnotationFieldDescriptor(fileColumn));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        return SUCCESS;
    }

    /**
     * Save action.
     * @return struts value.
     */
    public String save() {
        for (DisplayableAnnotationFieldDescriptor displayableFieldDescriptor : displayableFields) {
            if (displayableFieldDescriptor.isGroupChanged()) {
                displayableFieldDescriptor.getFieldDescriptor().switchAnnotationGroup(
                        annotationGroupNameToGroupMap.get(displayableFieldDescriptor.getAnnotationGroupName()));
            }
        }
        setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                LogEntry.getSystemLogSave(getImageSourceConfiguration()));
        getStudyManagementService().save(getStudyConfiguration());
        return SUCCESS;
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
            if (ImageAnnotationUploadType.FILE.equals(uploadType)) {
                getImageSourceConfiguration().setImageAnnotationConfiguration(
                    getStudyManagementService().addImageAnnotationFile(getImageSourceConfiguration(),
                            getImageAnnotationFile(), getImageAnnotationFileFileName(),
                            createNewAnnotationDefinition));
                setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                        LogEntry.getSystemLogAdd(getImageSourceConfiguration()));
                getStudyManagementService().save(getStudyConfiguration());
            } else {
                return loadAimAnnotation();
            }
        } catch (ValidationException e) {
            addFieldError("imageAnnotationFile", getText("struts.messages.exception.invalid.file", new String[] {e
                    .getResult().getInvalidMessage() }));
            return INPUT;
        } catch (Exception e) {
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * Load the image annotation from AIM Data Service.
     * @return struts result.
     */
    public String loadAimAnnotation() {
        if (getImageSourceConfiguration().getImageAnnotationConfiguration() == null) {
            getStudyManagementService().addAimAnnotationSource(getAimServerProfile(),
                getImageSourceConfiguration());
        }
        getImageSourceConfiguration().setStatus(Status.PROCESSING);
        getStudyManagementService().daoSave(getImageSourceConfiguration());
        setStudyLastModifiedByCurrentUser(getImageSourceConfiguration(),
                LogEntry.getSystemLogSave(getImageSourceConfiguration()));
        getStudyManagementService().save(getStudyConfiguration());
        updater.runJob(getImageSourceConfiguration().getId(), null, null, false, true);
        return "loadingAimAnnotation";
    }

    private boolean validateAddImageAnnotations() {
        if (ImageAnnotationUploadType.FILE.equals(uploadType) && imageAnnotationFile == null) {
            addFieldError("imageAnnotationFile", getText("struts.messages.error.imaging.annotation.file.required"));
        } else if (ImageAnnotationUploadType.AIM.equals(uploadType)) {
            validateAimConnection();
        }
        return checkErrors();
    }

    private void validateAimConnection() {
        if (StringUtils.isBlank(getAimServerProfile().getUrl())) {
            addFieldError("aimServerProfile.url", getText("struts.messages.error.url.required",
                    getArgs("AIM")));
        }
        if (checkErrors()) {
            try {
                getAimFacade().validateAimConnection(getAimServerProfile());
            } catch (ConnectionException e) {
                addFieldError("aimServerProfile.url", getText("struts.messages.error.unable.to.connect"));
            }
        }
    }

    /**
     * @return the displayableFields
     */
    public List<DisplayableAnnotationFieldDescriptor> getDisplayableFields() {
        return displayableFields;
    }


    /**
     * @param displayableFields the displayableFields to set
     */
    public void setDisplayableFields(List<DisplayableAnnotationFieldDescriptor> displayableFields) {
        this.displayableFields = displayableFields;
    }


    /**
     * @return the selectableAnnotationGroups
     */
    public List<AnnotationGroup> getSelectableAnnotationGroups() {
        return selectableAnnotationGroups;
    }

    /**
     * @return available NBIA services.
     */
    public Set<String> getAimServices() {
        return GridDiscoveryServiceJob.getGridAimServices().keySet();
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
     * @return the uploadType
     */
    public String getUploadType() {
        return uploadType.getValue();
    }

    /**
     * @param uploadType the uploadType to set
     */
    public void setUploadType(String uploadType) {
        this.uploadType = ImageAnnotationUploadType.getByValue(uploadType);
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
     * @return the aimFacade
     */
    public AIMFacade getAimFacade() {
        return aimFacade;
    }

    /**
     * @param aimFacade the aimFacade to set
     */
    public void setAimFacade(AIMFacade aimFacade) {
        this.aimFacade = aimFacade;
    }

    /**
     * @return true/false to disable the annotation file field.
     */
    public boolean isAnnotationFileDisable() {
        return ImageAnnotationUploadType.AIM.equals(uploadType);
    }

    /**
     * @return true/false to disable the AIM server info fields.
     */
    public boolean isAimDisable() {
        return ImageAnnotationUploadType.FILE.equals(uploadType);
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
     * @return the aimReload
     */
    public boolean isAimReload() {
        return aimReload;
    }

    /**
     * @param aimReload the aimReload to set
     */
    public void setAimReload(boolean aimReload) {
        this.aimReload = aimReload;
    }

    /**
     *
     * @return css style value.
     */
    public String getFileInputCssStyle() {
        return ImageAnnotationUploadType.FILE.getValue().equals(uploadType.getValue())
            ? "display: block;" : "display: none;";
    }

    /**
     *
     * @return css style value.
     */
    public String getAimInputCssStyle() {
        return ImageAnnotationUploadType.AIM.getValue().equals(uploadType.getValue())
            ? "display: block;" : "display: none;";
    }


}
