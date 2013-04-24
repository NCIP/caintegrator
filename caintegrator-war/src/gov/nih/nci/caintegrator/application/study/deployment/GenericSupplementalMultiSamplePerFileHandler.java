/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.caarray.GenericMultiSamplePerFileParser;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
class GenericSupplementalMultiSamplePerFileHandler extends AbstractGenericSupplementalMappingFileHandler {

    private final List<Sample> samples = new ArrayList<Sample>();
    private final Map<String, SupplementalDataFile> supplementalDataFiles = new HashMap<String, SupplementalDataFile>();
    private final Map<String, File> dataFiles = new HashMap<String, File>();

    private static final Logger LOGGER = Logger.getLogger(GenericSupplementalMultiSamplePerFileHandler.class);
    private static final int ONE_THOUSAND = 1000;

    GenericSupplementalMultiSamplePerFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    void mappingSample(String subjectIdentifier, String sampleName, SupplementalDataFile supplementalDataFile)
    throws FileNotFoundException, ValidationException, ConnectionException, DataRetrievalException {
        samples.add(getSample(sampleName, subjectIdentifier));
        if (!supplementalDataFiles.containsKey(supplementalDataFile.getFileName())) {
            supplementalDataFiles.put(supplementalDataFile.getFileName(), supplementalDataFile);
            dataFiles.put(supplementalDataFile.getFileName(), getDataFile(supplementalDataFile.getFileName()));
        }
    }

    @Override
    List<ArrayDataValues> loadArrayDataValue() throws DataRetrievalException {
        PlatformHelper platformHelper = new PlatformHelper(getDao().getPlatform(getGenomicSource().getPlatformName()));
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(getReporterType());
        return createArrayDataByBucket(platformHelper, reporterLists);
    }

    private List<ArrayDataValues> createArrayDataByBucket(PlatformHelper platformHelper,
            Set<ReporterList> reporterLists) throws DataRetrievalException {
        List<ArrayDataValues> arrayDataValuesList = new ArrayList<ArrayDataValues>();
        int bucketNum = 0;
        List<List<String>> sampleBuckets = createSampleBuckets(reporterLists, getSampleList());
        for (List<String> sampleBucket : sampleBuckets) {
            LOGGER.info("Starting an extract data on samples of size " + sampleBucket.size()
                    + ", for bucket number " + ++bucketNum + "/" + sampleBuckets.size());
            loadArrayData(arrayDataValuesList, platformHelper, reporterLists, extractData(sampleBucket));
        }
        return arrayDataValuesList;
    }

    private List<List<String>> createSampleBuckets(Set<ReporterList> reporterLists, List<String> sampleList) {
        List<List<String>> sampleBuckets = new ArrayList<List<String>>();
        long sampleBucketSize = computeBucketSize(reporterLists);
        int sampleCount = 0;
        List<String> sampleBucket = new ArrayList<String>();
        for (String sample : sampleList) {
            if (sampleCount++ % sampleBucketSize == 0) {
                sampleBucket = new ArrayList<String>();
                sampleBuckets.add(sampleBucket);
            }
            sampleBucket.add(sample);
        }
        return sampleBuckets;
    }

    private long computeBucketSize(Set<ReporterList> reporterLists) {
        int numReporters = 0;
        for (ReporterList reporterList : reporterLists) {
            numReporters += reporterList.getReporters().size();
        }
        return (ONE_THOUSAND * Cai2Util.getHeapSizeMB()) / numReporters;
    }

    private Map<String, Map<String, float[]>> extractData(List<String> mappedSampleBucket)
            throws DataRetrievalException {
        Map<String, Map<String, float[]>> dataMap = new HashMap<String, Map<String, float[]>>();
        for (SupplementalDataFile dataFile : supplementalDataFiles.values()) {
            GenericMultiSamplePerFileParser parser =
                    new GenericMultiSamplePerFileParser(dataFiles.get(dataFile.getFileName()),
                            dataFile.getProbeNameHeader(), dataFile.getSampleHeader(), mappedSampleBucket);
            parser.loadData(dataMap);
        }
        validateSampleMapping(dataMap, mappedSampleBucket);
        return dataMap;
    }

    private void validateSampleMapping(Map<String, Map<String, float[]>> dataMap, List<String> sampleList)
    throws DataRetrievalException {
        StringBuffer errorMsg = new StringBuffer();
        for (String sampleName : sampleList) {
            if (!dataMap.containsKey(sampleName)) {
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

    private List<String> getSampleList() {
        List<String> sampleNames = new ArrayList<String>();
        for (Sample sample : samples) {
            sampleNames.add(sample.getName());
        }
        return sampleNames;
    }

    private Sample getSample(String sampleName) {
        for (Sample sample : samples) {
            if (sampleName.equals(sample.getName())) {
                return sample;
            }
        }
        return null;
    }

    private void loadArrayData(List<ArrayDataValues> arrayDataValuesList, PlatformHelper platformHelper,
            Set<ReporterList> reporterLists, Map<String, Map<String, float[]>> dataMap) throws DataRetrievalException {
        for (String sampleName : dataMap.keySet()) {
            LOGGER.info("Start LoadArrayData for : " + sampleName);
            ArrayData arrayData = createArrayData(getSample(sampleName), reporterLists, getDataType());
            getDao().save(arrayData);
            ArrayDataValues values = new ArrayDataValues(platformHelper
                    .getAllReportersByType(getReporterType()));
            arrayDataValuesList.add(values);
            Map<String, float[]> reporterMap = dataMap.get(sampleName);
            for (String probeName : reporterMap.keySet()) {
                AbstractReporter reporter = platformHelper.getReporter(getReporterType(), probeName);
                if (reporter == null) {
                    LOGGER.warn("Reporter with name " + probeName + " was not found in platform "
                            + platformHelper.getPlatform().getName());
                } else {
                    values.setFloatValue(arrayData, reporter, getDataValueType(),
                            reporterMap.get(probeName), getCentralTendencyCalculator());
                }
            }
            getArrayDataService().save(values);
            values.clearMaps(); // Fixes a memory leak where the maps never got garbage collected.
            LOGGER.info("Done LoadArrayData for : " + sampleName);
        }
    }
 }
