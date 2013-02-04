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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
class AgilentCopyNumberMappingSingleFileHandler extends AbstractCopyNumberMappingFileHandler {
    
    static final String FILE_TYPE = "data";
    
    private static final Logger LOGGER = Logger.getLogger(AgilentCopyNumberMappingSingleFileHandler.class);
    
    AgilentCopyNumberMappingSingleFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    void doneWithFile(File dataFile) {
        dataFile.delete();
    }

    @Override
    List<ArrayDataValues> loadArrayData() 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<ArrayDataValues> arrayDataValuesList = new ArrayList<ArrayDataValues>();
        File dataFile = getDataFile(getDataFileName());
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
        List<String> samples = new ArrayList<String>();
        for (Sample sample : getSampleToFilenamesMap().keySet()) {
            samples.add(sample.getName());
        }
        return samples;
    }

    private Sample getSample(String sampleName) {
        for (Sample sample : getSampleToFilenamesMap().keySet()) {
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
                    values.setFloatValue(arrayData, reporter, ArrayDataValueType.COPY_NUMBER_LOG2_RATIO, reporterMap
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

    private String getDataFileName() {
        return getSampleToFilenamesMap().entrySet().iterator().next().getValue().get(0);
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

 }
