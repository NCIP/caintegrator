/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.util.Map;

import org.junit.Test;

@SuppressWarnings("PMD")
public class AgilentLevelTwoDataMultiFileParserTest {
    
    @Test
    public void testExtractData() throws DataRetrievalException {
        
        Map<String, Float> agilentData;
        boolean exceptionCaught = false;
        try {
            agilentData = AgilentLevelTwoDataMultiFileParser.INSTANCE.extractData(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE);
        } catch (DataRetrievalException e) {
            assertEquals(e.getMessage(), "Invalid Agilent data file; headers not found in file.");
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
        
        exceptionCaught = false;
        try {
            agilentData = AgilentLevelTwoDataMultiFileParser.INSTANCE.extractData(
                TestDataFiles.HUAITIAN_LEVEL_2_DATA_FILE);
            assertEquals(4, agilentData.keySet().size());
        } catch (DataRetrievalException e) {
            assertEquals(e.getMessage(), "Invalid Agilent data file; headers not found in file.");
            exceptionCaught = true;
        }
        assertFalse(exceptionCaught);
    }
}
