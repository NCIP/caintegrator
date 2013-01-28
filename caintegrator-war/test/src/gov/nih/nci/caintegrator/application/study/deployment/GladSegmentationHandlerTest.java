/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import gov.nih.nci.caintegrator.application.study.deployment.GladSegmentationHandler;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class GladSegmentationHandlerTest extends AbstractMockitoTest {
    private static final String TEST_OUTPUT = "Sample\tChromosome\tStart.bp\tEnd.bp\tNum.SNPs\tSeg.CN\n"
            + "1\t17\t41419603\t36581538\t6427\t2.06\n"
            + "2\t18\t56507051\t10075159\t6732\t1.881\n";
    private GladSegmentationHandler handler;
    private GenePatternClient genePatternClient;

    @Before
    public void setUp() throws Exception {
        genePatternClient = mock(GenePatternClient.class);
        when(genePatternClient.runAnalysis(anyString(), anyListOf(ParameterInfo.class))).thenReturn(new JobInfo());
        when(genePatternClient.getStatus(any(JobInfo.class))).thenAnswer(new Answer<JobInfo>() {
            @Override
            public JobInfo answer(InvocationOnMock invocation) throws Throwable {
                JobInfo jobInfo = (JobInfo) invocation.getArguments()[0];
                jobInfo.setStatus("Completed");
                return jobInfo;
            }
        });
        when(genePatternClient.getResultFile(any(JobInfo.class), anyString())).thenAnswer(new Answer<File>() {
            @Override
            public File answer(InvocationOnMock invocation) throws Throwable {
                File outputFile = File.createTempFile("output", ".glad");
                DataHandler dataHandler = mock(DataHandler.class);
                when(dataHandler.getInputStream()).thenReturn(new ByteArrayInputStream(TEST_OUTPUT.getBytes()));
                FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                IOUtils.copy(dataHandler.getInputStream(), fileOutputStream);
                fileOutputStream.close();
                return outputFile;
            }
        });
        handler = new GladSegmentationHandler(genePatternClient);
    }

    @Test
    public void testAddSegmentationData() throws DataRetrievalException {
        List<DnaAnalysisReporter> reporters = new ArrayList<DnaAnalysisReporter>();
        DnaAnalysisData dnaAnalysisData = new DnaAnalysisData(reporters);
        ArrayData arrayData1 = new ArrayData();
        arrayData1.setId(1L);
        dnaAnalysisData.addDnaAnalysisData(arrayData1, new float[0]);
        ArrayData arrayData2 = new ArrayData();
        arrayData2.setId(2L);
        dnaAnalysisData.addDnaAnalysisData(arrayData2, new float[0]);
        handler.addSegmentationData(dnaAnalysisData);
        assertEquals(1, arrayData1.getSegmentDatas().size());
        assertEquals(1, arrayData2.getSegmentDatas().size());
        SegmentData segmentData = arrayData1.getSegmentDatas().first();
        assertEquals("17", segmentData.getLocation().getChromosome());
        assertEquals(41419603, (int) segmentData.getLocation().getStartPosition());
        assertEquals(36581538, (int) segmentData.getLocation().getEndPosition());
        assertEquals(6427, (int) segmentData.getNumberOfMarkers());
        assertEquals(2.06, segmentData.getSegmentValue(), 0.001);
    }
}
