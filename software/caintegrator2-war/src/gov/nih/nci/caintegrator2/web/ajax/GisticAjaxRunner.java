/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ParameterException;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Asynchronous thread that runs GenePatternAnalysis jobs and updates the status of those jobs.  Still
 * need to eventually add a function to process the job remotely and update the status on GenePattern side.
 */
public class GisticAjaxRunner implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(GisticAjaxRunner.class);
    
    private final PersistedAnalysisJobAjaxUpdater updater;
    private final GisticAnalysisJob job;
    
    GisticAjaxRunner(PersistedAnalysisJobAjaxUpdater updater,
            GisticAnalysisJob job) {
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
            process();
        } catch (ConnectionException e) {
            addErrorMessage("Couldn't execute GISTIC analysis job: " + job.getName()
            + " - " + e.getMessage(), AnalysisJobStatusEnum.ERROR_CONNECTING);
        } catch (ParameterException e) {
            addErrorMessage("Couldn't execute GISTIC analysis job: " + job.getName()
            + " - " + e.getMessage(), AnalysisJobStatusEnum.INVALID_PARAMETER);
        } catch (Exception e) {
            addErrorMessage(e.getMessage(), AnalysisJobStatusEnum.LOCAL_ERROR);
        }
    }
    
    private void addErrorMessage(String errorMessage, AnalysisJobStatusEnum errorState) {
        updater.addError(errorMessage, job);
        LOGGER.error(errorMessage);
        job.setStatus(errorState);
        job.setStatusDescription(errorMessage);
        updater.saveAndUpdateJobStatus(job);
    }

    private void process() throws ConnectionException, InvalidCriterionException, ParameterException,
            IOException, DataRetrievalException {
        File resultFile = updater.getAnalysisService().executeGridGistic(updater, job);
        if (resultFile != null) {
            ResultsZipFile resultZipFile = new ResultsZipFile();
            resultZipFile.setPath(resultFile.getAbsolutePath());
            job.setResultsZipFile(resultZipFile);
        }
        updater.saveAndUpdateJobStatus(job);
    }

}
