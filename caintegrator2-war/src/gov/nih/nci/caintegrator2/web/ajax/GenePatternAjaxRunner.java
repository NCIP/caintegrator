/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.analysis.JobInfoWrapper;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.GenePatternAnalysisJob;
import gov.nih.nci.caintegrator2.web.action.analysis.AbstractAnalysisFormParameter;

import java.util.List;

import org.genepattern.webservice.WebServiceException;

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
        } catch (Exception e) {
            processErrorState(e);
        } catch (Error e) {
            processErrorState(e);
        }
    }

    private void processErrorState(Throwable e) {
        String errorMessage = "Couldn't execute GenePattern analysis job: " + e.getMessage();
        updater.addError(errorMessage, job);
        job.setStatus(AnalysisJobStatusEnum.LOCAL_ERROR);
        job.setStatusDescription(errorMessage);
        updater.saveAndUpdateJobStatus(job);
    }

    private JobInfoWrapper processLocally() throws WebServiceException, InvalidCriterionException {
        configureInvocationParameters(job.getGenePatternAnalysisForm().getParameters());
        JobInfoWrapper jobInfo = updater.getAnalysisService().executeGenePatternJob(
                job.getGenePatternAnalysisForm().getServer(), job.getGenePatternAnalysisForm().getInvocation());
        job.setJobUrl(jobInfo.getUrl().toExternalForm());
        job.setStatus(AnalysisJobStatusEnum.COMPLETED);
        job.setGpJobNumber(jobInfo.getJobInfo().getJobNumber());
        updater.saveAndUpdateJobStatus(job);
        return jobInfo;
    }
    
    private void configureInvocationParameters(List <AbstractAnalysisFormParameter> parameters) 
    throws InvalidCriterionException {
        job.setSubscription(updater.getAnalysisService().getRefreshedStudySubscription(job.getSubscription()));
        for (AbstractAnalysisFormParameter formParameter : parameters) {
            formParameter.configureForInvocation(job.getSubscription(), updater.getQueryManagementService());
        }
    }

}
