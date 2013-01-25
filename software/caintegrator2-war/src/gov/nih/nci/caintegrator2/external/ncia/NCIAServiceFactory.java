/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

/**
 * Creates NCIASearchService instances.
 */
public interface NCIAServiceFactory {

    /**
     * Creates a <code>NCIASearchService</code> instance connected to the server provided in the profile.
     * 
     * @param profile contains host information for the server to connect to.
     * @return the search service.
     * @throws ConnectionException if there's a problem connecting to the caArray server.
     */
    NCIASearchService createNCIASearchService(ServerConnectionProfile profile) throws ConnectionException;
}
