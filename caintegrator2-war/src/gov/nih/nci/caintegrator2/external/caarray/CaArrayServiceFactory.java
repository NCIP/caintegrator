/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

/**
 * Creates caArray service instances.
 */
public interface CaArrayServiceFactory {
    
    /**
     * Creates a <code>SearchService</code> instance connected to the server provided in the profile.
     * 
     * @param profile contains host information for the server to connect to.
     * @return the search service.
     * @throws ConnectionException if there's a problem connecting to the caArray server.
     */
    SearchService createSearchService(ServerConnectionProfile profile) throws ConnectionException;

    /**
     * Creates a <code>DataService</code> instance connected to the server provided in the profile.
     * 
     * @param profile contains host information for the server to connect to.
     * @return the search service.
     * @throws ConnectionException if there's a problem connecting to the caArray server.
     */
    DataService createDataService(ServerConnectionProfile profile) throws ConnectionException;

}
