/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

/**
 * Grid Discovery Client to get available services.
 */
public class GridDiscoveryClientImpl implements GridDiscoveryClient {

    private DiscoveryClient discClient = null;
    private ConfigurationHelper configurationHelper;

    /**
     * {@inheritDoc}
     */
    public EndpointReferenceType[] getServices()
            throws ResourcePropertyRetrievalException, MalformedURIException {
        
        EndpointReferenceType[] allServices = null;
        allServices = getDiscClient().getAllServices(true);
        return allServices;
    }

    /**
     * {@inheritDoc}
     */
    public EndpointReferenceType[] searchServices(String searchString)
            throws ResourcePropertyRetrievalException, MalformedURIException {
        EndpointReferenceType[] searchedServices = null;
        searchedServices = getDiscClient().discoverServicesBySearchString(searchString);
        return searchedServices;
    }
    
    private DiscoveryClient getDiscClient() throws MalformedURIException {
        if (discClient == null) {
            this.discClient = new DiscoveryClient(
                configurationHelper.getString(ConfigurationParameter.GRID_INDEX_URL));
        }
        return discClient;
    }

    /**
     * {@inheritDoc}
     */
    public String getAddress(EndpointReferenceType endpoint) {
        return endpoint.getAddress().toString();
    }

    /**
     * {@inheritDoc}
     */
    public String getHostinCenter(EndpointReferenceType endpoint)
        throws ResourcePropertyRetrievalException {
        ServiceMetadata metadata = MetadataUtils.getServiceMetadata(endpoint);
        return metadata.getHostingResearchCenter().getResearchCenter().getDisplayName();
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }

}
