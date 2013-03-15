/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid;

import static org.junit.Assert.assertFalse;
import gov.nih.nci.caintegrator.application.analysis.grid.GridDiscoveryClientImpl;
import gov.nih.nci.caintegrator.application.analysis.grid.GridDiscoveryServiceJob;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionException;

public class GridDiscoveryServiceJobTestIntegration extends AbstractMockitoTest {

    private GridDiscoveryServiceJob job;
    private GridDiscoveryClientImpl gridDiscoveryClientImpl = new GridDiscoveryClientImpl();

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        job = new GridDiscoveryServiceJob();
        job.setGridDiscoveryClient(gridDiscoveryClientImpl);
        job.setConfigurationHelper(configurationHelper);
        gridDiscoveryClientImpl.setConfigurationHelper(configurationHelper);
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
