/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.caarray.GenericMultiSamplePerFileParser;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
class ExpressionMultiSamplePerFileMappingFileHandler extends AbstractExpressionMappingFileHandler {

    private static final Logger LOGGER = Logger.getLogger(ExpressionSingleSamplePerFileMappingFileHandler.class);

    private final List<Sample> samples = new ArrayList<Sample>();
    private final List<String> dataFileNames = new ArrayList<String>();
    private final List<SupplementalDataFile> dataFiles = new ArrayList<SupplementalDataFile>();

    ExpressionMultiSamplePerFileMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    ArrayDataValues loadArrayData() throws DataRetrievalException, ConnectionException, ValidationException {
        loadMappingFile();
        loadArrayDataValues();
        getDao().save(getGenomicSource().getStudyConfiguration());
        return getArrayDataValues();
    }

    @Override
    void mappingSample(String subjectId, String sampleName, SupplementalDataFile supplementalDataFile)
    throws ValidationException, FileNotFoundException {
        samples.add(getGenomicSource().getSample(sampleName));
        if (!dataFileNames.contains(supplementalDataFile.getFileName())) {
            dataFileNames.add(supplementalDataFile.getFileName());
            dataFiles.add(supplementalDataFile);
        }
    }

    private void loadArrayDataValues() throws ConnectionException, DataRetrievalException, ValidationException {
        Map<String, Map<String, float[]>> dataMap = extractData();
        for (String sampleName : dataMap.keySet()) {
            loadArrayDataValues(getSample(sampleName), dataMap.get(sampleName));
        }
    }

    private Map<String, Map<String, float[]>> extractData()
            throws DataRetrievalException, ConnectionException, ValidationException {
        Map<String, Map<String, float[]>> dataMap = new HashMap<String, Map<String, float[]>>();
        for (SupplementalDataFile dataFile : dataFiles) {
            GenericMultiSamplePerFileParser parser = new GenericMultiSamplePerFileParser(
                    getDataFile(dataFile.getFileName()), dataFile.getProbeNameHeader(),
                    dataFile.getSampleHeader(), getSampleList());
            parser.loadMultiDataPoint(dataMap);
        }
        validateSampleMapping(dataMap, getSampleList());
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

    private void loadArrayDataValues(Sample sample, Map<String, float[]> dataMap)
    throws DataRetrievalException, ValidationException {
        ArrayData arrayData = createArrayData(sample);
        getDao().save(arrayData);
        loadArrayDataValues(dataMap, arrayData);
        getArrayDataService().save(getArrayDataValues());
    }

    protected void loadArrayDataValues(Map<String, float[]> dataMap, ArrayData arrayData) {
        for (String probeName : dataMap.keySet()) {
            AbstractReporter reporter =
                    getPlatformHelper().getReporter(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, probeName);
            if (reporter == null) {
                LOGGER.warn("Reporter with name " + probeName + " was not found in platform "
                        + getPlatformHelper().getPlatform().getName());
            } else {
                getArrayDataValues().setFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL,
                        dataMap.get(probeName), getCentralTendencyCalculator());
            }
        }
    }

 }
