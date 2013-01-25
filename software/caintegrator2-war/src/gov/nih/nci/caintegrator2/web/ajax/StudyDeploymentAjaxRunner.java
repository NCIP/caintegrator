/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.DeploymentListener;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;

/**
 * Asynchronous thread that runs Study Deployment jobs and updates the status of those jobs. 
 */
public class StudyDeploymentAjaxRunner implements Runnable, DeploymentListener {
        
    private final StudyDeploymentAjaxUpdater updater;
    private final StudyConfiguration job;
    private String username;
    
    StudyDeploymentAjaxRunner(StudyDeploymentAjaxUpdater updater, StudyConfiguration studyConfiguration) {
        this.updater = updater;
        job = studyConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        username = job.getLastModifiedBy().getUsername();
            try {
                updater.getDeploymentService().performDeployment(job, this);
            } catch (Exception e) {
                updater.getDeploymentService().handleDeploymentFailure(job, this, e);
            } catch (Error e) {
                updater.getDeploymentService().handleDeploymentFailure(job, this, e);
            }
    }

    /**
     * {@inheritDoc}
     */
    public void statusUpdated(StudyConfiguration configuration) {
        if (Status.ERROR.equals(configuration.getStatus())) {
            updater.addError(configuration.getStatusDescription(), configuration);
        }
        updater.updateJobStatus(username, configuration);
    }
}
