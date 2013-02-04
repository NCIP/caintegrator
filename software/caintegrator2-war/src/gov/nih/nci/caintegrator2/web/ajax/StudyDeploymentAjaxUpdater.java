/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private static final String JOB_DELETE_STUDY_URL = "studyJobDeleteUrl_";
    private static final String JOB_START_DATE = "studyJobStartDate_";
    private static final String JOB_FINISH_DATE = "studyJobFinishDate_";
    private static final String JOB_ACTION_BAR = "jobActionBar_";
    
    private DeploymentService deploymentService;

    /**
     * {@inheritDoc}
     */
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
        String[][] rowString = new String[1][7];
        String id = studyConfiguration.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_STUDY_NAME + id + endSpan;
        rowString[0][1] = startSpan + JOB_STUDY_DESCRIPTION + id + endSpan;
        rowString[0][2] = startSpan + JOB_LAST_MODIFIED_BY + id + endSpan;
        rowString[0][3] = startSpan + JOB_STUDY_STATUS + id + endSpan;
        rowString[0][4] = startSpan + JOB_START_DATE + id + endSpan;
        rowString[0][5] = startSpan + JOB_FINISH_DATE + id + endSpan;
        rowString[0][6] = startSpan + JOB_EDIT_STUDY_URL + id + endSpan
                          + startSpan + JOB_ACTION_BAR + id + endSpan
                          + startSpan + JOB_DELETE_STUDY_URL + id + endSpan;
        
        return rowString;
    }

    /**
     * {@inheritDoc}
     */
    public void runJob(StudyConfiguration studyConfiguration) {
        Thread studyConfigurationRunner = new Thread(new StudyDeploymentAjaxRunner(this, studyConfiguration));
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
     * @param username
     * @return
     */
    private Util getDwrUtil(String username) {
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
        utilThis.setValue(JOB_STUDY_NAME + studyConfigurationId, 
                          studyConfiguration.getStudy().getShortTitleText());
        utilThis.setValue(JOB_STUDY_DESCRIPTION + studyConfigurationId, 
                          studyConfiguration.getStudy().getLongTitleText());
        utilThis.setValue(JOB_LAST_MODIFIED_BY + studyConfigurationId, 
                          studyConfiguration.getLastModifiedBy().getUsername());
        utilThis.setValue(JOB_STUDY_STATUS + studyConfigurationId, 
                          getStatusMessage(studyConfiguration.getStatus()));
        utilThis.setValue(JOB_START_DATE + studyConfigurationId, 
                          getDateString(studyConfiguration.getDeploymentStartDate()));
        utilThis.setValue(JOB_FINISH_DATE + studyConfigurationId, 
                          getDateString(studyConfiguration.getDeploymentFinishDate()));
        updateRowActions(studyConfiguration, utilThis, studyConfigurationId);
    }

    private void updateRowActions(StudyConfiguration studyConfiguration, Util utilThis, String studyConfigurationId) {
        if (!Status.PROCESSING.equals(studyConfiguration.getStatus())) {
            utilThis.setValue(JOB_EDIT_STUDY_URL + studyConfigurationId, 
                    "<a href=\"editStudy.action?studyConfiguration.id=" 
                    + studyConfiguration.getId() + "\">Edit</a>",
                    false);
            utilThis.setValue(JOB_ACTION_BAR + studyConfigurationId, "&nbsp;|&nbsp;", false);
            utilThis.setValue(JOB_DELETE_STUDY_URL + studyConfigurationId, 
                    "<a href=\"deleteStudy.action?studyConfiguration.id=" 
                    + studyConfiguration.getId() 
                    + "\" onclick=\"return confirm('This study will be permanently deleted.')\">Delete</a>",
                    false);
            if (Status.ERROR.equals(studyConfiguration.getStatus())) {
                utilThis.setValue(JOB_STUDY_STATUS + studyConfigurationId, 
                        "<a title=\"Click to view the Error description in the Study Overview\" "
                        + "href=\"editStudy.action?studyConfiguration.id=" 
                        + studyConfiguration.getId()
                        + "\">Error</a>",
                        false);
            }
        } else {
            utilThis.setValue(JOB_EDIT_STUDY_URL + studyConfigurationId, "");
            utilThis.setValue(JOB_ACTION_BAR + studyConfigurationId, "");
            utilThis.setValue(JOB_DELETE_STUDY_URL, "");
        }
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
