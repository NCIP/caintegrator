/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;



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
     */
    void runJob(StudyConfiguration studyConfiguration);

}
