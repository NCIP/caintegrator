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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.xwork.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobExecutionContext;

/**
 *
 * Tests the data refresh job.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class DataRefreshJobTest {

    private DataRefreshJob job;
    private CaIntegrator2Dao dao;
    private CaArrayFacade caArrayFacade;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() throws Exception {
        Date now = new Date();
        job = new DataRefreshJob();
        caArrayFacade = mock(CaArrayFacade.class);
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class))).thenReturn(now);

        job.setCaArrayFacade(caArrayFacade);

        dao = mock(CaIntegrator2Dao.class);
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        source.setExperimentIdentifier("test-experiment");
        source.setLastModifiedDate(DateUtils.addDays(now, -1));
        when(dao.getAllGenomicDataSources()).thenReturn(Arrays.asList(source));
        job.setDao(dao);
    }


    /**
     * Tests the job execution.
     * @throws Exception on error
     */
    @Test
    public void executeInternal() throws Exception {
        job.executeInternal(mock(JobExecutionContext.class));
        verify(dao, times(1)).getAllGenomicDataSources();
        verify(caArrayFacade, times(1)).getLastDataModificationDate(any(GenomicDataSourceConfiguration.class));;
        verify(dao, times(1)).save(anyObject());
        verify(dao, times(1)).markStudiesAsNeedingRefresh();
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
}
