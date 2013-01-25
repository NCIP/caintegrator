/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class GenericSingleSamplePerFileParserTest {

    @Test
    public void testExtractData() throws DataRetrievalException {

        Map<String, List<Float>> dataMap;
        boolean exceptionCaught = false;
        try {
            SupplementalDataFile supplementalDataFile = new SupplementalDataFile();
            supplementalDataFile.setFileName(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE_PATH);
            supplementalDataFile.setFile(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE);
            supplementalDataFile.setProbeNameHeader("ID");
            supplementalDataFile.setValueHeader("logratio");
            dataMap = GenericSingleSamplePerFileParser.INSTANCE.extractData(supplementalDataFile,
                    PlatformVendorEnum.AGILENT);
        } catch (DataRetrievalException e) {
            assertEquals(e.getMessage(), "Invalid supplemental data file; headers not found in file.");
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);

        exceptionCaught = false;
        try {
            SupplementalDataFile supplementalDataFile = new SupplementalDataFile();
            supplementalDataFile.setFileName(TestDataFiles.HUAITIAN_LEVEL_2_DATA_FILE_PATH);
            supplementalDataFile.setFile(TestDataFiles.HUAITIAN_LEVEL_2_DATA_FILE);
            supplementalDataFile.setProbeNameHeader("ID");
            supplementalDataFile.setValueHeader("logratio");
            dataMap = GenericSingleSamplePerFileParser.INSTANCE.extractData(supplementalDataFile,
                    PlatformVendorEnum.AGILENT);
            assertEquals(4, dataMap.keySet().size());
        } catch (DataRetrievalException e) {
            assertEquals(e.getMessage(), "Invalid supplemental data file; headers not found in file.");
            exceptionCaught = true;
        }
        assertFalse(exceptionCaught);
    }
}
