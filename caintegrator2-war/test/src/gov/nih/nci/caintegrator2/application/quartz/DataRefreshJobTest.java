/**
 * Copyright (c) 2012, 5AM Solutions, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of the author nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
