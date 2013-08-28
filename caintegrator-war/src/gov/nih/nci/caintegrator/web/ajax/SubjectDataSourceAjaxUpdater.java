/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.application.study.AbstractClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyManagementService;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;

import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.
 */
public class SubjectDataSourceAjaxUpdater extends AbstractDwrAjaxUpdater
    implements ISubjectDataSourceAjaxUpdater {

    private static final String STATUS_TABLE = "subjectSourceJobStatusTable";
    private static final String ERROR_MESSAGE = "errors";
    private static final String JOB_TYPE = "subjectSourceType_";
    private static final String JOB_DESCRIPTION = "subjectSourceDescription_";
    private static final String JOB_DEPLOYMENT_STATUS = "subjectSourceStatus_";
    private static final String JOB_LAST_MODIFIED_DATE = "subjectSourceLastModifiedDate_";
    private static final String JOB_EDIT_URL = "subjectSourceEditUrl_";
    private static final String JOB_LOAD_URL = "subjectSourceLoadUrl_";
    private static final String JOB_DELETE_URL = "subjectSourceDeleteUrl_";
    private static final String JOB_ACTION_BAR1 = "subjectSourceActionBar1_";
    private static final String JOB_ACTION_BAR2 = "subjectSourceActionBar2_";
    private static final String SUBJECT_SOURCES_LOADER = "subjectSourceLoader";

    private StudyManagementService studyManagementService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeDynamicTable(DisplayableUserWorkspace workspace) {
        try {
            StudyConfiguration studyConfiguration = workspace.getCurrentStudyConfiguration();
            if (studyConfiguration != null && studyConfiguration.getId() != null) {
                studyConfiguration = studyManagementService.
                    getRefreshedEntity(workspace.getCurrentStudyConfiguration());
                initializeDynamicTable(workspace.getUserWorkspace().getUsername(), studyConfiguration);
            }
        } finally {
            getDwrUtil(workspace.getUserWorkspace().getUsername()).setValue(SUBJECT_SOURCES_LOADER, "");
        }

    }

    private void initializeDynamicTable(String username, StudyConfiguration studyConfiguration) {
        int counter = 0;
        for (AbstractClinicalSourceConfiguration clinicalSource
                : studyConfiguration.getClinicalConfigurationCollection()) {
            if (!(clinicalSource instanceof DelimitedTextClinicalSourceConfiguration)) {
                throw new IllegalStateException("Unknown type of clinical source.");
            }
            DelimitedTextClinicalSourceConfiguration textSource =
                (DelimitedTextClinicalSourceConfiguration) clinicalSource;
            getDwrUtil(username).addRows(STATUS_TABLE,
                                            createRow(textSource),
                                            retrieveRowOptions(counter));
            updateJobStatus(username, textSource);
            counter++;
        }
    }

    /**
     * Removes table then reinitializes.
     * @param username of user.
     * @param studyConfiguration the study.
     */
    public void reinitializeDynamicTable(String username, StudyConfiguration studyConfiguration) {
        getDwrUtil(username).removeAllRows(STATUS_TABLE);
        initializeDynamicTable(username, studyConfiguration);
        toggleModalWaitDialog(username, studyConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void associateJobWithSession(DwrUtilFactory dwrUtilFactory, String username, Util util) {
        dwrUtilFactory.associateSubjectDataSourceWithSession(username, util);
    }

    private String[][] createRow(DelimitedTextClinicalSourceConfiguration clinicalSource) {
        String[][] rowString = new String[1][5];
        String id = clinicalSource.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_TYPE + id + endSpan;
        rowString[0][1] = startSpan + JOB_DESCRIPTION + id + endSpan;
        rowString[0][2] = startSpan + JOB_DEPLOYMENT_STATUS + id + endSpan;
        rowString[0][3] = startSpan + JOB_LAST_MODIFIED_DATE + id + endSpan;
        rowString[0][4] = startSpan + JOB_EDIT_URL + id + endSpan
                          + startSpan + getLoadActionUrl(clinicalSource) + id + endSpan
                          + startSpan + JOB_DELETE_URL + id + endSpan;

        return rowString;
    }

    private String getLoadActionUrl(DelimitedTextClinicalSourceConfiguration subjectSource) {
        if (subjectSource.isLoadable()) {
            return JOB_LOAD_URL;
        }
        return "NOT_LOADABLE";
    }

    /**
     * {@inheritDoc}
     */
    public void runJob(Long studyConfigurationId, Long subjectSourceId, SubjectDataSourceAjaxRunner.JobType jobType) {
        Thread subjectSourceRunner = new Thread(new SubjectDataSourceAjaxRunner(this,
                studyConfigurationId, subjectSourceId, jobType));
        subjectSourceRunner.start();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected Util getDwrUtil(String username) {
        return getDwrUtilFactory().retrieveSubjectDataSourceUtil(username);
    }

    /**
     * Saves clinicalSource status to database, then updates the status to JSP.
     * @param username to update the status to.
     * @param clinicalSource to save and update.
     */
    public void saveAndUpdateJobStatus(String username, DelimitedTextClinicalSourceConfiguration clinicalSource) {
        getStudyManagementService().saveSubjectSourceStatus(clinicalSource);
        updateJobStatus(username, clinicalSource);
    }

    /**
     * Adds error to JSP.
     * @param errorMessage .
     * @param username to associate JSP script session to.
     */
    public void addError(String errorMessage, String username) {
        getDwrUtil(username).setValue(ERROR_MESSAGE, errorMessage);
    }

    /**
     * Updates studyConfiguration status.
     * @param username to update the status to.
     * @param clinicalSource to update.
     */
    public void updateJobStatus(String username, DelimitedTextClinicalSourceConfiguration clinicalSource) {
        Util utilThis = getDwrUtil(username);
        String clinicalSourceId = clinicalSource.getId().toString();
        utilThis.setValue(JOB_TYPE + clinicalSourceId,
                clinicalSource.getType().toString());
        utilThis.setValue(JOB_DESCRIPTION + clinicalSourceId,
                clinicalSource.getDescription());
        utilThis.setValue(JOB_DEPLOYMENT_STATUS + clinicalSourceId, getStatusMessage(clinicalSource));
        utilThis.setValue(JOB_LAST_MODIFIED_DATE + clinicalSourceId,
                clinicalSource.getDisplayableLastModifiedDate());
        updateRowActions(clinicalSource, utilThis, clinicalSourceId);
        toggleModalWaitDialog(username, clinicalSource.getStudyConfiguration());

    }

    private void toggleModalWaitDialog(String username, StudyConfiguration studyConfiguration) {
        if (Cai2Util.isAnySubjectSourceInProgress(studyConfiguration)) {
            getDwrUtil(username).addFunctionCall("showBusyDialog");

        } else {
            getDwrUtil(username).addFunctionCall("hideBusyDialog");
        }
    }

    private void updateRowActions(DelimitedTextClinicalSourceConfiguration subjectSource,
            Util utilThis, String subjectSourceId) {
        if (!Status.PROCESSING.equals(subjectSource.getStatus())) { // Not processing gets actions
            addNonProcessingActions(subjectSource, utilThis, subjectSourceId);
        } else { // Processing has no actions.
            utilThis.setValue(JOB_EDIT_URL + subjectSourceId, "");
            utilThis.setValue(getLoadActionUrl(subjectSource) + subjectSourceId, "");
            utilThis.setValue(JOB_DELETE_URL + subjectSourceId, "");
            utilThis.setValue(JOB_ACTION_BAR1 + subjectSourceId, "");
            utilThis.setValue(JOB_ACTION_BAR2 + subjectSourceId, "");
        }
    }

    private void addNonProcessingActions(DelimitedTextClinicalSourceConfiguration subjectSource, Util utilThis,
            String subjectSourceId) {
        String jobActionBarString = "&nbsp;";
        utilThis.setValue(JOB_EDIT_URL + subjectSourceId,
                retrieveUrl(subjectSource, "editClinicalSource", "Edit Annotations", "edit_annotations", false),
                false);
        utilThis.setValue(JOB_ACTION_BAR1 + subjectSourceId, jobActionBarString, false);
        if (subjectSource.isLoadable()) {
            addLoadAction(subjectSource, utilThis, subjectSourceId, jobActionBarString);
        }
        utilThis.setValue(JOB_DELETE_URL + subjectSourceId,
                retrieveUrl(subjectSource, "deleteClinicalSource", "Delete", "delete", true),
                false);
    }

    private void addLoadAction(DelimitedTextClinicalSourceConfiguration subjectSource, Util utilThis,
            String subjectSourceId, String jobActionBarString) {
        if (!subjectSource.isCurrentlyLoaded()) {
            utilThis.setValue(JOB_LOAD_URL + subjectSourceId,
                    retrieveUrl(subjectSource, "loadClinicalSource",
                    "Load Subject Annotation Source", "load", false),
                    false);
        } else {
            utilThis.setValue(JOB_LOAD_URL + subjectSourceId,
                    retrieveUrl(subjectSource, "reLoadClinicalSource",
                    "Reload All Subject Annotation Sources", "reload", false),
                    false);
        }
        utilThis.setValue(JOB_ACTION_BAR2 + subjectSourceId, jobActionBarString, false);
    }

    private String retrieveUrl(DelimitedTextClinicalSourceConfiguration subjectSource, String actionName,
            String linkDisplay, String linkCssClass, boolean isDelete) {
        String deleteString = "", token = "", tokenName = "";

        try {
            token = SessionHelper.getInstance().getToken();
            tokenName = SessionHelper.getInstance().getTokenName();
        } catch (Exception e) { token = ""; }

        if (isDelete) {
            deleteString =
                "onclick=\"return confirm('The subject annotation source file will be permanently deleted.')\"";
        }

        return "<a style=\"margin: 0pt;\" class=\"btn\" href=\"" + actionName + ".action?studyConfiguration.id="
        + subjectSource.getStudyConfiguration().getId()
        + "&clinicalSource.id=" + subjectSource.getId()
        + "&struts.token.name=" + tokenName
        + "&token=" + token
        + "\""
        + deleteString + "><span class=\"btn_img\"><span class=\""
        + linkCssClass + "\">" + linkDisplay + "</span></span></a>";

    }

    private String getStatusMessage(AbstractClinicalSourceConfiguration source) {
        Status subjectSourceStatus = source.getStatus();
        if (Status.PROCESSING.equals(subjectSourceStatus)) {
            return AJAX_LOADING_GIF + " " + subjectSourceStatus.getValue();
        } else if (Status.NOT_LOADED.equals(subjectSourceStatus) && !source.isLoadable()) {
            return Status.DEFINITION_INCOMPLETE.getValue();
        } else if (Status.ERROR.equals(subjectSourceStatus)) {
            return "<a href=\"editClinicalSource.action?studyConfiguration.id="
            + source.getStudyConfiguration().getId()
            + "&clinicalSource.id=" + source.getId() + "\">" + Status.ERROR.getValue() + "</a>";
        }
        return subjectSourceStatus.getValue();
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
