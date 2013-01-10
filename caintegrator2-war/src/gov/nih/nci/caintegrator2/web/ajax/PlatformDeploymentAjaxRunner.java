/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;

/**
 * Asynchronous thread that runs Study Deployment jobs and updates the status of those jobs. 
 */
public class PlatformDeploymentAjaxRunner implements Runnable {
    
    private final PlatformDeploymentAjaxUpdater updater;
    private final PlatformConfiguration platformConfiguration;
    private final String username;
    
    
    
    PlatformDeploymentAjaxRunner(PlatformDeploymentAjaxUpdater updater,
            PlatformConfiguration platformConfiguration, 
            String username) {
        this.updater = updater;
        this.platformConfiguration = platformConfiguration;
        this.username = username;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
        statusUpdated(updater.getArrayDataService().loadArrayDesign(platformConfiguration));
    }

    /**
     * {@inheritDoc}
     */
    public void statusUpdated(PlatformConfiguration configuration) {
        if (Status.ERROR.equals(configuration.getStatus())) {
            updater.addError(configuration.getStatusDescription(), username);
        }
        updater.updateJobStatus(username, configuration);
    }

}
