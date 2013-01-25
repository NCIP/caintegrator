/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.SupplementalDataFile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
class AgilentCopyNumberMappingMultiFileHandler extends AbstractDnaAnalysisMappingFileHandler {
    
    static final String FILE_TYPE = "data";
    private final Map<Sample, List<SupplementalDataFile>> sampleToDataFileMap =
        new HashMap<Sample, List<SupplementalDataFile>>();
    private final CentralTendencyCalculator centralTendencyCalculator;
    
    
    AgilentCopyNumberMappingMultiFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
        this.centralTendencyCalculator = new CentralTendencyCalculator(
                genomicSource.getTechnicalReplicatesCentralTendency(), 
                genomicSource.isUseHighVarianceCalculation(), 
                genomicSource.getHighVarianceThreshold(), 
                genomicSource.getHighVarianceCalculationType());
    }

    List<ArrayDataValues> loadArrayData() throws DataRetrievalException, ConnectionException, ValidationException {
        try {
            CSVReader reader = new CSVReader(new FileReader(getMappingFile()));
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                String subjectId = fields[0].trim();
                String sampleName = fields[1].trim();
                SupplementalDataFile supplementalDataFile = new SupplementalDataFile();
                supplementalDataFile.setFileName(fields[2].trim());
                supplementalDataFile.setProbeNameHeader(fields[3].trim());
                supplementalDataFile.setValueHeader(fields[4].trim());
                mappingSample(subjectId, sampleName, supplementalDataFile);
            }
            List<ArrayDataValues> arrayDataValues = loadArrayDataValues();
            getDao().save(getGenomicSource().getStudyConfiguration());
            reader.close();
            return arrayDataValues;
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Copy number mapping file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read copy number mapping file: ", e);
        }
    }

    private void mappingSample(String subjectIdentifier, String sampleName, SupplementalDataFile supplementalDataFile) 
    throws ValidationException, FileNotFoundException {
        Sample sample = getSample(sampleName, subjectIdentifier);
        addCopyNumberFile(sample, supplementalDataFile);
    }

    private void addCopyNumberFile(Sample sample, SupplementalDataFile supplementalDataFile) {
        List<SupplementalDataFile> supplementalDataFiles = sampleToDataFileMap.get(sample);
        if (supplementalDataFiles == null) {
            supplementalDataFiles = new ArrayList<SupplementalDataFile>();
            sampleToDataFileMap.put(sample, supplementalDataFiles);
        }
        supplementalDataFiles.add(supplementalDataFile);
    }

    private List<ArrayDataValues> loadArrayDataValues() 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<ArrayDataValues> values = new ArrayList<ArrayDataValues>();
        for (Sample sample : sampleToDataFileMap.keySet()) {
            values.add(loadArrayDataValues(sample));
        }
        return values;
    }

    private ArrayDataValues loadArrayDataValues(Sample sample) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<SupplementalDataFile> supplementalDataFiles = new ArrayList<SupplementalDataFile>();
        try {
            for (SupplementalDataFile supplementalDataFile : sampleToDataFileMap.get(sample)) {
                supplementalDataFile.setFile(getDataFile(supplementalDataFile.getFileName()));
                supplementalDataFiles.add(supplementalDataFile);
            }
            return loadArrayDataValues(sample, supplementalDataFiles);
        } finally {
            for (SupplementalDataFile supplementalDataFile : supplementalDataFiles) {
                doneWithFile(supplementalDataFile.getFile());
            }
        }
    }

    private ArrayDataValues loadArrayDataValues(Sample sample, List<SupplementalDataFile> supplementalDataFiles) 
    throws DataRetrievalException, ValidationException {
        PlatformHelper platformHelper = new PlatformHelper(getDao().getPlatform(getGenomicSource().getPlatformName()));
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        ArrayData arrayData = createArrayData(sample, reporterLists);
        getDao().save(arrayData);
        ArrayDataValues values = 
            new ArrayDataValues(platformHelper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
        for (SupplementalDataFile supplementalDataFile : supplementalDataFiles) {
            AgilentCopyNumberDataRetrieval.INSTANCE.parseDataFile(supplementalDataFile, values,
                    arrayData, platformHelper, centralTendencyCalculator);
        }
        getArrayDataService().save(values);
        return values;
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

 }
