/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Parse Agilent level 2 data file.
 */
public final class AgilentLevelTwoDataMultiFileParser {

    /**
     * The INSTANCE of the AgilentRawDataFileParser.
     */
    public static final AgilentLevelTwoDataMultiFileParser INSTANCE = new AgilentLevelTwoDataMultiFileParser();
    
    private CSVReader dataFileReader;
    private static final String PROBE_NAME_HEADER = "ID";
    private static final String LOG_RATIO_HEADER = "logratio";
    private final Map<String, Integer> headerToIndexMap = new HashMap<String, Integer>();
    
    /**
     * Extract data from the raw file.
     * @param inputStreamReader of the raw file
     * @return the extracted data.
     * @throws DataRetrievalException when error parsing.
     */
    public Map<String, Float> extractData(InputStreamReader inputStreamReader) throws DataRetrievalException {
        try {
            dataFileReader = new CSVReader(inputStreamReader, '\t');
            loadHeaders();
            Map<String, Float> agilentDataMap = new HashMap<String, Float>();
            String[] fields;
            while ((fields = dataFileReader.readNext()) != null) {
                String probeName = fields[headerToIndexMap.get(PROBE_NAME_HEADER)];
                if (probeName.startsWith("A_")) {
                    Float logRatio;
                    try {
                        logRatio = new Float(fields[headerToIndexMap.get(LOG_RATIO_HEADER)]);
                        agilentDataMap.put(probeName, logRatio);
                    } catch (NumberFormatException e) {
                        logRatio = 0.0f; // The logratio is missing ignore this reporter.
                    }
                }
            }
            return agilentDataMap;
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read Agilent data file.", e);
        }
    }

    /**
     * Extract data from the raw file.
     * @param dataFile the raw file.
     * @return the extracted data.
     * @throws DataRetrievalException when error parsing.
     */
    public Map<String, Float> extractData(File dataFile) throws DataRetrievalException {
        try {
            return extractData(new InputStreamReader(new FileInputStream(dataFile)));
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read Agilent data file.", e);
        }
    }

    private void loadHeaders() throws IOException, DataRetrievalException {
        String[] fields;
        while ((fields = dataFileReader.readNext()) != null) {
            if (isFeatutreHeadersLine(fields)) {
                loadFeatutreHeaders(fields);
                return;
            }
        }        
        throw new DataRetrievalException("Invalid Agilent data file; headers not found in file.");
    }

    private void loadFeatutreHeaders(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            headerToIndexMap.put(headers[i], i);
        }
    }
    
    private boolean isFeatutreHeadersLine(String[] fields) {
        return fields.length > 0 && PROBE_NAME_HEADER.equals(fields[0]);
    }
}
