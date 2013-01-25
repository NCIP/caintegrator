/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis.grid;

import static org.junit.Assert.assertFalse;
import gov.nih.nci.caintegrator2.common.ConfigurationHelperStub;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionException;

public class GridDiscoveryServiceJobTestIntegration {

    private GridDiscoveryServiceJob job;
    private GridDiscoveryClientImpl gridDiscoveryClientImpl = new GridDiscoveryClientImpl();
    private ConfigurationHelperStub configurationHelperStub = new ConfigurationHelperStub();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        job = new GridDiscoveryServiceJob();
        job.setGridDiscoveryClient(gridDiscoveryClientImpl);
        job.setConfigurationHelper(configurationHelperStub);
        gridDiscoveryClientImpl.setConfigurationHelper(configurationHelperStub);
    }
    
    @Test
    public void executeInternalTest() throws JobExecutionException {
        assertFalse(GridDiscoveryServiceJob.getGridPreprocessServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridCmsServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridPcaServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridCaDnaCopyServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridGisticServices().isEmpty());
        job.executeInternal(null);
        assertFalse(GridDiscoveryServiceJob.getGridPreprocessServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridCmsServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridPcaServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridCaDnaCopyServices().isEmpty());
        assertFalse(GridDiscoveryServiceJob.getGridGisticServices().isEmpty());
    }
}
