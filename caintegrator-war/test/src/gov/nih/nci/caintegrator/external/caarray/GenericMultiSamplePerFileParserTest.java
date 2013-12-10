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

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for the generic multi sample per file parser.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenericMultiSamplePerFileParserTest {
    /**
     * Expected exception.
     */
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();
    private static final String PROBE_HEADER = "ProbeID";
    private static final String SAMPLE_HEADER = "Hybridization Ref";
    private static final String SAMPLE_ONE = "TCGA-13-0805-01A-01D-0357-04";
    private static final String SAMPLE_TWO = "TCGA-13-0805-10A-01D-0357-04";
    private GenericMultiSamplePerFileParser parser;

    /**
     * Tests loading invalid data.
     * @throws DataRetrievalException on error
     * @throws IOException on error
     */
    @Test
    public void loadInvalidData() throws DataRetrievalException, IOException {
        expectedException.expect(DataRetrievalException.class);
        expectedException.expectMessage("Invalid supplemental data file; headers not found in file.");

        Map<String, Map<String, float[]>> dataMap =  new HashMap<String, Map<String, float[]>>();
        parser = new GenericMultiSamplePerFileParser(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE,
                PROBE_HEADER, SAMPLE_HEADER, Arrays.asList(SAMPLE_ONE, SAMPLE_TWO));
        parser.loadData(dataMap);
    }

    /**
     * Tests loading valid data.
     * @throws DataRetrievalException on unexepected error
     * @throws IOException on unexpected error
     */
    @Test
    public void loadValidData() throws DataRetrievalException, IOException {
        Map<String, Map<String, float[]>> dataMap =  new HashMap<String, Map<String, float[]>>();
        parser = new GenericMultiSamplePerFileParser(TestDataFiles.TCGA_LEVEL_2_DATA_FILE,
                PROBE_HEADER, SAMPLE_HEADER, Arrays.asList(SAMPLE_ONE, SAMPLE_TWO));
        parser.loadData(dataMap);
        assertEquals(2, dataMap.keySet().size());
        for (Map<String, float[]> reporterList : dataMap.values()) {
            assertTrue(reporterList.keySet().size() > 0);
        }
    }

    /**
     * Tests loading valid data with a single header line.
     * @throws DataRetrievalException on unexepected error
     * @throws IOException on unexpected error
     */
    @Test
    public void testExtractDataFileWithOneHeaderLine() throws DataRetrievalException, IOException {
        Map<String, Map<String, float[]>> dataMap =  new HashMap<String, Map<String, float[]>>();

        parser = new GenericMultiSamplePerFileParser(TestDataFiles.TCGA_LEVEL_2_DATA_FILE_ONE_HEADER,
                PROBE_HEADER, PROBE_HEADER, Arrays.asList(SAMPLE_ONE, SAMPLE_TWO));
        parser.loadData(dataMap);
        assertEquals(2, dataMap.keySet().size());
        for (Map<String, float[]> reporterList : dataMap.values()) {
            assertTrue(reporterList.keySet().size() > 0);
        }
    }

    /**
     * Tests loading invalid multi-point data.
     * @throws DataRetrievalException on unexepected error
     * @throws IOException on unexpected error
     */
    @Test
    public void loadInvalidMultiDataPoint() throws DataRetrievalException, IOException {
        expectedException.expect(DataRetrievalException.class);
        expectedException.expectMessage("Invalid supplemental data file; headers not found in file.");

        Map<String, Map<String, float[]>> dataMap =  new HashMap<String, Map<String, float[]>>();
        parser = new GenericMultiSamplePerFileParser(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE,
                    PROBE_HEADER, SAMPLE_HEADER, Arrays.asList(SAMPLE_ONE, SAMPLE_TWO));
        parser.loadMultiDataPoint(dataMap);
    }

    /**
     * Tests loading valid multi-point data.
     * @throws DataRetrievalException on unexepected error
     * @throws IOException on unexpected error
     */
    @Test
    public void loadValidMultiDataPoint() throws DataRetrievalException, IOException {
        Map<String, Map<String, float[]>> dataMap =  new HashMap<String, Map<String, float[]>>();
        parser = new GenericMultiSamplePerFileParser(TestDataFiles.TCGA_LEVEL_2_DATA_FILE, PROBE_HEADER,
                SAMPLE_HEADER, Arrays.asList(SAMPLE_ONE, SAMPLE_TWO));
        parser.loadMultiDataPoint(dataMap);
        assertEquals(2, dataMap.keySet().size());
        for (Map<String, float[]> reporterList : dataMap.values()) {
            assertTrue(reporterList.keySet().size() > 0);
        }
    }
}
