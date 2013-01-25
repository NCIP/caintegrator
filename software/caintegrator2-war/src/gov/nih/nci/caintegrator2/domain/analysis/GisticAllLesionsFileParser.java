/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.analysis;

import gov.nih.nci.caintegrator2.domain.genomic.AmplificationTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GisticGenomicRegionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Parse the allLesion file from GISTIC result.
 */
public class GisticAllLesionsFileParser {

    private static final float DECIMAL_1000 = 1000.0f;
    private final Map<String, Integer> headerMap = new HashMap<String, Integer>();
    private final Map<Integer, String> sampleMap = new HashMap<Integer, String>();
    private final Map<String, Map<GisticGenomicRegionReporter, Float>> gisticData =
        new HashMap<String, Map<GisticGenomicRegionReporter, Float>>();
    private final Map<String, List<Gene>> ampGeneMap;
    private final Map<String, List<Gene>> delGeneMap;
    private final ReporterList reporterList;
    private int regionReporterIndex;

    private static final String UNIQUE_NAME_HEADER = "Unique Name";
    private static final String DESCRIPTOR_HEADER = "Descriptor";
    private static final String WIDE_PEAK_LIMITS_HEADER = "Wide Peak Limits";
    private static final String PEAK_LIMITS_HEADER = "Peak Limits";
    private static final String REGION_LIMITS_HEADER = "Region Limits";
    private static final String Q_VALUES_HEADER = "q values";
    private static final String RESIDUAL_Q_VALUES_HEADER =
        "Residual q values after removing segments shared with higher peaks";
    private static final String BROAD_FOCAL_HEADER = "Broad or Focal";
    private static final String AMPLITUDE_THRESHOLD_HEADER = "Amplitude Threshold";
    private static final String[] REQUIRED_HEADERS = {UNIQUE_NAME_HEADER, DESCRIPTOR_HEADER,
        WIDE_PEAK_LIMITS_HEADER, PEAK_LIMITS_HEADER, REGION_LIMITS_HEADER, Q_VALUES_HEADER,
        RESIDUAL_Q_VALUES_HEADER, BROAD_FOCAL_HEADER, AMPLITUDE_THRESHOLD_HEADER};

    /**
     * @param ampGeneMap cytoband and amp genes map
     * @param delGeneMap cytoband and delete genes map
     * @param reporterList the reporter list
     */
    public GisticAllLesionsFileParser(Map<String, List<Gene>> ampGeneMap,
            Map<String, List<Gene>> delGeneMap, ReporterList reporterList) {
        super();
        this.ampGeneMap = ampGeneMap;
        this.delGeneMap = delGeneMap;
        this.reporterList = reporterList;
    }

    /**
     * 
     * @param inputFile input file
     * @throws IOException IO exception
     * @throws DataRetrievalException data retrieval exception
     * @return GISTIC data mapping
     */
    public Map<String, Map<GisticGenomicRegionReporter, Float>> parse(File inputFile)
    throws IOException, DataRetrievalException {
        FileReader fileReader = new FileReader(inputFile);
        CSVReader csvReader = new CSVReader(fileReader, '\t');
        loadHeaders(csvReader);
        String[] fields;
        regionReporterIndex = 0;
        while ((fields = csvReader.readNext()) != null) {
            processDataLine(fields);
        }
        csvReader.close();
        fileReader.close();
        FileUtils.deleteQuietly(inputFile);
        return gisticData;
    }
    
    private void processDataLine(String[] fields) {
        if (getField(fields, AMPLITUDE_THRESHOLD_HEADER).equalsIgnoreCase("Actual Log2 Ratio Given")) {
            if (getField(fields, UNIQUE_NAME_HEADER).contains("Amplification")) {
                createRegionReporter(fields, ampGeneMap, AmplificationTypeEnum.AMPLIFIED);
            } else if (getField(fields, UNIQUE_NAME_HEADER).contains("Deletion")) {
                createRegionReporter(fields, delGeneMap, AmplificationTypeEnum.DELETED);
            }
        }
    }

    private void createRegionReporter(String[] fields, Map<String, List<Gene>> geneMap,
            AmplificationTypeEnum type) {
        String boundaries = getBoundaries(fields);
        if (geneMap.containsKey(boundaries)) {
            GisticGenomicRegionReporter regionReporter = new GisticGenomicRegionReporter();
            reporterList.getReporters().add(regionReporter);
            regionReporter.setIndex(regionReporterIndex++);
            regionReporter.setReporterList(reporterList);
            regionReporter.setGeneAmplificationType(type);
            regionReporter.setGenomicDescriptor(getField(fields, DESCRIPTOR_HEADER));
            regionReporter.setBroadOrFocal(getField(fields, BROAD_FOCAL_HEADER));
            regionReporter.setQvalue(roundToThreeDecimalPlaces(getField(fields, Q_VALUES_HEADER)));
            regionReporter.setResidualQValue(roundToThreeDecimalPlaces(
                    getField(fields, RESIDUAL_Q_VALUES_HEADER)));
            regionReporter.setRegionBoundaries(getField(fields, REGION_LIMITS_HEADER));
            regionReporter.setWidePeakBoundaries(getField(fields, WIDE_PEAK_LIMITS_HEADER));
            regionReporter.getGenes().addAll(geneMap.get(boundaries));
            loadGisticData(regionReporter, fields);
        }
    }

    private String getBoundaries(String[] fields) {
        String boundaries = getField(fields, WIDE_PEAK_LIMITS_HEADER);
        boundaries =  boundaries.substring(0, boundaries.indexOf("(probes"));
        return boundaries;
    }

    private String getField(String[] fields, String descriptorHeader) {
        return fields[headerMap.get(descriptorHeader)].trim();
    }

    private void loadGisticData(GisticGenomicRegionReporter regionReporter, String[] fields) {
        for (int i = REQUIRED_HEADERS.length; i < fields.length; i++) {
            if (!StringUtils.isBlank(fields[i])) {
                gisticData.get(sampleMap.get(i)).put(regionReporter, Float.valueOf(fields[i]));
            }
        }
    }

    private void loadHeaders(CSVReader csvReader) throws IOException, DataRetrievalException {
        boolean headerFound = false;
        String[] fields;
        while ((fields = csvReader.readNext()) != null) {
            for (int i = 0; i < REQUIRED_HEADERS.length; i++) {
                if (!fields[i].equalsIgnoreCase(REQUIRED_HEADERS[i])) {
                    break;
                }
                headerMap.put(REQUIRED_HEADERS[i], i);
            }
            headerFound = true;
            break;
        }
        if (!headerFound) {
            throw new DataRetrievalException("Header line not found");
        }
        loadSampleHeaders(fields);
    }

    private void loadSampleHeaders(String[] fields) {
        for (int i = REQUIRED_HEADERS.length; i < fields.length; i++) {
            if (!StringUtils.isBlank(fields[i])) {
                sampleMap.put(i, fields[i]);
                gisticData.put(fields[i], new HashMap<GisticGenomicRegionReporter, Float>());
            }
        }
    }

    private Double roundToThreeDecimalPlaces(String value) {
        float floatValue = Float.valueOf(value);
        return Double.valueOf(Math.round(floatValue * DECIMAL_1000) / DECIMAL_1000);        
    }

}
