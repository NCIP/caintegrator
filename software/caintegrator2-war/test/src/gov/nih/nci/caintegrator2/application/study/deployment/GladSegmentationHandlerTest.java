/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.application.analysis.GenePatternClientStub;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisData;
import gov.nih.nci.caintegrator2.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.io.IOUtils;
import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.WebServiceException;
import org.junit.Before;
import org.junit.Test;

public class GladSegmentationHandlerTest {
    
    private GladSegmentationHandler handler;
    private LocalGenePatternClientStub clientStub = new LocalGenePatternClientStub();
    
    @Before
    public void setUp() {
        handler = new GladSegmentationHandler(clientStub);
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
        assertEquals(2.06, (float) segmentData.getSegmentValue(), 0.001);
    }
    
    private static class LocalGenePatternClientStub extends GenePatternClientStub {
        
        @Override
        public JobInfo runAnalysis(String taskName, List<ParameterInfo> parameters) throws WebServiceException {
            assertEquals("GLAD", taskName);
            assertEquals(2, parameters.size());
            return new JobInfo();
        }

        @Override
        public JobInfo getStatus(JobInfo jobInfo) {
            jobInfo.setStatus("Completed");
            return jobInfo;
        }
        
        @Override
        public File getResultFile(JobInfo jobInfo, String filename) {
            assertEquals("output.glad", filename);
            try {
                File outputFile = File.createTempFile("output", ".glad");
                DataHandler dataHandler = new DataHandler(null, null) {
                    private static final String TEST_OUTPUT = "Sample\tChromosome\tStart.bp\tEnd.bp\tNum.SNPs\tSeg.CN\n"
                        + "1\t17\t41419603\t36581538\t6427\t2.06\n"
                        + "2\t18\t56507051\t10075159\t6732\t1.881\n";
                    
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return new ByteArrayInputStream(TEST_OUTPUT.getBytes());
                    }
                };
                FileOutputStream fileOutputStream;
                fileOutputStream = new FileOutputStream(outputFile);
                IOUtils.copy(dataHandler.getInputStream(), fileOutputStream);
                fileOutputStream.close();
                return outputFile;
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
    }

}
