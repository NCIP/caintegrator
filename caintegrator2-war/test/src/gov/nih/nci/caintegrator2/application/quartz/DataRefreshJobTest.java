/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.quartz;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;

import javax.ejb.EJBAccessException;

import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

/**
 *
 * Tests the data refresh job.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class DataRefreshJobTest extends AbstractMockitoTest {
    private DataRefreshJob job;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() throws Exception {
        job = new DataRefreshJob();
        job.setCaArrayFacade(caArrayFacade);
        job.setDao(dao);
    }


    /**
     * Tests the job execution in non SSO mode.
     * @throws Exception on error
     */
    @Test
    public void executeInternal() throws Exception {
        job.setSingleSignOnInstallation(false);
        job.executeInternal(mock(JobExecutionContext.class));
        verify(dao, times(1)).getAllGenomicDataSources();
        verify(caArrayFacade, times(1)).getLastDataModificationDate(any(GenomicDataSourceConfiguration.class));;
        verify(dao, times(1)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }

    /**
     * Tests the job execution in  SSO mode.
     * @throws Exception
     */
    @Test
    public void executeInternalSSOMode() throws Exception {
        job.setSingleSignOnInstallation(true);
        job.executeInternal(mock(JobExecutionContext.class));
        verify(dao, never()).getAllGenomicDataSources();
        verify(caArrayFacade, never()).getLastDataModificationDate(any(GenomicDataSourceConfiguration.class));;
        verify(dao, never()).save(anyObject());
        verify(dao, never()).markStudiesAsNeedingRefresh();
    }


    /**
     * Tests the job execution when no experiment is to be found.
     * @throws Exception on error
     */
    @Test
    public void executeInternalNoExperiment() throws Exception {
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class)))
        .thenThrow(new ExperimentNotFoundException("Experiement not found"));
        job.executeInternal(mock(JobExecutionContext.class));
        verify(dao, times(1)).getAllGenomicDataSources();
        verify(dao, times(0)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }

    /**
     * Tests the job execution when the connection fails.
     * @throws Exception on error
     */
    @Test
    public void executeInternalConnectionFails() throws Exception {
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class)))
        .thenThrow(new ConnectionException("Connection Failed"));
        job.executeInternal(mock(JobExecutionContext.class));
        verify(dao, times(1)).getAllGenomicDataSources();
        verify(dao, times(0)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }

    /**
     * Tests job execution when an unexpected error occurs.
     * @throws Exception on error
     */
    @Test
    public void executeInternalEJBError() throws Exception {
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class)))
        .thenThrow(new EJBAccessException());
        job.executeInternal(mock(JobExecutionContext.class));
        verify(dao, times(1)).getAllGenomicDataSources();
        verify(dao, times(0)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }

    /**
     * Tests the job execution.
     * @throws Exception on error
     */
    @Test
    public void run() throws Exception {
        job.run();
        verify(caArrayFacade, times(1)).getLastDataModificationDate(any(GenomicDataSourceConfiguration.class));;
        verify(dao, times(1)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }

    /**
     * Tests the job execution when no experiment is to be found.
     * @throws Exception on error
     */
    @Test
    public void runNoExperiment() throws Exception {
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class)))
        .thenThrow(new ExperimentNotFoundException("Experiement not found"));
        job.run();
        verify(dao, times(0)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }

    /**
     * Tests the job execution when the connection fails.
     * @throws Exception on error
     */
    @Test
    public void runInternalConnectionFails() throws Exception {
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class)))
        .thenThrow(new ConnectionException("Connection Failed"));
        job.run();
        verify(dao, times(0)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }

    /**
     * Tests job execution when an unexpected error occurs.
     * @throws Exception on error
     */
    @Test
    public void runGenericException() throws Exception {
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class))).thenThrow(new EJBAccessException());
        job.run();
        verify(dao, times(0)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
    }
}
