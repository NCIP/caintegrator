/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.DelimitedTextClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyManagementService;
import gov.nih.nci.caintegrator2.application.study.ValidationException;

import org.apache.log4j.Logger;

/**
 * Asynchronous thread that runs Subject Source Loading jobs and updates the status of those jobs.
 */
public class SubjectDataSourceAjaxRunner implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(SubjectDataSourceAjaxRunner.class);

    private final SubjectDataSourceAjaxUpdater updater;
    private final Long subjectSourceId;
    private final Long studyConfigurationId;
    private final JobType jobType;
    private DelimitedTextClinicalSourceConfiguration subjectSource;
    private String username;

    SubjectDataSourceAjaxRunner(SubjectDataSourceAjaxUpdater updater,
            Long studyConfigurationId, Long subjectSourceId,
            JobType jobType) {
        this.updater = updater;
        this.subjectSourceId = subjectSourceId;
        this.studyConfigurationId = studyConfigurationId;
        this.jobType = jobType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        try {
            setupSession();
            updater.updateJobStatus(username, subjectSource);
            StudyManagementService studyManagementService = updater.getStudyManagementService();
            switch (jobType) {
            case LOAD:
                updater.updateJobStatus(username,
                        studyManagementService.loadClinicalAnnotation(studyConfigurationId, subjectSourceId));
                break;
            case RELOAD:
                updater.reinitializeDynamicTable(username,
                        studyManagementService.reLoadClinicalAnnotation(studyConfigurationId));
                break;
            case DELETE:
                updater.reinitializeDynamicTable(username,
                        studyManagementService.deleteClinicalSource(studyConfigurationId, subjectSourceId));
                break;
            default:
                throw new IllegalStateException("Unknown job type.");
            }
            updater.refreshJsp(username);
        } catch (ValidationException e) {
            addError(e.getResult().getInvalidMessage(), e);
        } catch (Throwable e) {
            addError(e.getMessage(), e);
        }
    }

    private void setupSession() {
        StudyConfiguration studyConfiguration =
            updater.getStudyManagementService().getRefreshedStudyConfiguration(studyConfigurationId);
        subjectSource = studyConfiguration.getClinicalSource(subjectSourceId);
        username = studyConfiguration.getLastModifiedBy().getUsername();
    }

    private void addError(String message, Throwable e) {
        LOGGER.error("Loading of subject annotation source failed.", e);
        subjectSource.setStatus(Status.ERROR);
        subjectSource.setStatusDescription(message);
        updater.addError(message, username);
        updater.saveAndUpdateJobStatus(username, subjectSource);
    }

    /**
     * The job type to run for this ajax runner.
     */
    public static enum JobType {
        /**
         * Load.
         */
        LOAD,
        /**
         * Reload.
         */
        RELOAD,
        /**
         * Delete.
         */
        DELETE;
    }
}
