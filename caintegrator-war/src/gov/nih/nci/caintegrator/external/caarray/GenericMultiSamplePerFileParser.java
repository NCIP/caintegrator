/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Reads data in supplemental data file.
 */
public final class GenericMultiSamplePerFileParser {
    private CSVReader dataFileReader;
    private final Map<String, Integer> sampleToIndexMap = new HashMap<String, Integer>();

    /**
     * @param dataFile data file
     * @param sampleList list of samples to process
     * @param probeHeader the probe header
     * @param sampleHeader the sample header
     * @throws DataRetrievalException data retrieval exception
     */
    public GenericMultiSamplePerFileParser(File dataFile, String probeHeader, String sampleHeader,
            List<String> sampleList) throws DataRetrievalException {
        createDataFileReader(dataFile, probeHeader, sampleHeader, sampleList);
    }

    private void createDataFileReader(File dataFile, String probeHeader, String sampleHeader, List<String> sampleList)
    throws DataRetrievalException {
        try {
            dataFileReader = new CSVReader(new InputStreamReader(new FileInputStream(dataFile)), '\t');
            loadHeaders(probeHeader, sampleHeader, sampleList);
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Supplemental file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read supplemental file: ", e);
        }
    }

    /**
     * @param dataMap the data mapping
     * @throws DataRetrievalException read data exception
     */
    public void loadData(Map<String, Map<String, float[]>> dataMap) throws DataRetrievalException {
        String[] fields;
        try {
            while ((fields = Cai2Util.readDataLine(dataFileReader)) != null) {
                String probeName = fields[0];
                for (String sampleName : sampleToIndexMap.keySet()) {
                    String valueField = StringUtils.trim(fields[sampleToIndexMap.get(sampleName)]);
                    if (NumberUtils.isNumber(valueField)) {
                        setReporterMap(dataMap, sampleName, probeName,  NumberUtils.toFloat(valueField));
                    }
                }
            }
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read supplemental file.", e);
        }
    }

    /**
     * @param dataMap the data mapping
     * @throws DataRetrievalException read data exception
     */
    public void loadMultiDataPoint(Map<String, Map<String, float[]>> dataMap) throws DataRetrievalException {
        String[] fields;
        try {
            while ((fields = Cai2Util.readDataLine(dataFileReader)) != null) {
                String probeName = fields[0];
                for (String sampleName : sampleToIndexMap.keySet()) {
                    String valueField = StringUtils.trim(fields[sampleToIndexMap.get(sampleName)]);
                    if (NumberUtils.isNumber(valueField)) {
                        setMultiPointReporterMap(dataMap, sampleName, probeName, NumberUtils.toFloat(valueField));
                    }
                }
            }
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read supplemental file.", e);
        }
    }

    private void setReporterMap(Map<String, Map<String, float[]>> dataMap, String sampleName, String probeName,
            float log2Ratio) {
        if (!dataMap.containsKey(sampleName)) {
            Map<String, float[]> reporterMap = new HashMap<String, float[]>();
            dataMap.put(sampleName, reporterMap);
        }
        Map<String, float[]> reporterMap = dataMap.get(sampleName);
        if (!reporterMap.containsKey(probeName)) {
            reporterMap.put(probeName, new float[]{});
        }
        dataMap.get(sampleName).put(probeName, ArrayUtils.add(dataMap.get(sampleName).get(probeName), log2Ratio));
    }

    private void setMultiPointReporterMap(Map<String, Map<String, float[]>> dataMap, String sampleName,
            String probeName, float log2Ratio) {
        if (!dataMap.containsKey(sampleName)) {
            Map<String, float[]> reporterMap = new HashMap<String, float[]>();
            dataMap.put(sampleName, reporterMap);
        }
        addValueToDataMap(dataMap.get(sampleName), probeName, log2Ratio);
    }

    private void addValueToDataMap(Map<String, float[]> dataMap, String probeName, float value) {
        dataMap.put(probeName, ArrayUtils.add(dataMap.get(probeName), value));
    }

    private void loadHeaders(String probeHeader, String sampleHeader, List<String> sampleList)
    throws IOException, DataRetrievalException {
        sampleToIndexMap.clear();
        String[] fields = Cai2Util.readDataLine(dataFileReader);
        checkHeadersLine(fields, sampleHeader);
        loadSampleHeaders(fields, sampleList);
        if (!probeHeader.equalsIgnoreCase(sampleHeader)) {
            fields = Cai2Util.readDataLine(dataFileReader);
            checkHeadersLine(fields, probeHeader);
        }
    }

    private void loadSampleHeaders(String[] headers, List<String> sampleList) {
        for (int i = 0; i < headers.length; i++) {
            if (sampleList.contains(headers[i])) {
                sampleToIndexMap.put(headers[i], i);
            }
        }
    }

    private void checkHeadersLine(String[] fields, String keyword) throws DataRetrievalException {
        if (fields == null || !keyword.equals(fields[0])) {
            throw new DataRetrievalException("Invalid supplemental data file; headers not found in file.");
        }
    }
}
