/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator2.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator2.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * Asynchronous thread that runs PCA Grid based jobs and updates the status of those jobs.  
 */
public class PCAAjaxRunner implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(PCAAjaxRunner.class);
    
    private final PersistedAnalysisJobAjaxUpdater updater;
    private final PrincipalComponentAnalysisJob job;
    
    PCAAjaxRunner(PersistedAnalysisJobAjaxUpdater updater,
            PrincipalComponentAnalysisJob job) {
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
        } catch (ConnectionException e) {
            addErrorMessage("Couldn't execute Principal Component Analysis job: " + job.getName()
                    + " - " + e.getMessage(), AnalysisJobStatusEnum.ERROR_CONNECTING);
        } catch (InvalidCriterionException e) {
            addErrorMessage(e.getMessage(), AnalysisJobStatusEnum.LOCAL_ERROR);
        } catch (RuntimeException e) {
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

    private void processLocally() throws ConnectionException, InvalidCriterionException {
        File resultFile = updater.getAnalysisService().executeGridPCA(updater, job);
        job.setStatus(AnalysisJobStatusEnum.COMPLETED);
        if (resultFile != null) {
            ResultsZipFile resultZipFile = new ResultsZipFile();
            resultZipFile.setPath(resultFile.getAbsolutePath());
            job.setResultsZipFile(resultZipFile);
        }
        updater.saveAndUpdateJobStatus(job);
    }

}
