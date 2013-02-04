/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.AgilentLevelTwoDataSingleFileParser;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
class AgilentCopyNumberMappingSingleFileHandler extends AbstractDnaAnalysisMappingFileHandler {
    
    static final String FILE_TYPE = "data";
    private String dataFileName;
    private final List<Sample> samples = new ArrayList<Sample>();
    
    private static final Logger LOGGER = Logger.getLogger(AgilentCopyNumberMappingSingleFileHandler.class);
    
    AgilentCopyNumberMappingSingleFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    List<ArrayDataValues> loadArrayData()
    throws DataRetrievalException, ConnectionException, ValidationException {
        try {
            CSVReader reader = new CSVReader(new FileReader(getMappingFile()));
            String[] fields;
            samples.clear();
            while ((fields = reader.readNext()) != null) {
                String subjectId = fields[0].trim();
                String sampleName = fields[1].trim();
                dataFileName = fields[2].trim();
                mappingSample(subjectId, sampleName);
            }
            List<ArrayDataValues> arrayDataValues = loadArrayDataValue();
            getDao().save(getGenomicSource().getStudyConfiguration());
            reader.close();
            return arrayDataValues;
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Copy number mapping file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read copy number mapping file: ", e);
        }
    }

    private void mappingSample(String subjectIdentifier, String sampleName) 
    throws ValidationException, FileNotFoundException {
        Sample sample = getSample(sampleName, subjectIdentifier);
        samples.add(sample);
    }

    private List<ArrayDataValues> loadArrayDataValue() 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<ArrayDataValues> arrayDataValuesList = new ArrayList<ArrayDataValues>();
        File dataFile = getDataFile(dataFileName);
        try {
            PlatformHelper platformHelper = new PlatformHelper(getDao().getPlatform(
                    getGenomicSource().getPlatformName()));
            Set<ReporterList> reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
            Map<String, Map<String, Float>> agilentDataMap = AgilentLevelTwoDataSingleFileParser.INSTANCE.extractData(
                    dataFile, getSampleList());
            loadArrayData(arrayDataValuesList, platformHelper, reporterLists, agilentDataMap);
            return arrayDataValuesList;
        } finally {
            doneWithFile(dataFile);
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
            Set<ReporterList> reporterLists, Map<String, Map<String, Float>> agilentDataMap)
    throws DataRetrievalException {
        for (String sampleName : agilentDataMap.keySet()) {
            LOGGER.info("Start LoadArrayData for : " + sampleName);
            ArrayData arrayData = createArrayData(getSample(sampleName), reporterLists);
            getDao().save(arrayData);
            ArrayDataValues values = new ArrayDataValues(platformHelper
                    .getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
            arrayDataValuesList.add(values);
            Map<String, Float> reporterMap = agilentDataMap.get(sampleName);
            for (String probeName : reporterMap.keySet()) {
                AbstractReporter reporter = getReporter(platformHelper, probeName);
                if (reporter == null) {
                    LOGGER.warn("Reporter with name " + probeName + " was not found in platform "
                            + platformHelper.getPlatform().getName());
                } else {
                    values.setFloatValue(arrayData, reporter, ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO, reporterMap
                            .get(probeName).floatValue());
                }
            }
            getArrayDataService().save(values);
            LOGGER.info("Done LoadArrayData for : " + sampleName);
        }
    }

    private AbstractReporter getReporter(PlatformHelper platformHelper, String probeSetName) {
        AbstractReporter reporter = platformHelper.getReporter(ReporterTypeEnum.DNA_ANALYSIS_REPORTER, 
                probeSetName); 
        return reporter;
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

 }
