/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid;

import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.addressing.ServiceNameType;

/**
 * 
 */
public class GridDiscoveryClientStub implements GridDiscoveryClient {

    /**
     * {@inheritDoc}
     */
    public EndpointReferenceType[] getServices() throws ResourcePropertyRetrievalException {
        EndpointReferenceType[] results = new EndpointReferenceType[4];
        results[0] = buildEndpoint("http://broad/PreprocessDatasetMAGEService");
        results[1] = buildEndpoint("http://broad/ComparativeMarkerSelectionMAGEService");
        results[2] = buildEndpoint("http://broad/PCA");
        results[3] = buildEndpoint("http://broad/OtherService");
        return results;
    }
    
    /**
     * {@inheritDoc}
     */
    public EndpointReferenceType[] searchServices(String searchString) throws ResourcePropertyRetrievalException {
        EndpointReferenceType[] results = new EndpointReferenceType[3];
        results[0] = buildEndpoint("http://broad/PreprocessDatasetService" + searchString);
        results[1] = buildEndpoint("http://broad/ComparativeMarkerSelectionService" + searchString);
        results[2] = buildEndpoint("http://broad/OtherService" + searchString);
        return results;
    }
    
    private EndpointReferenceType buildEndpoint(String uri) {
        EndpointReferenceType endpoint = new EndpointReferenceType();
        endpoint.setServiceName(new ServiceNameType(uri, "Board"));
        return endpoint;
    }

    /**
     * {@inheritDoc}
     */
    public String getAddress(EndpointReferenceType endpoint) {
        return endpoint.getServiceName().getNamespaceURI();
    }

    /**
     * {@inheritDoc}
     */
    public String getHostinCenter(EndpointReferenceType endpoint) {
        return "NCI";
    }

}
