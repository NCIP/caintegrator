/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.StatusUpdateListener;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.
 */
public class PersistedAnalysisJobAjaxUpdater extends AbstractDwrAjaxUpdater
    implements IPersistedAnalysisJobAjaxUpdater, StatusUpdateListener {

    private static final String STATUS_TABLE = "analysisJobStatusTable";
    private static final String JOB_NAME = "jobName_";
    private static final String JOB_TYPE = "jobType_";
    private static final String JOB_METHOD = "jobMethod_";
    private static final String JOB_STATUS = "jobStatus_";
    private static final String JOB_STATUS_DESCRIPTION = "jobStatusDescription_";
    private static final String JOB_CREATION_DATE = "jobCreationDate_";
    private static final String JOB_LAST_UPDATE_DATE = "jobLastUpdateDate_";
    private static final String DELETE_ACTION = "action_";
    private static final String JOB_RESULTS_URL = "jobResultsUrl_";
    private static final String JOB_INPUT_URL = "jobInputUrl_";
    private static final String JOB_ACTION_BAR1 = "jobActionBar1_";
    private static final String JOB_ACTION_BAR2 = "jobActionBar2_";
    private AnalysisService analysisService;
    private QueryManagementService queryManagementService;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeDynamicTable(DisplayableUserWorkspace workspace) {
        int counter = 0;
        List <AbstractPersistedAnalysisJob> jobList = new ArrayList<AbstractPersistedAnalysisJob>();
        jobList.addAll(workspace.getCurrentStudySubscription().getAnalysisJobCollection());
        Collections.sort(jobList);
        for (AbstractPersistedAnalysisJob job : jobList) {
            getDwrUtil(workspace.getUserWorkspace().getUsername())
                                            .addRows(STATUS_TABLE,
                                            createRow(job),
                                            retrieveRowOptions(counter));
            updateJobStatus(job);
            counter++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void associateJobWithSession(DwrUtilFactory dwrUtilFactory, String username, Util util) {
        dwrUtilFactory.associateAnalysisJobWithSession(username, util);
    }

    private String[][] createRow(AbstractPersistedAnalysisJob job) {
        String[][] rowString = new String[1][8];
        String id = job.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_NAME + id + endSpan;
        rowString[0][1] = startSpan + JOB_TYPE + id + endSpan;
        rowString[0][2] = startSpan + JOB_METHOD + id + endSpan;
        rowString[0][3] = startSpan + JOB_STATUS + id + endSpan;
        rowString[0][4] = startSpan + JOB_STATUS_DESCRIPTION + id + endSpan;
        rowString[0][5] = startSpan + JOB_CREATION_DATE + id + endSpan;
        rowString[0][6] = startSpan + JOB_LAST_UPDATE_DATE + id + endSpan;
        rowString[0][7] = startSpan + DELETE_ACTION + id + endSpan
                          + startSpan + JOB_ACTION_BAR1 + id + endSpan
                          + startSpan + JOB_INPUT_URL + id + endSpan
                          + startSpan + JOB_ACTION_BAR2 + id + endSpan
                          + startSpan + JOB_RESULTS_URL + id + endSpan;
        return rowString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void runJob(AbstractPersistedAnalysisJob job) {
        Thread jobRunner = null;
        if (AnalysisJobTypeEnum.GENE_PATTERN.equals(job.getJobType())) {
            jobRunner = new Thread(new GenePatternAjaxRunner(this, (GenePatternAnalysisJob) job));
        } else {
            switch (AnalysisJobTypeEnum.getByValue(job.getMethod())) {
            case CMS:
                jobRunner = new Thread(new ComparativeMarkerSelectionAjaxRunner(this,
                        (ComparativeMarkerSelectionAnalysisJob) job));
                break;
            case GENE_PATTERN:
                jobRunner = new Thread(new GenePatternAjaxRunner(this, (GenePatternAnalysisJob) job));
                break;
            case PCA:
                jobRunner = new Thread(new PCAAjaxRunner(this, (PrincipalComponentAnalysisJob) job));
                break;
            case GISTIC:
                jobRunner = new Thread(new GisticAjaxRunner(this, (GisticAnalysisJob) job));
                break;
            default:
                throw new IllegalStateException("Job type doesn't have an associated Runner");
            }
        }
        jobRunner.start();
    }

    /**
     * Adds error to JSP.
     * @param errorMessage .
     * @param job to associate JSP script session to.
     */
    public void addError(String errorMessage, AbstractPersistedAnalysisJob job) {
        getDwrUtil(job.getUserWorkspace().getUsername()).setValue("errors", errorMessage);
    }

    /**
     * Saves job to database, then updates the status to JSP.
     * @param job to save and update.
     */
    public void saveAndUpdateJobStatus(AbstractPersistedAnalysisJob job) {
        job.setLastUpdateDate(new Date());
        getWorkspaceService().savePersistedAnalysisJob(job);
        updateJobStatus(job);
    }

    /**
     * Updates job status.
     * @param job to update.
     */
    public void updateJobStatus(AbstractPersistedAnalysisJob job) {
        Util utilThis = getDwrUtil(job.getUserWorkspace().getUsername());
        String jobId = job.getId().toString();
        utilThis.setValue(JOB_NAME + jobId, job.getName());
        StringBuffer jobTypeString = new StringBuffer(job.getJobType().getType());
        if (job instanceof GisticAnalysisJob) {
            jobTypeString.append(((GisticAnalysisJob) job).getConnectionType().getValue());
        }
        utilThis.setValue(JOB_TYPE + jobId, jobTypeString.toString());
        utilThis.setValue(JOB_METHOD + jobId, job.getMethod());
        utilThis.setValue(JOB_CREATION_DATE + jobId,
                getDateString(job.getCreationDate()));
        utilThis.setValue(JOB_STATUS + jobId, getStatusMessage(job.getStatus()));
        utilThis.setValue(JOB_STATUS_DESCRIPTION + jobId, job.getStatusDescription());
        if (job.getLastUpdateDate() != null) {
            utilThis.setValue(JOB_LAST_UPDATE_DATE + jobId,
                getDateString(job.getLastUpdateDate()));
        }
        addJobUrls(utilThis, job);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStatus(AbstractPersistedAnalysisJob job) {
        saveAndUpdateJobStatus(job);
    }

    private void addDeleteActionUrl(Util utilThis, AbstractPersistedAnalysisJob job) {
        String jobId = job.getId().toString();
        if (job.getStatus().isDeletable()) {
            utilThis.setValue(DELETE_ACTION + jobId,
                    "<a href=\"deleteGenePatternAnalysis.action?jobId=" + jobId
                    + "\" onclick=\"return confirm('This analysis job will be permanently deleted.')\">"
                    + "Delete</a>",
                    false);
        }
    }

    private void addJobUrls(Util utilThis, AbstractPersistedAnalysisJob job) {
        String jobId = job.getId().toString();
        utilThis.setValue(JOB_ACTION_BAR1 + jobId, "");
        utilThis.setValue(JOB_ACTION_BAR2 + jobId, "");
        updateJobUrls(utilThis, job, jobId);
    }

    private void updateJobUrls(Util utilThis, AbstractPersistedAnalysisJob job, String jobId) {
        addDeleteActionUrl(utilThis, job);
        boolean hasInputs = handleInputFile(utilThis, job, jobId);
        boolean hasResults = handleResults(utilThis, job, jobId);
        addActionBars(utilThis, jobId, hasInputs, hasResults, job.getStatus().isDeletable());
    }


    private boolean handleInputFile(Util utilThis, AbstractPersistedAnalysisJob job, String jobId) {
        boolean hasInputs = false;
        if (job.getInputZipFile() != null) {
            utilThis.setValue(JOB_INPUT_URL + jobId,
                    retrieveJobDownloadLink(jobId, DownloadType.INPUT),
                    false);
            hasInputs = true;
        }
        return hasInputs;
    }

    private void addActionBars(Util utilThis, String jobId,
            boolean hasInputs, boolean hasResults, boolean isDeletable) {
        if (isDeletable && (hasInputs || hasResults)) {
            utilThis.setValue(JOB_ACTION_BAR1 + jobId, " | ");
        }
        if (hasInputs && hasResults) {
            utilThis.setValue(JOB_ACTION_BAR2 + jobId, " | ");
        }
        if (!isDeletable && !hasInputs && !hasResults) {
            utilThis.setValue(DELETE_ACTION, "None");
        }
    }

    private boolean handleResults(Util utilThis, AbstractPersistedAnalysisJob job, String jobId) {
        boolean hasResults = false;
        if (job.getResultsZipFile() != null) {
            utilThis.setValue(JOB_RESULTS_URL + jobId,
                    retrieveJobDownloadLink(jobId, DownloadType.RESULTS),
                    false);
            hasResults = true;
        } else if (job instanceof GenePatternAnalysisJob
                && !StringUtils.isBlank(((GenePatternAnalysisJob) job).getJobUrl())) {
            GenePatternAnalysisJob gpJob = (GenePatternAnalysisJob) job;
            utilThis.setValue(JOB_RESULTS_URL + jobId,
                    "<a href=\"" + gpJob.getJobUrl() + "\" target=\"_\">View Results "
                        + gpJob.getGpJobNumber() + "</a>", false);
            hasResults = true;
        }
        return hasResults;
    }


    private String retrieveJobDownloadLink(String jobId, DownloadType downloadType) {
        return "<a href=\"analysisJobDownload.action?type="
                + downloadType.getType() + "&jobId=" + jobId
                + "\">" + downloadType.getDisplayString() + "</a>";
    }

    private String getStatusMessage(AnalysisJobStatusEnum jobStatus) {
        if (AnalysisJobStatusEnum.PROCESSING_LOCALLY.equals(jobStatus)
                || AnalysisJobStatusEnum.PROCESSING_REMOTELY.equals(jobStatus)) {
            return AJAX_LOADING_GIF + " " + jobStatus.getValue();
        }
        return jobStatus.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Util getDwrUtil(String username) {
        return getDwrUtilFactory().retrieveAnalysisJobUtil(username);
    }


    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    /**
     * Enum for the download type.
     */
    public static enum DownloadType {
        /**
         * Input download type.
         */
        INPUT("Input", "Download Input"),
        /**
         * Results download type.
         */
        RESULTS("Result", "Download Results");

        private String type;
        private String displayString;

        DownloadType(String type, String displayString) {
            this.type = type;
            this.displayString = displayString;
        }

        /**
         * @return the type
         */
        public String getType() {
            return type;
        }

        /**
         * @param type the type to set
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return the displayString
         */
        public String getDisplayString() {
            return displayString;
        }

        /**
         * @param displayString the displayString to set
         */
        public void setDisplayString(String displayString) {
            this.displayString = displayString;
        }
    }


}
