/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Asynchronous thread that runs GenePatternAnalysis jobs and updates the status of those jobs.  Still
 * need to eventually add a function to process the job remotely and update the status on GenePattern side.
 */
public class ComparativeMarkerSelectionAjaxRunner implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(ComparativeMarkerSelectionAjaxRunner.class);
    
    private final PersistedAnalysisJobAjaxUpdater updater;
    private final ComparativeMarkerSelectionAnalysisJob job;
    
    ComparativeMarkerSelectionAjaxRunner(PersistedAnalysisJobAjaxUpdater updater,
            ComparativeMarkerSelectionAnalysisJob job) {
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
            processJob();
        } catch (ConnectionException e) {
            addErrorMessage("Couldn't execute ComparativeMarkerSelection analysis job: " + job.getName()
            + " - " + e.getMessage(), AnalysisJobStatusEnum.ERROR_CONNECTING);
        } catch (InvalidCriterionException e) {
            addErrorMessage(e.getMessage(), AnalysisJobStatusEnum.LOCAL_ERROR);
        } catch (RuntimeException e) {
            addErrorMessage(e.getMessage(), AnalysisJobStatusEnum.ERROR_CONNECTING);
        }
    }
    
    private void addErrorMessage(String errorMessage, AnalysisJobStatusEnum errorState) {
        updater.addError(errorMessage, job);
        LOGGER.error(errorMessage);
        job.setStatus(errorState);
        job.setStatusDescription(errorMessage);
        updater.saveAndUpdateJobStatus(job);
    }

    private void processJob() throws ConnectionException, InvalidCriterionException {
        File resultFile = updater.getAnalysisService().executeGridPreprocessComparativeMarker(
                updater, job);
        job.setStatus(AnalysisJobStatusEnum.COMPLETED);
        if (resultFile != null) {
            ResultsZipFile resultZipFile = new ResultsZipFile();
            resultZipFile.setPath(resultFile.getAbsolutePath());
            job.setResultsZipFile(resultZipFile);
        }
        updater.saveAndUpdateJobStatus(job);
    }

}
