/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;



/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IStudyDeploymentAjaxUpdater {
    
    /**
     * Initializes the web context to this JSP so the update messages stream here.
     */
    void initializeJsp();
    
    /**
     * Used to run the StudyDeploymentJob.
     * @param studyConfiguration study configuration to deploy.
     * @param heatmapParameters use to create data files.
     */
    void runJob(StudyConfiguration studyConfiguration, HeatmapParameters heatmapParameters);

}
