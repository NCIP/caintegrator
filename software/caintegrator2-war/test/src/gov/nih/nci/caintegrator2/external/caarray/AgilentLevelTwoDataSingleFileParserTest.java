/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

@SuppressWarnings("PMD")
public class AgilentLevelTwoDataSingleFileParserTest {

    private String sample1 = "TCGA-13-0805-01A-01D-0357-04";
    private String sample2 = "TCGA-13-0805-10A-01D-0357-04";
    private String sample3 = "BAD_1";
    private String sample4 = "BAD_2";
    
    @Test
    public void testExtractData() throws DataRetrievalException {
        
        Map<String, Map<String, Float>> agilentData;
        List<String> sampleList = new ArrayList<String>();
        sampleList.add(sample1);
        sampleList.add(sample2);
        boolean exceptionCaught = false;
        try {
            agilentData = AgilentLevelTwoDataSingleFileParser.INSTANCE.extractData(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE, sampleList);
        } catch (DataRetrievalException e) {
            assertEquals(e.getMessage(), "Invalid header for Agilent data file.");
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        agilentData = AgilentLevelTwoDataSingleFileParser.INSTANCE.extractData(
                TestDataFiles.TCGA_LEVEL_2_DATA_FILE, sampleList);
        assertEquals(2, agilentData.keySet().size());
        for (Map<String, Float> reporterList : agilentData.values()) {
            assertTrue(reporterList.keySet().size() > 0);
        }

        sampleList.add(sample3);
        sampleList.add(sample4);
        exceptionCaught = false;
        try {
            agilentData = AgilentLevelTwoDataSingleFileParser.INSTANCE.extractData(
                TestDataFiles.TCGA_LEVEL_2_DATA_FILE, sampleList);
        } catch (DataRetrievalException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }
}
