/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.GenericMultiSamplePerFileParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class GenericMultiSamplePerFileParserTest {

    private String sample1 = "TCGA-13-0805-01A-01D-0357-04";
    private String sample2 = "TCGA-13-0805-10A-01D-0357-04";
    private GenericMultiSamplePerFileParser parser;

    @Test
    public void testExtractData() throws DataRetrievalException, IOException {

        Map<String, Map<String, List<Float>>> dataMap =  new HashMap<String, Map<String, List<Float>>>();
        List<String> sampleList = new ArrayList<String>();
        sampleList.add(sample1);
        sampleList.add(sample2);
        boolean exceptionCaught = false;
        try {
            parser = new GenericMultiSamplePerFileParser(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE,
                    "ProbeID", "Hybridization Ref", sampleList);
            parser.loadData(dataMap);
        } catch (DataRetrievalException e) {
            assertEquals(e.getMessage(), "Invalid supplemental data file; headers not found in file.");
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);

        parser = new GenericMultiSamplePerFileParser(TestDataFiles.TCGA_LEVEL_2_DATA_FILE,
                "ProbeID", "Hybridization Ref", sampleList);
        parser.loadData(dataMap);
        assertEquals(2, dataMap.keySet().size());
        for (Map<String, List<Float>> reporterList : dataMap.values()) {
            assertTrue(reporterList.keySet().size() > 0);
        }
    }

    @Test
    public void testExtractDataFileWithOneHeaderLine() throws DataRetrievalException, IOException {

        Map<String, Map<String, List<Float>>> dataMap =  new HashMap<String, Map<String, List<Float>>>();
        List<String> sampleList = new ArrayList<String>();
        sampleList.add(sample1);
        sampleList.add(sample2);

        parser = new GenericMultiSamplePerFileParser(TestDataFiles.TCGA_LEVEL_2_DATA_FILE_ONE_HEADER,
                "ProbeID", "ProbeID", sampleList);
        parser.loadData(dataMap);
        assertEquals(2, dataMap.keySet().size());
        for (Map<String, List<Float>> reporterList : dataMap.values()) {
            assertTrue(reporterList.keySet().size() > 0);
        }
    }

    @Test
    public void testExtractMultiDataPoint() throws DataRetrievalException, IOException {

        Map<String, Map<String, List<Float>>> dataMap =  new HashMap<String, Map<String, List<Float>>>();
        List<String> sampleList = new ArrayList<String>();
        sampleList.add(sample1);
        sampleList.add(sample2);
        boolean exceptionCaught = false;
        try {
            parser = new GenericMultiSamplePerFileParser(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE,
                    "ProbeID", "Hybridization Ref", sampleList);
            parser.loadMultiDataPoint(dataMap);
        } catch (DataRetrievalException e) {
            assertEquals(e.getMessage(), "Invalid supplemental data file; headers not found in file.");
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);

        parser = new GenericMultiSamplePerFileParser(TestDataFiles.TCGA_LEVEL_2_DATA_FILE,
                "ProbeID", "Hybridization Ref", sampleList);
        parser.loadMultiDataPoint(dataMap);
        assertEquals(2, dataMap.keySet().size());
        for (Map<String, List<Float>> reporterList : dataMap.values()) {
            assertTrue(reporterList.keySet().size() > 0);
        }
    }
}
