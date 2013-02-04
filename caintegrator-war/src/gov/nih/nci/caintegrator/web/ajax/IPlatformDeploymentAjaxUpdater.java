/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;



/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IPlatformDeploymentAjaxUpdater {
    
    /**
     * Initializes the web context to this JSP so the update messages stream here.
     */
    void initializeJsp();
    
    /**
     * Used to run the PlatformConfiguration job.
     * @param platformConfiguration of PlatformConfiguration to run.
     * @param username of the user to update the status to.
     */
    void runJob(PlatformConfiguration platformConfiguration, String username);

}
