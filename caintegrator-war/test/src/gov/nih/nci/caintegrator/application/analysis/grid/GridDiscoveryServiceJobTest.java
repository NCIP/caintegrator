/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.application.analysis.grid.GridDiscoveryClient;
import gov.nih.nci.caintegrator.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.addressing.ServiceNameType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.quartz.JobExecutionException;

public class GridDiscoveryServiceJobTest extends AbstractMockitoTest {

    private GridDiscoveryServiceJob job;
    private GridDiscoveryClient gridDiscoveryClient;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        gridDiscoveryClient = mock(GridDiscoveryClient.class);

        EndpointReferenceType[] serviceEndpoints = new EndpointReferenceType[4];
        serviceEndpoints[0] = buildEndpoint("http://broad/PreprocessDatasetMAGEService");
        serviceEndpoints[1] = buildEndpoint("http://broad/ComparativeMarkerSelectionMAGEService");
        serviceEndpoints[2] = buildEndpoint("http://broad/PCA");
        serviceEndpoints[3] = buildEndpoint("http://broad/OtherService");
        when(gridDiscoveryClient.getServices()).thenReturn(serviceEndpoints);
        when(gridDiscoveryClient.searchServices(anyString())).thenAnswer(new Answer<EndpointReferenceType[]>() {
            @Override
            public EndpointReferenceType[] answer(InvocationOnMock invocation) throws Throwable {
                String searchString = (String) invocation.getArguments()[0];
                EndpointReferenceType[] results = new EndpointReferenceType[3];
                results[0] = buildEndpoint("http://broad/PreprocessDatasetService" + searchString);
                results[1] = buildEndpoint("http://broad/ComparativeMarkerSelectionService" + searchString);
                results[2] = buildEndpoint("http://broad/OtherService" + searchString);
                return results;
            }
        });
        when(gridDiscoveryClient.getAddress(any(EndpointReferenceType.class))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                EndpointReferenceType endpoint = (EndpointReferenceType) invocation.getArguments()[0];
                return endpoint.getServiceName().getNamespaceURI();
            }
        });
        when(gridDiscoveryClient.getHostinCenter(any(EndpointReferenceType.class))).thenReturn("NCI");

        job = new GridDiscoveryServiceJob();
        job.setConfigurationHelper(configurationHelper);
        job.setGridDiscoveryClient(gridDiscoveryClient);
    }

    @Test
    public void executeInternalTest() throws JobExecutionException {
        assertFalse(GridDiscoveryServiceJob.getGridPreprocessServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridCmsServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridPcaServices().isEmpty());
        job.executeInternal(null);
        assertFalse(GridDiscoveryServiceJob.getGridPreprocessServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridCmsServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridPcaServices().isEmpty());
    }

    private EndpointReferenceType buildEndpoint(String uri) {
        EndpointReferenceType endpoint = new EndpointReferenceType();
        endpoint.setServiceName(new ServiceNameType(uri, "Board"));
        return endpoint;
    }
}
