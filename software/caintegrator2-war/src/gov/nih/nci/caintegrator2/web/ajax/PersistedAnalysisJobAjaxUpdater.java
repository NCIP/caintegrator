/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caArray
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caArray Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caArray Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caArray Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.StatusUpdateListener;
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

import org.apache.commons.lang.StringUtils;
import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See method "addActionBars()"
public class PersistedAnalysisJobAjaxUpdater extends AbstractDwrAjaxUpdater
    implements IPersistedAnalysisJobAjaxUpdater, StatusUpdateListener {
    
    private static final String STATUS_TABLE = "analysisJobStatusTable";
    private static final String JOB_NAME = "jobName_";
    private static final String JOB_TYPE = "jobType_";
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

    /**
     * {@inheritDoc}
     */
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
        String[][] rowString = new String[1][7];
        String id = job.getId().toString();
        String startSpan = "<span id=\"";
        String endSpan = "\"> </span>";
        rowString[0][0] = startSpan + JOB_NAME + id + endSpan;
        rowString[0][1] = startSpan + JOB_TYPE + id + endSpan;
        rowString[0][2] = startSpan + JOB_STATUS + id + endSpan;
        rowString[0][3] = startSpan + JOB_STATUS_DESCRIPTION + id + endSpan;
        rowString[0][4] = startSpan + JOB_CREATION_DATE + id + endSpan;
        rowString[0][5] = startSpan + JOB_LAST_UPDATE_DATE + id + endSpan;
        rowString[0][6] = startSpan + DELETE_ACTION + id + endSpan
                          + startSpan + JOB_ACTION_BAR1 + id + endSpan
                          + startSpan + JOB_INPUT_URL + id + endSpan
                          + startSpan + JOB_ACTION_BAR2 + id + endSpan
                          + startSpan + JOB_RESULTS_URL + id + endSpan;
        return rowString;
    }

    /**
     * {@inheritDoc}
     */
    public void runJob(AbstractPersistedAnalysisJob job) {
        Thread jobRunner = null;
        switch(AnalysisJobTypeEnum.getByValue(job.getJobType())) {
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
        utilThis.setValue(JOB_TYPE + jobId, job.getJobType());
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

    @SuppressWarnings("PMD.CyclomaticComplexity") // Figuring out what combination of actions are available.
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
    
    private Util getDwrUtil(String username) {
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