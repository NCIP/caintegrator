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
import gov.nih.nci.caintegrator.external.caarray.GenericSingleSamplePerFileParser;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

/**
 * Retrieves and loads single sample per file data from caArray.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@Transactional (propagation = Propagation.REQUIRED)
class ExpressionSingleSamplePerFileMappingFileHandler extends AbstractExpressionMappingFileHandler {
    private static final Logger LOGGER = Logger.getLogger(ExpressionSingleSamplePerFileMappingFileHandler.class);
    private final Map<Sample, List<SupplementalDataFile>> sampleToDataFileMap = Maps.newHashMap();

    ExpressionSingleSamplePerFileMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
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
        Sample sample = getSampleNameToSampleMap().get(sampleName);
        addDataFile(sample, supplementalDataFile);
    }

    private void addDataFile(Sample sample, SupplementalDataFile supplementalDataFile) {
        List<SupplementalDataFile> supplementalDataFiles = sampleToDataFileMap.get(sample);
        if (supplementalDataFiles == null) {
            supplementalDataFiles = new ArrayList<SupplementalDataFile>();
            sampleToDataFileMap.put(sample, supplementalDataFiles);
        }
        supplementalDataFiles.add(supplementalDataFile);
    }

    private void loadArrayDataValues() throws ConnectionException, DataRetrievalException, ValidationException {
        for (Sample sample : sampleToDataFileMap.keySet()) {
            loadArrayDataValues(sample);
        }
    }

    private void loadArrayDataValues(Sample sample)
            throws ConnectionException, DataRetrievalException, ValidationException {
        List<SupplementalDataFile> supplementalDataFiles = new ArrayList<SupplementalDataFile>();
        try {
            for (SupplementalDataFile supplementalDataFile : sampleToDataFileMap.get(sample)) {
                supplementalDataFile.setFile(getDataFile(supplementalDataFile.getFileName()));
                supplementalDataFiles.add(supplementalDataFile);
            }
            loadArrayDataValues(sample, supplementalDataFiles);
        } finally {
            for (SupplementalDataFile supplementalDataFile : supplementalDataFiles) {
                doneWithFile(supplementalDataFile.getFile());
            }
        }
    }

    private void loadArrayDataValues(Sample sample, List<SupplementalDataFile> supplementalDataFiles)
    throws DataRetrievalException, ValidationException {
        ArrayData arrayData = createArrayData(sample);
        getDao().save(arrayData);
        for (SupplementalDataFile supplementalDataFile : supplementalDataFiles) {
            Map<String, float[]> dataMap = GenericSingleSamplePerFileParser.INSTANCE.extractData(
                    supplementalDataFile, getPlatformHelper().getPlatform().getVendor());
            loadArrayDataValues(dataMap, arrayData);
        }
        getArrayDataService().save(getArrayDataValues());
    }

    protected void loadArrayDataValues(Map<String, float[]> dataMap, ArrayData arrayData) {
        for (String probeName : dataMap.keySet()) {
            AbstractReporter reporter = getPlatformHelper().getReporter(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET,
                    probeName);
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
