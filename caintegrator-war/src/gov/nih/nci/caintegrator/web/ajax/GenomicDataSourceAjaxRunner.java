/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.caarray.ExperimentNotFoundException;

import org.apache.log4j.Logger;

/**
 * Asynchronous thread that runs Genomic Source Loading jobs and updates the status of those jobs.
 */
public class GenomicDataSourceAjaxRunner implements Runnable {
    
    private static final Logger LOGGER = Logger.getLogger(GenomicDataSourceAjaxRunner.class);
        
    private final GenomicDataSourceAjaxUpdater updater;
    private final Long genomicSourceId;
    private GenomicDataSourceConfiguration genomicSource;
    private String username;
    
    GenomicDataSourceAjaxRunner(GenomicDataSourceAjaxUpdater updater,
            Long genomicSourceId) {
        this.updater = updater;
        this.genomicSourceId = genomicSourceId;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.AvoidCatchingThrowable") // We want to catch all so that it goes into the error state.
    public void run() {
        setupSession();
        updater.updateJobStatus(username, genomicSource, true);
        try {
            updater.getStudyManagementService().loadGenomicSource(genomicSource);
            updater.updateJobStatus(username, genomicSource, true);
        } catch (ConnectionException e) {
            addError("The configured server couldn't be reached. Please check the configuration settings.", e);
        } catch (ExperimentNotFoundException e) {
            addError(e.getMessage(), e);
        } catch (Throwable e) {
            addError(e.getMessage(), e);
        }
    }

    private void setupSession() {
        genomicSource = updater.getStudyManagementService().getRefreshedGenomicSource(genomicSourceId);
        username = genomicSource.getStudyConfiguration().getLastModifiedBy().getUsername();
    }

    private void addError(String message, Throwable e) {
        LOGGER.error("Deployment of genomic source failed.", e);
        genomicSource.setStatus(Status.ERROR);
        genomicSource.setStatusDescription(message);
        updater.saveAndUpdateJobStatus(username, genomicSource);
    }
}
