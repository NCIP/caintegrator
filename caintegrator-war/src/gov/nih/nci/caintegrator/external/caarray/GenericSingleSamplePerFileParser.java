/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Parser for single sample per file supplemental data.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public final class GenericSingleSamplePerFileParser {

    /**
     * The INSTANCE of the GenericSingleSamplePerFileParser.
     */
    public static final GenericSingleSamplePerFileParser INSTANCE = new GenericSingleSamplePerFileParser();

    private CSVReader dataFileReader;
    private final Map<String, Integer> headerToIndexMap = new HashMap<String, Integer>();

    /**
     * Extract data from the raw file.
     * @param supplementalDataFile the supplemental data file detail
     * @param vendor the platform vendor
     * @return the extracted data.
     * @throws DataRetrievalException when error parsing.
     */
    public Map<String, float[]> extractData(SupplementalDataFile supplementalDataFile, PlatformVendorEnum vendor)
    throws DataRetrievalException {
        try {
            dataFileReader = new CSVReader(new FileReader(supplementalDataFile.getFile()), '\t');
            loadHeaders(supplementalDataFile.getProbeNameHeader());
            Map<String, float[]> dataMap = new HashMap<String, float[]>();
            String[] fields;
            while ((fields = Cai2Util.readDataLine(dataFileReader)) != null) {
                String probeName = fields[headerToIndexMap.get(supplementalDataFile.getProbeNameHeader())];
                extractValue(supplementalDataFile, vendor, dataMap, fields, probeName);
            }
            return dataMap;
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read supplemental data file.", e);
        } finally {
            dataFileReader = null;
        }
    }

    private void extractValue(SupplementalDataFile supplementalDataFile, PlatformVendorEnum vendor,
            Map<String, float[]> dataMap, String[] fields, String probeName) {
        String valueField = StringUtils.trim(fields[headerToIndexMap.get(supplementalDataFile.getValueHeader())]);
        if ((PlatformVendorEnum.AGILENT == vendor && probeName.startsWith("A_") || PlatformVendorEnum.AGILENT != vendor)
                && NumberUtils.isNumber(valueField)) {
            dataMap.put(probeName, ArrayUtils.add(dataMap.get(probeName), NumberUtils.toFloat(valueField)));
        }
    }

    private void loadHeaders(String probeNameHeader) throws IOException, DataRetrievalException {
        String[] fields;
        while ((fields = dataFileReader.readNext()) != null) {
            if (isFeatureHeadersLine(fields, probeNameHeader)) {
                loadFeatureHeaders(fields);
                return;
            }
        }
        throw new DataRetrievalException("Invalid supplemental data file; headers not found in file.");
    }

    private boolean isFeatureHeadersLine(String[] fields, String probeNameHeader) {
        return fields.length > 0 && probeNameHeader.equals(fields[0]);
    }

    private void loadFeatureHeaders(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            headerToIndexMap.put(headers[i], i);
        }
    }
}
