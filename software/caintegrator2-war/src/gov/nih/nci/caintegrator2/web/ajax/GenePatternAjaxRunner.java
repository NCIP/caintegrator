/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import gov.nih.nci.caintegrator2.application.analysis.JobInfoWrapper;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;

/**
 * Asynchronous thread that runs GenePatternAnalysis jobs and updates the status of those jobs.  Still
 * need to eventually add a function to process the job remotely and update the status on GenePattern side.
 */
public class GenePatternAjaxRunner implements Runnable {
    private final PersistedAnalysisJobAjaxUpdater updater;
    private final GenePatternAnalysisJob job;
    
    GenePatternAjaxRunner(PersistedAnalysisJobAjaxUpdater updater, GenePatternAnalysisJob job) {
        this.updater = updater;
        this.job = job;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        job.setStatus(AnalysisJobStatusEnum.PROCESSING_LOCALLY);
        updater.saveAndUpdateJobStatus(job);
        try {
            processLocally();
        } catch (GenePatternServiceException e) {
            updater.addError("Couldn't execute GenePattern analysis job: " + e.getMessage(), job);
        }
    }

    private JobInfoWrapper processLocally() throws GenePatternServiceException {
        JobInfoWrapper jobInfo = updater.getAnalysisService().executeGenePatternJob(
                job.getGenePatternAnalysisForm().getServer(), job.getGenePatternAnalysisForm().getInvocation());
        job.setJobUrl(jobInfo.getUrl().toExternalForm());
        job.setStatus(AnalysisJobStatusEnum.COMPLETED);
        job.setGpJobNumber(jobInfo.getJobInfo().getJobNumber());
        updater.saveAndUpdateJobStatus(job);
        return jobInfo;
    }

}
