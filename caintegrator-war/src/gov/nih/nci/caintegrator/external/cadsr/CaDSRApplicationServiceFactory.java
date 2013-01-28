/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.cadsr;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.system.applicationservice.ApplicationService;

/**
 * Interface to create caDsr ApplicationService objects.
 */
public interface CaDSRApplicationServiceFactory {
    
    /**
     * Returns an ApplicationService object given the caDSR url.
     * @param caDsrUrl - URL for caDSR.
     * @return ApplicationService object based on URL.
     * @throws ConnectionException - Unable to get the connection.
     */
    ApplicationService retrieveCaDsrApplicationService(String caDsrUrl) throws ConnectionException;

}
