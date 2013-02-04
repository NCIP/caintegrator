/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid;

import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

/**
 * Grid Discovery Client to get available services.
 */
public interface GridDiscoveryClient {

    /**
     * Get the EndpointReferenceTypes of the grid services registered in the Index Service.
     * @return matching EndpointReferenceType
     * @throws ResourcePropertyRetrievalException if the DiscoveryClient or MetadaUtils cannot retrieve data
     * @throws MalformedURIException invalid URL
     * 
     */
    EndpointReferenceType[] getServices()
        throws ResourcePropertyRetrievalException, MalformedURIException;

    /**
     * @param searchString search string.
     * @return matching EndpointReferenceType
     * @throws ResourcePropertyRetrievalException exception from the grid service
     * @throws MalformedURIException invalid URL
     */
    EndpointReferenceType[] searchServices(String searchString)
        throws ResourcePropertyRetrievalException, MalformedURIException;

    /**
     * Extract the URL address from the EndpointReferenceType.
     * @param endpoint EndpointReferenceType
     * @return the URL address
     */
    String getAddress(EndpointReferenceType endpoint);

    /**
     * Extract the hosting center from the EndpointReferenceType.
     * @param endpoint EndpointReferenceType
     * @return the hosting center
     * @throws ResourcePropertyRetrievalException exception from the grid service
     */
    String getHostinCenter(EndpointReferenceType endpoint)
        throws ResourcePropertyRetrievalException;

}
