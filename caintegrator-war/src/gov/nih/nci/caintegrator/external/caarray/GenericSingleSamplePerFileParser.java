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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Parse supplemental data file.
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
    public Map<String, List<Float>> extractData(SupplementalDataFile supplementalDataFile, PlatformVendorEnum vendor)
    throws DataRetrievalException {
        try {
            
            dataFileReader = new CSVReader(new InputStreamReader(
                    new FileInputStream(supplementalDataFile.getFile())), '\t');
            loadHeaders(supplementalDataFile.getProbeNameHeader());
            Map<String, List<Float>> dataMap = new HashMap<String, List<Float>>();
            String[] fields;
            while ((fields = Cai2Util.readDataLine(dataFileReader)) != null) {
                String probeName = fields[headerToIndexMap.get(supplementalDataFile.getProbeNameHeader())];
                extractValue(supplementalDataFile, vendor, dataMap, fields, probeName);
            }
            return dataMap;
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read supplemental data file.", e);
        }
    }

    private void extractValue(SupplementalDataFile supplementalDataFile, PlatformVendorEnum vendor,
            Map<String, List<Float>> dataMap, String[] fields, String probeName) {
        if (PlatformVendorEnum.AGILENT.equals(vendor) && probeName.startsWith("A_")
                || !PlatformVendorEnum.AGILENT.equals(vendor)) {
            Float value;
            try {
                value = new Float(fields[headerToIndexMap.get(supplementalDataFile.getValueHeader())]);
                addValueToDataMap(dataMap, probeName, value);
            } catch (NumberFormatException e) {
                value = 0.0f; // The value is missing ignore this reporter.
            }
        }
    }
    
    private void addValueToDataMap(Map<String, List<Float>> dataMap, String probeName, Float value) {
        if (!dataMap.containsKey(probeName)) {
            dataMap.put(probeName, new ArrayList<Float>());
        }
        dataMap.get(probeName).add(value);
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
