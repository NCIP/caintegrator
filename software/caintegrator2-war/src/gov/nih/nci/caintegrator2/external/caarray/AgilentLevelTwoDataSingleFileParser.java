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
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Reads data in Agilent data file.
 */
public final class AgilentLevelTwoDataSingleFileParser {

    /**
     * The INSTANCE of the AgilentLevelTwoDataSingleFileParser.
     */
    public static final AgilentLevelTwoDataSingleFileParser INSTANCE = new AgilentLevelTwoDataSingleFileParser();
    
    private CSVReader dataFileReader;
    private static final String SAMPLE_HEADER = "Hybridization Ref";
    private static final String PROBE_NAME = "ProbeID";
    private final Map<String, Integer> sampleToIndexMap = new HashMap<String, Integer>();
    
    /**
     * Extract data from the data file.
     * @param dataFile the data file.
     * @param sampleList the list of mapped samples
     * @return the extracted data.
     * @throws DataRetrievalException when error parsing.
     */
    public Map<String, Map<String, Float>> extractData(File dataFile, List<String> sampleList)
    throws DataRetrievalException {
        try {
            return extractData(new InputStreamReader(new FileInputStream(dataFile)), sampleList);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read Agilent data file.", e);
        }
    }
    
    private Map<String, Map<String, Float>> extractData(InputStreamReader inputStreamReader,
            List<String> sampleList) throws DataRetrievalException {
        try {
            dataFileReader = new CSVReader(inputStreamReader, '\t');
            loadHeaders(sampleList);
            Map<String, Map<String, Float>> agilentDataMap = new HashMap<String, Map<String, Float>>();
            loadData(agilentDataMap);
            validateSampleMapping(agilentDataMap, sampleList);
            return agilentDataMap;
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read Agilent data file.", e);
        }
    }

    /**
     * @param agilentDataMap
     * @throws IOException
     */
    private void loadData(Map<String, Map<String, Float>> agilentDataMap) throws IOException {
        String[] fields;
        while ((fields = dataFileReader.readNext()) != null) {
            if (fields.length > 1) {
                String probeName = fields[0];
                for (String sampleName : sampleToIndexMap.keySet()) {
                    Float log2Ratio = getLog2Ratio(fields, sampleToIndexMap.get(sampleName));
                    if (log2Ratio != null) {
                        Map<String, Float> reporterMap = getReporterMap(agilentDataMap, sampleName);
                        reporterMap.put(probeName, log2Ratio);
                    }
                }
            }
        }
    }

    private void validateSampleMapping(Map<String, Map<String, Float>> agilentDataMap, List<String> sampleList)
    throws DataRetrievalException {
        StringBuffer errorMsg = new StringBuffer();
        for (String sampleName : sampleList) {
            if (!agilentDataMap.containsKey(sampleName)) {
                if (errorMsg.length() > 0) {
                    errorMsg.append(", ");
                }
                errorMsg.append(sampleName);
            }
        }
        if (errorMsg.length() > 0) {
            throw new DataRetrievalException("Sample not found error: " + errorMsg.toString());
        }
    }
    
    private Float getLog2Ratio(String[] fields, int index) {
        try {
            return new Float(fields[index]);
        } catch (Exception e) {
            return null;
        }
    }
    
    private Map<String, Float> getReporterMap(Map<String, Map<String, Float>> agilentDataMap, String sampleName) {
        if (!agilentDataMap.containsKey(sampleName)) {
            Map<String, Float> reporterMap = new HashMap<String, Float>();
            agilentDataMap.put(sampleName, reporterMap);
        }
        return agilentDataMap.get(sampleName);
    }

    private void loadHeaders(List<String> sampleList) throws IOException, DataRetrievalException {
        String[] fields;
        fields = dataFileReader.readNext();
        checkHeadersLine(fields, SAMPLE_HEADER);
        loadSampleHeaders(fields, sampleList);
        fields = dataFileReader.readNext();
        checkHeadersLine(fields, PROBE_NAME);
    }

    private void loadSampleHeaders(String[] headers, List<String> sampleList) {
        for (int i = 3; i < headers.length; i++) {
            if (sampleList.contains(headers[i])) {
                sampleToIndexMap.put(headers[i], i);
            }
        }
    }
    
    private void checkHeadersLine(String[] fields, String keyword) throws DataRetrievalException {
        if (!keyword.equals(fields[0])) {
            throw new DataRetrievalException("Invalid header for Agilent data file.");
        }
    }
}
