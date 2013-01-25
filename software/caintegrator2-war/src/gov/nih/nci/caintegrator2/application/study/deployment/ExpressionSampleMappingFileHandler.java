/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.CentralTendencyCalculator;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.SupplementalDataFile;
import gov.nih.nci.caintegrator2.external.caarray.SupplementalMultiFileParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
class ExpressionSampleMappingFileHandler extends AbstractCaArrayFileHandler {

    private static final Logger LOGGER = Logger.getLogger(ExpressionSampleMappingFileHandler.class);
    
    private final CaIntegrator2Dao dao;
    private final CentralTendencyCalculator centralTendencyCalculator;
    static final String FILE_TYPE = "data";
    private final Map<Sample, List<SupplementalDataFile>> sampleToDataFileMap =
        new HashMap<Sample, List<SupplementalDataFile>>();
    private final PlatformHelper platformHelper;
    private final Set<ReporterList> reporterLists;
    private ArrayDataValues arrayDataValues;
    
    
    ExpressionSampleMappingFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService);
        this.dao = dao;
        platformHelper = new PlatformHelper(dao.getPlatform(genomicSource.getPlatformName()));
        reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        arrayDataValues = 
            new ArrayDataValues(platformHelper.getAllReportersByType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET));
        this.centralTendencyCalculator = new CentralTendencyCalculator(
                genomicSource.getTechnicalReplicatesCentralTendency(), 
                genomicSource.isUseHighVarianceCalculation(), 
                genomicSource.getHighVarianceThreshold(), 
                genomicSource.getHighVarianceCalculationType());
    }

    ArrayDataValues loadArrayData() throws DataRetrievalException, ConnectionException, ValidationException {
        try {
            CSVReader reader = new CSVReader(new FileReader(getGenomicSource().getSampleMappingFile()));
            String[] fields;
            while ((fields = reader.readNext()) != null) {
                String sampleName = fields[1].trim();
                SupplementalDataFile supplementalDataFile = new SupplementalDataFile();
                supplementalDataFile.setFileName(fields[2].trim());
                supplementalDataFile.setProbeNameHeader(fields[3].trim());
                supplementalDataFile.setValueHeader(fields[4].trim());
                mappingSample(sampleName, supplementalDataFile);
            }
            loadArrayDataValues();
            dao.save(getGenomicSource().getStudyConfiguration());
            reader.close();
            return arrayDataValues;
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("Sample mapping file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read sample mapping file: ", e);
        }
    }

    private void mappingSample(String sampleName, SupplementalDataFile supplementalDataFile) 
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
        dao.save(arrayData);
        for (SupplementalDataFile supplementalDataFile : supplementalDataFiles) {
            Map<String, List<Float>> dataMap = SupplementalMultiFileParser.INSTANCE.extractData(
                    supplementalDataFile, platformHelper.getPlatform().getVendor());
            loadArrayDataValues(dataMap, arrayData);
        }
        getArrayDataService().save(arrayDataValues);
    }
    
    protected void loadArrayDataValues(Map<String, List<Float>> dataMap, ArrayData arrayData) {
        for (String probeName : dataMap.keySet()) {
            AbstractReporter reporter = getReporter(probeName);
            if (reporter == null) {
                LOGGER.warn("Reporter with name " + probeName + " was not found in platform " 
                        + platformHelper.getPlatform().getName());
            } else {
                arrayDataValues.setFloatValue(arrayData, reporter, ArrayDataValueType.EXPRESSION_SIGNAL,
                        dataMap.get(probeName), centralTendencyCalculator);
            }
        }
    }

    private AbstractReporter getReporter(String probeSetName) {
        AbstractReporter reporter = platformHelper.getReporter(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, 
                probeSetName); 
        return reporter;
    }
    
    private ArrayData createArrayData(Sample sample) {
        Array array = new Array();
        array.setPlatform(platformHelper.getPlatform());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        if (!reporterLists.isEmpty()) {
            arrayData.getReporterLists().addAll(reporterLists);
            for (ReporterList reporterList : reporterLists) {
                reporterList.getArrayDatas().add(arrayData);
            }
        }
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.setStudy(getGenomicSource().getStudyConfiguration().getStudy());
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        dao.save(array);
        return arrayData;
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

    /**
     * @return the arrayDataValues
     */
    public ArrayDataValues getArrayDataValues() {
        return arrayDataValues;
    }

    /**
     * @param arrayDataValues the arrayDataValues to set
     */
    public void setArrayDataValues(ArrayDataValues arrayDataValues) {
        this.arrayDataValues = arrayDataValues;
    }

 }
