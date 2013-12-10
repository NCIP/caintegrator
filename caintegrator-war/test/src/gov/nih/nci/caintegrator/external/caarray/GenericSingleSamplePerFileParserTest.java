/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for parsing supplemental single sample per file data.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenericSingleSamplePerFileParserTest {
    /**
     * Expected exception.
     */
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    /**
     * Tests loading of an invalid data file.
     * @throws DataRetrievalException on data retrieval error
     */
    @Test
    public void invalidDataFile() throws DataRetrievalException {
        expectedException.expect(DataRetrievalException.class);
        expectedException.expectMessage("Invalid supplemental data file; headers not found in file.");

        SupplementalDataFile supplementalDataFile = new SupplementalDataFile();
        supplementalDataFile.setFileName(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE_PATH);
        supplementalDataFile.setFile(TestDataFiles.SHORT_AGILENT_COPY_NUMBER_FILE);
        supplementalDataFile.setProbeNameHeader("ID");
        supplementalDataFile.setValueHeader("logratio");
        GenericSingleSamplePerFileParser.INSTANCE.extractData(supplementalDataFile, PlatformVendorEnum.AGILENT);
    }

    /**
     * Tests loading of a valid data file.
     * @throws DataRetrievalException on an unexpected data retrieval error
     */
    @Test
    public void validDataFile() throws DataRetrievalException {
        SupplementalDataFile supplementalDataFile = new SupplementalDataFile();
        supplementalDataFile.setFileName(TestDataFiles.HUAITIAN_LEVEL_2_DATA_FILE_PATH);
        supplementalDataFile.setFile(TestDataFiles.HUAITIAN_LEVEL_2_DATA_FILE);
        supplementalDataFile.setProbeNameHeader("ID");
        supplementalDataFile.setValueHeader("logratio");
        Map<String, float[]> dataMap = GenericSingleSamplePerFileParser.INSTANCE.extractData(supplementalDataFile,
                PlatformVendorEnum.AGILENT);
        assertEquals(4, dataMap.keySet().size());
    }
}
