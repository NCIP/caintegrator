/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.study.DeploymentListener;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

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
     * @throws ValidationException validation exception
     * @throws DataRetrievalException dataretrieval exception
     * @throws ConnectionException connection exception
     */
    Status performDeployment(StudyConfiguration studyConfiguration, DeploymentListener listener)
    throws ConnectionException, DataRetrievalException, ValidationException;
    
    /**
     * Update the study configuration status. 
     * 
     * @param studyConfiguration the study configuration to deploy
     * @param listener informed of status changes during deployment
     * @param e throwable exception
     * @return the resulting status
     */
    Status handleDeploymentFailure(StudyConfiguration studyConfiguration,
            DeploymentListener listener, Throwable e);

}
