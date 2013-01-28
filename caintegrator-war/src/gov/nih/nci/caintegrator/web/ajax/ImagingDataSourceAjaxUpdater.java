/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ImageDataSourceMappingTypeEnum;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;

import java.io.File;

import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.
 */
public class ImagingDataSourceAjaxUpdater extends AbstractDwrAjaxUpdater implements IImagingDataSourceAjaxUpdater {

    private static final String STATUS_TABLE = "imagingSourceJobStatusTable";
    private static final String JOB_HOST_NAME = "imagingSourceHostName_";
    private static final String JOB_COLLECTION_NAME = "imagingSourceCollectionName_";
    private static final String JOB_DEPLOYMENT_STATUS = "imagingSourceStatus_";
    private static final String JOB_LAST_MODIFIED_DATE = "imagingSourceLastModified_";
    private static final String JOB_EDIT_URL = "imagingSourceEditUrl_";
    private static final String JOB_EDIT_ANNOTATIONS_URL = "imagingSourceEditAnnotationsUrl_";
    private static final String JOB_LOAD_ANNOTATIONS_URL = "imagingSourceLoadAnnotationsUrl_";
    private static final String JOB_DELETE_URL = "imagingSourceDeleteUrl_";
    private static final String JOB_FILE_DESCRIPTION = "imagingSourceFileDescription_";
    private static final String JOB_ACTION_BAR1 = "imagingSourceActionBar1_";
    private static final String JOB_ACTION_BAR2 = "imagingSourceActionBar2_";
    private static final String JOB_ACTION_BAR3 = "imagingSourceActionBar3_";
    private static final String IMAGING_SOURCES_LOADER = "imagingSourceLoader";

    private StudyManagementService studyManagementService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeDynamicTable(DisplayableUserWorkspace workspace) {
        String username = workspace.getUserWorkspace().getUsername();
        try {
            StudyConfiguration studyConfiguration = workspace.getCurrentStudyConfiguration();
            if (studyConfiguration != null && studyConfiguration.getId() != null) {
                studyConfiguration = studyManagementService.getRefreshedEntity(studyConfiguration);
                int counter = 0;
                for (ImageDataSourceConfiguration imagingSource : studyConfiguration.getImageDataSources()) {
                    getDwrUtil(username).addRows(STATUS_TABLE,
                                                    createRow(imagingSource),
                                                    retrieveRowOptions(counter));
                    updateJobStatus(username, imagingSource);
                    counter++;
                }
            }
        } finally {
            getDwrUtil(username).setValue(IMAGING_SOURCES_LOADER, "");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void associateJobWithSession(DwrUtilFactory dwrUtilFactory, String username, Util util) {
        dwrUtilFactory.associateImagingDataSourceWithSession(username, util);
    }

    private String[][] createRow(ImageDataSourceConfiguration imagingSource) {
        String[][] rowString = new String[1][6];
        String id = imagingSource.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_HOST_NAME + id + endSpan;
        rowString[0][1] = startSpan + JOB_COLLECTION_NAME + id + endSpan;
        rowString[0][2] = startSpan + JOB_FILE_DESCRIPTION + id + endSpan;
        rowString[0][3] = startSpan + JOB_DEPLOYMENT_STATUS + id + endSpan;
        rowString[0][4] = startSpan + JOB_LAST_MODIFIED_DATE + id + endSpan;
        rowString[0][5] = startSpan + JOB_EDIT_URL + id + endSpan
                          + startSpan + JOB_EDIT_ANNOTATIONS_URL + id + endSpan
                          + startSpan + JOB_LOAD_ANNOTATIONS_URL + id + endSpan
                          + startSpan + JOB_DELETE_URL + id + endSpan;

        return rowString;
    }

    /**
     * {@inheritDoc}
     */
    public void runJob(Long imagingSourceId,
                       File imageClinicalMappingFile,
                       ImageDataSourceMappingTypeEnum mappingType,
                       boolean mapOnly, boolean loadAimAnnotation) {
        Thread imagingSourceRunner = new Thread(new ImagingDataSourceAjaxRunner(this, imagingSourceId,
                imageClinicalMappingFile, mappingType, mapOnly, loadAimAnnotation));
        imagingSourceRunner.start();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Util getDwrUtil(String username) {
        return getDwrUtilFactory().retrieveImagingDataSourceUtil(username);
    }

    /**
     * Saves imagingSource to database, then updates the status to JSP.
     * @param username to update the status to.
     * @param imagingSource to save and update.
     */
    public void saveAndUpdateJobStatus(String username, ImageDataSourceConfiguration imagingSource) {
        getStudyManagementService().daoSave(imagingSource);
        updateJobStatus(username, imagingSource.getId());
    }

    /**
     * Updates imagingSource status.
     * @param username to update the status to.
     * @param sourceId the id of the imaging source to update to update.
     */
    public void updateJobStatus(String username, Long sourceId) {
        ImageDataSourceConfiguration imagingSource =
                getStudyManagementService().getRefreshedImageSource(sourceId);
        updateJobStatus(username, imagingSource);
    }

    /**
     * Updates imagingSource status.
     * @param username to update the status to.
     * @param imagingSource the the imaging source to update.
     */
    private void updateJobStatus(String username, ImageDataSourceConfiguration imagingSource) {
        Util utilThis = getDwrUtil(username);
        String imagingSourceId = imagingSource.getId().toString();
        utilThis.setValue(JOB_HOST_NAME + imagingSourceId,
                            imagingSource.getServerProfile().getHostname());
        utilThis.setValue(JOB_COLLECTION_NAME + imagingSourceId,
                            imagingSource.getCollectionName());
        updateRowFileDescriptions(utilThis, imagingSource, imagingSourceId);
        utilThis.setValue(JOB_DEPLOYMENT_STATUS + imagingSourceId, getStatusMessage(imagingSource.getStatus()));
        utilThis.setValue(JOB_LAST_MODIFIED_DATE + imagingSourceId,
                imagingSource.getDisplayableLastModifiedDate());
        updateRowActions(imagingSource, utilThis, imagingSourceId);
    }

    private void updateRowFileDescriptions(Util utilThis, ImageDataSourceConfiguration imagingSource,
            String imagingSourceId) {
        StringBuffer fileDescriptionString = new StringBuffer();
        if (isAimDataService(imagingSource)) {
            fileDescriptionString.append("<i>AIM Service URL: </i>");
            fileDescriptionString
                    .append(imagingSource.getImageAnnotationConfiguration().getAimServerProfile().getUrl());
        } else {
            updateFileDescriptionString(imagingSource, fileDescriptionString);
        }
        fileDescriptionString.append("<br><i>Mapping File: </i>");
        fileDescriptionString.append(imagingSource.getMappingFileName());
        fileDescriptionString.append(" (");
        fileDescriptionString.append(imagingSource.getMappedImageSeriesAcquisitions().size());
        fileDescriptionString.append(" mapped images)");
        utilThis.setValue(JOB_FILE_DESCRIPTION + imagingSourceId, fileDescriptionString.toString());
    }

    private boolean isAimDataService(ImageDataSourceConfiguration imagingSource) {
        return imagingSource.getImageAnnotationConfiguration() != null
             && imagingSource.getImageAnnotationConfiguration().isAimDataService();
    }

    private void updateFileDescriptionString(ImageDataSourceConfiguration imagingSource,
            StringBuffer fileDescriptionString) {
        fileDescriptionString.append("<i>Annotation File: </i>");
        if (imagingSource.getImageAnnotationConfiguration() != null) {
            fileDescriptionString.append(imagingSource.getImageAnnotationConfiguration().
                    getAnnotationFile().getFile().getName());
        } else {
            fileDescriptionString.append("None");
        }
    }

    private void updateRowActions(ImageDataSourceConfiguration imagingSource, Util utilThis, String imagingSourceId) {
        if (!Status.PROCESSING.equals(imagingSource.getStatus())) { // Not processing gets actions
            addNonProcessingActions(imagingSource, utilThis, imagingSourceId);
        } else { // Processing has no actions.
            utilThis.setValue(JOB_EDIT_URL + imagingSourceId, "");
            utilThis.setValue(JOB_EDIT_ANNOTATIONS_URL + imagingSourceId, "");
            utilThis.setValue(JOB_LOAD_ANNOTATIONS_URL + imagingSourceId, "");
            utilThis.setValue(JOB_DELETE_URL + imagingSourceId, "");
            utilThis.setValue(JOB_ACTION_BAR1 + imagingSourceId, "");
            utilThis.setValue(JOB_ACTION_BAR2 + imagingSourceId, "");
            utilThis.setValue(JOB_ACTION_BAR3 + imagingSourceId, "");
        }
    }

    private void addNonProcessingActions(ImageDataSourceConfiguration imagingSource, Util utilThis,
            String imagingSourceId) {
        String jobActionBarString = "&nbsp;";
        if (!Status.ERROR.equals(imagingSource.getStatus())) {
            addNonErrorActions(imagingSource, utilThis, imagingSourceId, jobActionBarString);
        }
        utilThis.setValue(JOB_EDIT_URL + imagingSourceId,
                retrieveUrl(imagingSource, "editImagingSource", "Edit", "edit", false),
                false);
        utilThis.setValue(JOB_ACTION_BAR1 + imagingSourceId, jobActionBarString, false);
        if (isAimDataService(imagingSource) && !Status.LOADED.equals(imagingSource.getStatus())) {
            utilThis.setValue(JOB_LOAD_ANNOTATIONS_URL + imagingSourceId,
                    retrieveUrl(imagingSource, "loadAimAnnotation", "Load AIM Annotations", "load", false),
                    false);
            utilThis.setValue(JOB_ACTION_BAR2 + imagingSourceId, "");
        }
        utilThis.setValue(JOB_DELETE_URL + imagingSourceId,
                retrieveUrl(imagingSource, "deleteImagingSource", "Delete", "delete", true),
                false);
    }

    private void addNonErrorActions(ImageDataSourceConfiguration imagingSource,
                    Util utilThis, String imagingSourceId, String jobActionBarString) {
        addEditAnnotationAction(imagingSource, utilThis, imagingSourceId, jobActionBarString);
        if (imagingSource.getImageAnnotationConfiguration() != null) {
            addLoadAnnotationAction(imagingSource, utilThis, imagingSourceId, jobActionBarString);
        }

    }

    private void addEditAnnotationAction(ImageDataSourceConfiguration imagingSource, Util utilThis,
            String imagingSourceId, String jobActionBarString) {
        if (!isAimDataService(imagingSource)) {
            String editString =
                imagingSource.getImageAnnotationConfiguration() != null ? "Edit Annotations" : "Add Annotations";
            utilThis.setValue(JOB_EDIT_ANNOTATIONS_URL + imagingSourceId, retrieveUrl(imagingSource,
                    "editImagingSourceAnnotations", editString, "edit_annotations", false));
            utilThis.setValue(JOB_ACTION_BAR2 + imagingSourceId, jobActionBarString, false);
        }
    }

    private void addLoadAnnotationAction(ImageDataSourceConfiguration imagingSource, Util utilThis,
            String imagingSourceId, String jobActionBarString) {
        if (isAimDataService(imagingSource)) {
            if (!Status.LOADED.equals(imagingSource.getStatus())) {
                utilThis.setValue(JOB_LOAD_ANNOTATIONS_URL + imagingSourceId,
                    retrieveUrl(imagingSource, "loadAimAnnotation", "Load AIM Annotations", "load", false),
                    false);
                utilThis.setValue(JOB_ACTION_BAR3 + imagingSourceId, jobActionBarString, false);
            }
        } else if (imagingSource.getImageAnnotationConfiguration().isLoadable()
                && !imagingSource.getImageAnnotationConfiguration().isCurrentlyLoaded()) {
            utilThis.setValue(JOB_LOAD_ANNOTATIONS_URL + imagingSourceId,
                    retrieveUrl(imagingSource, "loadImagingSource", "Load Annotations", "load", false),
                    false);
            utilThis.setValue(JOB_ACTION_BAR3 + imagingSourceId, jobActionBarString, false);
        }
    }

    private String retrieveUrl(ImageDataSourceConfiguration imagingSource, String actionName,
            String linkDisplay, String linkCssClass, boolean isDelete) {
        String deleteString = "";
        if (isDelete) {
            deleteString = "onclick=\"return confirm('The Imaging Data Source "
                            + imagingSource.getCollectionName() + " will be permanently deleted.')\"";
        }

        return "<a style=\"margin: 0pt;\" class=\"btn\" href=\"" + actionName + ".action?studyConfiguration.id="
                    + imagingSource.getStudyConfiguration().getId()
                    + "&imageSourceConfiguration.id=" + imagingSource.getId() + "\""
                    + deleteString + "><span class=\"btn_img\"><span class=\""
                    + linkCssClass + "\">" + linkDisplay + "</span></span></a>";
    }

    private String getStatusMessage(Status imagingSourceStatus) {
        if (Status.PROCESSING.equals(imagingSourceStatus)) {
            return AJAX_LOADING_GIF + " " + imagingSourceStatus.getValue();
        }
        return imagingSourceStatus.getValue();
    }

    /**
     * @return the studyManagementService
     */
    public StudyManagementService getStudyManagementService() {
        return studyManagementService;
    }

    /**
     * @param studyManagementService the studyManagementService to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.studyManagementService = studyManagementService;
    }


}
