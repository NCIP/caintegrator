/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.study.DeploymentListener;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;

/**
 * Service responsible for study deployment.
 */
public interface DeploymentService {
    
    /**
     * Marks the study as having started deployment and notifies clients.
     * 
     * @param studyConfiguration the study configuration to deploy
     * @param listener informed of status changes during deployment
     * @return the resulting status.
     */
    Status prepareForDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener);
    
    /**
     * Performs the deployment and notifies clients of the results.
     * 
     * @param studyConfiguration the study configuration to deploy
     * @param listener informed of status changes during deployment
     * @return the resulting status.
     */
    Status performDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener);

}
