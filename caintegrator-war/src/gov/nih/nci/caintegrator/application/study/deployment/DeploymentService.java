/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;

/**
 * Service responsible for study deployment.
 */
public interface DeploymentService {
    
    /**
     * Marks the study as having started deployment and notifies clients.
     * 
     * @param studyConfiguration the study configuration to deploy
     */
    void prepareForDeployment(StudyConfiguration studyConfiguration);
    
    /**
     * Performs the deployment and notifies clients of the results.
     * 
     * @param studyConfiguration the study configuration to deploy
     * @param heatmapParameters use to create data files
     * @return the study configuration.
     */
    StudyConfiguration performDeployment(StudyConfiguration studyConfiguration, HeatmapParameters heatmapParameters);
    
    /**
     * Update the study configuration status. 
     * 
     * @param studyConfiguration the study configuration to deploy
     * @param e throwable exception
     */
    void handleDeploymentFailure(StudyConfiguration studyConfiguration, Throwable e);

}
