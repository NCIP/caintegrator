/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;


/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IPersistedAnalysisJobAjaxUpdater {
    
    /**
     * Initializes the web context to this JSP so the update messages stream here.
     */
    void initializeJsp();
    
    /**
     * Used to run the AbstractPersistedAnalysisJob.
     * @param job to run.
     */
    void runJob(AbstractPersistedAnalysisJob job);

}
