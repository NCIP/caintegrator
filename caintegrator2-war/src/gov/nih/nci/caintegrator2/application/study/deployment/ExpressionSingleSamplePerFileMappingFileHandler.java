/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.GenericSingleSamplePerFileParser;
import gov.nih.nci.caintegrator2.external.caarray.SupplementalDataFile;

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
class ExpressionSingleSamplePerFileMappingFileHandler extends AbstractExpressionMappingFileHandler {

    private static final Logger LOGGER = Logger.getLogger(ExpressionSingleSamplePerFileMappingFileHandler.class);
    
    private final Map<Sample, List<SupplementalDataFile>> sampleToDataFileMap =
        new HashMap<Sample, List<SupplementalDataFile>>();
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

    void mappingSample(String subjectId, String sampleName, SupplementalDataFile supplementalDataFile) 
    throws ValidationException, FileNotFoundException {
        Sample sample = getGenomicSource().getSample(sampleName);
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

    private void loadArrayDataValues() 
    throws ConnectionException, DataRetrievalException, ValidationException {
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
            Map<String, List<Float>> dataMap = GenericSingleSamplePerFileParser.INSTANCE.extractData(
                    supplementalDataFile, getPlatformHelper().getPlatform().getVendor());
            loadArrayDataValues(dataMap, arrayData);
        }
        getArrayDataService().save(getArrayDataValues());
    }
    
    protected void loadArrayDataValues(Map<String, List<Float>> dataMap, ArrayData arrayData) {
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
