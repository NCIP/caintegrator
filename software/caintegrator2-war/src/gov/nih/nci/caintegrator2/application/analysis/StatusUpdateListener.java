/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.domain.application.AbstractPersistedAnalysisJob;

/**
 * 
 */
public interface StatusUpdateListener {

    /**
     * Update the job status on the web page.
     * @param job the job to update.
     */
    void updateStatus(AbstractPersistedAnalysisJob job);  
}
