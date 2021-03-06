/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.caintegrator.common.ConfigurationHelper;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Grid Discovery Client to get available services.
 */
@Component("gridDiscoveryClient")
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
    @Autowired
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }

}
