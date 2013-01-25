/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;

/**
 * Asynchronous thread that runs Study Deployment jobs and updates the status of those jobs. 
 */
public class StudyDeploymentAjaxRunner implements Runnable {
        
    private final StudyDeploymentAjaxUpdater updater;
    private final StudyConfiguration job;
    private final HeatmapParameters heatmapParameters;
    private String username;
    
    StudyDeploymentAjaxRunner(StudyDeploymentAjaxUpdater updater, StudyConfiguration studyConfiguration,
            HeatmapParameters heatmapParameters) {
        this.updater = updater;
        this.heatmapParameters = heatmapParameters;
        job = studyConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        username = job.getLastModifiedBy().getUsername();
        statusUpdated(updater.getDeploymentService().performDeployment(job, heatmapParameters));
    }

    private void statusUpdated(StudyConfiguration configuration) {
        if (Status.ERROR.equals(configuration.getStatus())) {
            updater.addError(configuration.getStatusDescription(), configuration);
        }
        updater.updateJobStatus(username, configuration);
        if (Status.DEPLOYED.equals(configuration.getStatus())) {
            updater.refreshJsp(username);
        }
    }
}
