/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.cabio;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.system.applicationservice.CaBioApplicationService;

/**
 * Interface to create caBIO ApplicationService objects.
 */
public interface CaBioApplicationServiceFactory {
    
    /**
     * Returns an ApplicationService object given the caBIO url.
     * @param caBioUrl - URL for caBIO.
     * @return ApplicationService object based on URL.
     * @throws ConnectionException - Unable to get the connection.
     */
    CaBioApplicationService retrieveCaBioApplicationService(String caBioUrl) throws ConnectionException;

}
