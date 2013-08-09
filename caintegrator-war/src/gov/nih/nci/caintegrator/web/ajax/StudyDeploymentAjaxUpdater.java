/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.
 */
public class StudyDeploymentAjaxUpdater extends AbstractDwrAjaxUpdater
    implements IStudyDeploymentAjaxUpdater {

    private static final String STATUS_TABLE = "studyDeploymentJobStatusTable";
    private static final String JOB_STUDY_NAME = "studyName_";
    private static final String JOB_STUDY_DESCRIPTION = "studyDescription_";
    private static final String JOB_STUDY_STATUS = "studyStatus_";
    private static final String JOB_LAST_MODIFIED_BY = "studyLastModified_";
    private static final String JOB_EDIT_STUDY_URL = "studyJobEditUrl_";
    private static final String JOB_COPY_STUDY_URL = "studyJobCopyUrl_";
    private static final String JOB_ARCHIVE_STUDY_URL = "studyJobArchiveUrl_";
    private static final String JOB_DELETE_STUDY_URL = "studyJobDeleteUrl_";
    private static final String JOB_START_DATE = "studyJobStartDate_";
    private static final String JOB_FINISH_DATE = "studyJobFinishDate_";
    private static final String JOB_NEW_DATA_AVAILABLE = "studyNewDataAvailable_";
    private static final String JOB_ACTION_BAR = "jobActionBar_";

    private DeploymentService deploymentService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeDynamicTable(DisplayableUserWorkspace workspace) {
        int counter = 0;
        List <StudyConfiguration> studyConfigurationList = new ArrayList<StudyConfiguration>();
        studyConfigurationList.addAll(workspace.getManagedStudies());
        Comparator<StudyConfiguration> nameComparator = new Comparator<StudyConfiguration>() {
            public int compare(StudyConfiguration configuration1, StudyConfiguration configuration2) {
                return configuration1.getStudy().getShortTitleText().
                       compareToIgnoreCase(configuration2.getStudy().getShortTitleText());
            }
        };
        Collections.sort(studyConfigurationList, nameComparator);
        String username = workspace.getUserWorkspace().getUsername();
        for (StudyConfiguration studyConfiguration : studyConfigurationList) {
            getDwrUtil(username).addRows(STATUS_TABLE,
                                            createRow(studyConfiguration),
                                            retrieveRowOptions(counter));
            updateJobStatus(username, studyConfiguration);
            counter++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void associateJobWithSession(DwrUtilFactory dwrUtilFactory, String username, Util util) {
        dwrUtilFactory.associateStudyConfigurationJobWithSession(username, util);
    }

    private String[][] createRow(StudyConfiguration studyConfiguration) {
        String[][] rowString = new String[1][8];
        String id = studyConfiguration.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_STUDY_NAME + id + endSpan;
        rowString[0][1] = startSpan + JOB_STUDY_DESCRIPTION + id + endSpan;
        rowString[0][2] = startSpan + JOB_LAST_MODIFIED_BY + id + endSpan;
        rowString[0][3] = startSpan + JOB_STUDY_STATUS + id + endSpan;
        rowString[0][4] = startSpan + JOB_START_DATE + id + endSpan;
        rowString[0][5] = startSpan + JOB_FINISH_DATE + id + endSpan;
        rowString[0][6] = startSpan + JOB_NEW_DATA_AVAILABLE + id + endSpan;
        rowString[0][7] = startSpan + JOB_EDIT_STUDY_URL + id + endSpan
                          + startSpan + JOB_ACTION_BAR + id + "e" + endSpan
                          + startSpan + JOB_COPY_STUDY_URL + id + endSpan
                          + startSpan + JOB_ACTION_BAR + id + "c" + endSpan
                          + startSpan + JOB_ARCHIVE_STUDY_URL + id + endSpan
                          + startSpan + JOB_ACTION_BAR + id + "a" + endSpan
                          + startSpan + JOB_DELETE_STUDY_URL + id + endSpan;

        return rowString;
    }

    /**
     * {@inheritDoc}
     */
    public void runJob(StudyConfiguration studyConfiguration, HeatmapParameters heatmapParameters) {
        Thread studyConfigurationRunner = new Thread(new StudyDeploymentAjaxRunner(
                this, studyConfiguration, heatmapParameters));
        studyConfigurationRunner.start();
    }

    /**
     * Adds error to JSP.
     * @param errorMessage .
     * @param studyConfiguration to associate JSP script session to.
     */
    public void addError(String errorMessage, StudyConfiguration studyConfiguration) {
        getDwrUtil(studyConfiguration.getUserWorkspace().getUsername()).setValue("errorMessages", errorMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Util getDwrUtil(String username) {
        return getDwrUtilFactory().retrieveStudyConfigurationUtil(username);
    }

    /**
     * Updates studyConfiguration status.
     * @param username to update the status to.
     * @param studyConfiguration to update.
     */
    public void updateJobStatus(String username, StudyConfiguration studyConfiguration) {
        Util utilThis = getDwrUtil(username);
        String studyConfigurationId = studyConfiguration.getId().toString();
        String descriptionDivId = JOB_STUDY_DESCRIPTION + "div_" + studyConfigurationId;
        utilThis.setValue(JOB_STUDY_NAME + studyConfigurationId,
                studyConfiguration.getStudy().getShortTitleText());
        utilThis.setValue(JOB_STUDY_DESCRIPTION + studyConfigurationId,
                "<s:div id=\"" + descriptionDivId + "\">"
                + studyConfiguration.getStudy().getLongTitleText() + "</s:div>");
        utilThis.setValue(JOB_LAST_MODIFIED_BY + studyConfigurationId,
                          studyConfiguration.getLastModifiedBy().getUsername());
        utilThis.setValue(JOB_STUDY_STATUS + studyConfigurationId,
                          getStatusMessage(studyConfiguration.getStatus()));
        utilThis.setValue(JOB_START_DATE + studyConfigurationId,
                          getDateString(studyConfiguration.getDeploymentStartDate()));
        utilThis.setValue(JOB_FINISH_DATE + studyConfigurationId,
                          getDateString(studyConfiguration.getDeploymentFinishDate()));
        utilThis.setValue(JOB_NEW_DATA_AVAILABLE + studyConfigurationId,
                StringUtils.capitalize(BooleanUtils.toStringYesNo(studyConfiguration.isDataRefreshed())));
        updateRowActions(studyConfiguration, utilThis, studyConfigurationId);
        utilThis.addFunctionCall("truncateDescriptionDiv", descriptionDivId);
    }

    private void updateRowActions(StudyConfiguration studyConfiguration, Util utilThis, String studyConfigurationId) {
        if (!Status.PROCESSING.equals(studyConfiguration.getStatus())) {
            utilThis.setValue(JOB_EDIT_STUDY_URL + studyConfigurationId,
                    getEditStudyUrlString(studyConfiguration, "Edit", ""),
                    false);
            utilThis.setValue(JOB_ACTION_BAR + studyConfigurationId + "e", "&nbsp;|&nbsp;", false);
            utilThis.setValue(JOB_COPY_STUDY_URL + studyConfigurationId,
                    getCopyStudyUrlString(studyConfiguration),
                    false);
            utilThis.setValue(JOB_ACTION_BAR + studyConfigurationId + "c", "&nbsp;|&nbsp;", false);
            utilThis.setValue(JOB_ARCHIVE_STUDY_URL + studyConfigurationId,
                    getArchiveStudyUrlString(studyConfiguration),
                    false);
            utilThis.setValue(JOB_ACTION_BAR + studyConfigurationId + "a", "&nbsp;|&nbsp;", false);

            String deleteMsg = "The study: " + studyConfiguration.getStudy().getShortTitleText()
                + " will be permanently deleted.";
            utilThis.setValue(JOB_DELETE_STUDY_URL + studyConfigurationId,
                    getDeleteStudyUrlString(studyConfiguration, deleteMsg),
                    false);
            addError(studyConfiguration, utilThis, studyConfigurationId);
        } else {
            utilThis.setValue(JOB_EDIT_STUDY_URL + studyConfigurationId, "");
            utilThis.setValue(JOB_ACTION_BAR + studyConfigurationId, "");
            utilThis.setValue(JOB_DELETE_STUDY_URL, "");
        }
    }

    private void addError(StudyConfiguration studyConfiguration, Util utilThis, String studyConfigurationId) {
            if (Status.ERROR.equals(studyConfiguration.getStatus())) {
                utilThis.setValue(JOB_STUDY_STATUS + studyConfigurationId,
                    getEditStudyUrlString(studyConfiguration, "Error",
                            "Click to view the Error description in the Study Overview"),
                        false);
            }
    }

    /**
     * @param studyConfiguration
     * @param deleteMsg
     * @return the string which forms the html for the deleteStudy url.
     */
    private String getDeleteStudyUrlString(StudyConfiguration studyConfiguration, String deleteMsg) {

        String token = "", tokenName = "";

        try {
            token = SessionHelper.getInstance().getToken();
            tokenName = SessionHelper.getInstance().getTokenName();
        } catch (Exception e) { token = ""; }

       String returnString = "<a href=\"deleteStudy.action?studyConfiguration.id="
               + studyConfiguration.getId() + "&struts.token.name=" + tokenName + "&token=" + token
               + "\" onclick=\"updateUrlTokenParameters(this);return confirm('" + deleteMsg + "')\">Delete</a>";

        return returnString;
    }

    /**
     * @param studyConfiguration
     * @return the string which forms the html for the editStudy url.
     */
    private String getEditStudyUrlString(StudyConfiguration studyConfiguration, String action, String title) {
        String returnString = null;
        if (studyConfiguration.getStudy().isEnabled()) {
            returnString = "<a href=\"editStudy.action?studyConfiguration.id="
           + studyConfiguration.getId()
           + "\" title=\"" + title + "\"";
        } else {
            returnString = "<a style=\"color:Grey; text-decoration:none;\""
            + " title=\"Edit Disabled - Enable Study to Edit\"";
        }
        return returnString + ">" + action + "</a>";
    }

    /**
     * @param studyConfiguration
     * @return the string which forms the html for the copyStudy url.
     */
    private String getCopyStudyUrlString(StudyConfiguration studyConfiguration) {

        String token = "", tokenName = "";

        try {
            token = SessionHelper.getInstance().getToken();
            tokenName = SessionHelper.getInstance().getTokenName();
        } catch (Exception e) { token = ""; }

       String returnString = null;

       if (studyConfiguration.getStudy().isEnabled()) {
           returnString = "<a onclick=\"updateUrlTokenParameters(this)\" href=\"copyStudy.action?studyConfiguration.id="
       + studyConfiguration.getId() + "&struts.token.name=" + tokenName + "&token=" + token;
       } else {
           returnString = "<a style=\"color:Grey; text-decoration:none;\""
               + " title=\"Copy Disabled - Enable Study to Copy\"";
       }
       return returnString + "\" >Copy</a>";
    }

    /**
     * @param studyConfiguration
     * @return the string which forms the html for the disable/enable Study url.
     */
    private String getArchiveStudyUrlString(StudyConfiguration studyConfiguration) {
        String token = "", tokenName = "";
        try {
            token = SessionHelper.getInstance().getToken();
            tokenName = SessionHelper.getInstance().getTokenName();
        } catch (Exception e) { token = ""; }

       String returnString = "";
       String actionName = "";
       if (BooleanUtils.isTrue(studyConfiguration.getStudy().isEnabled())) {
           returnString = "<a onclick=\"updateUrlTokenParameters(this)\" "
                   + "href=\"disableStudy.action?studyConfiguration.id=";
           actionName = "Disable";
       } else {
           returnString = "<a onclick=\"updateUrlTokenParameters(this)\" "
                   + "href=\"enableStudy.action?studyConfiguration.id=";
           actionName = "Enable";
       }

       return returnString + studyConfiguration.getId()
       + "&struts.token.name="
       + tokenName
       + "&token="
       + token
       + "\" >" + actionName + "</a>";
    }

    private String getStatusMessage(Status studyConfigurationStatus) {
        if (Status.PROCESSING.equals(studyConfigurationStatus)) {
            return AJAX_LOADING_GIF + " " + studyConfigurationStatus.getValue();
        }
        return studyConfigurationStatus.getValue();
    }

    /**
     * @return the deploymentService
     */
    public DeploymentService getDeploymentService() {
        return deploymentService;
    }

    /**
     * @param deploymentService the deploymentService to set
     */
    public void setDeploymentService(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }


}
