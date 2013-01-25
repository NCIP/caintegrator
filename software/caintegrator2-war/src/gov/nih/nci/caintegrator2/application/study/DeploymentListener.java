/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

/**
 * Interface realized by clients that need updates of deployment status.
 */
public interface DeploymentListener {

    /**
     * Invoked when deployment status has changed.
     * 
     * @param configuration the configuration between deployed.
     */
    void statusUpdated(StudyConfiguration configuration);
    
}
