/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

/**
 * Creates AIMSearchService instances.
 */
public interface AIMServiceFactory {

    /**
     * Creates a <code>AIMSearchService</code> instance connected to the server provided in the profile.
     * 
     * @param connection contains host information for the server to connect to.
     * @return the search service.
     * @throws ConnectionException if there's a problem connecting to the AIM server.
     */
    AIMSearchService createAIMSearchService(ServerConnectionProfile connection) throws ConnectionException;
}
