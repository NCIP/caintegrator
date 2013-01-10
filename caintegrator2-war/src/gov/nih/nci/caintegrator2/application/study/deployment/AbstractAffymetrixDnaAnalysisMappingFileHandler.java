/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
public abstract class AbstractAffymetrixDnaAnalysisMappingFileHandler
    extends AbstractUnparsedSupplementalMappingFileHandler {

    private final Map<Sample, List<String>> sampleToFilenamesMap = new HashMap<Sample, List<String>>();
    
    AbstractAffymetrixDnaAnalysisMappingFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }
    
    List<ArrayDataValues> loadArrayData() throws DataRetrievalException, ConnectionException, ValidationException {
        try {
            CSVReader reader = new CSVReader(new FileReader(getMappingFile()));
            String[] fields;
            while ((fields = Cai2Util.readDataLine(reader)) != null) {
                String subjectId = fields[0].trim();
                String sampleName = fields[1].trim();
                String dnaAnalysisFilename = fields[2].trim();
                mappingSample(subjectId, sampleName, dnaAnalysisFilename);
            }
            List<ArrayDataValues> arrayDataValues = loadArrayDataValues();
            getDao().save(getGenomicSource().getStudyConfiguration());
            reader.close();
            return arrayDataValues;
        } catch (FileNotFoundException e) {
            throw new DataRetrievalException("DNA analysis mapping file not found: ", e);
        } catch (IOException e) {
            throw new DataRetrievalException("Couldn't read DNA analysis mapping file: ", e);
        }
    }

    private void mappingSample(String subjectIdentifier, String sampleName, String dnaAnalysisFilename) 
    throws ValidationException, FileNotFoundException {
        Sample sample = getSample(sampleName, subjectIdentifier);
        addMappingFile(sample, dnaAnalysisFilename);
    }

    private void addMappingFile(Sample sample, String dnaAnalysisFilename) {
        List<String> filenames = sampleToFilenamesMap.get(sample);
        if (filenames == null) {
            filenames = new ArrayList<String>();
            sampleToFilenamesMap.put(sample, filenames);
        }
        filenames.add(dnaAnalysisFilename);
    }

    List<ArrayDataValues> loadArrayDataValues() 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<ArrayDataValues> values = new ArrayList<ArrayDataValues>();
        for (Sample sample : sampleToFilenamesMap.keySet()) {
            values.add(loadArrayDataValues(sample));
        }
        return values;
    }

    private ArrayDataValues loadArrayDataValues(Sample sample) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<File> dataFiles = new ArrayList<File>();
        try {
            for (String filename : sampleToFilenamesMap.get(sample)) {
                validateDataFileExtension(filename);
                dataFiles.add(getDataFile(filename));
            }
            return loadArrayDataValues(sample, dataFiles);
        } finally {
            for (File file : dataFiles) {
                doneWithFile(file);
            }
        }
    }

    private void validateDataFileExtension(String filename) throws ValidationException {
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        if (!getFileType().equalsIgnoreCase(extension)) {
            throw new ValidationException("Data file must be '." + getFileType() + "' type instead of: " + filename);
        }
    }

    private ArrayDataValues loadArrayDataValues(Sample sample, List<File> chpFiles) 
    throws DataRetrievalException, ValidationException {
        List<AffymetrixDnaAnalysisChpParser> parsers = new ArrayList<AffymetrixDnaAnalysisChpParser>();
        Set<String> reporterListNames = new HashSet<String>();
        for (File chpFile : chpFiles) {
            AffymetrixDnaAnalysisChpParser parser = new AffymetrixDnaAnalysisChpParser(chpFile,
                    getCentralTendencyCalculator());
            parsers.add(parser);
            reporterListNames.add(parser.getArrayDesignName());
        }
        Platform platform = getPlatform(reporterListNames);
        return loadArrayDataValues(sample, platform, parsers);
    }

    private ArrayDataValues loadArrayDataValues(Sample sample, Platform platform, 
            List<AffymetrixDnaAnalysisChpParser> parsers) throws DataRetrievalException {
        PlatformHelper helper = new PlatformHelper(platform);
        Set<ReporterList> reporterLists = helper.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        ArrayData arrayData = createArrayData(sample, reporterLists, ArrayDataType.COPY_NUMBER);
        getDao().save(arrayData);
        ArrayDataValues values = 
            new ArrayDataValues(helper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
        for (AffymetrixDnaAnalysisChpParser parser : parsers) {
            parser.parse(values, arrayData, getDataType());
        }
        getArrayDataService().save(values);
        return values;
    }

    /**
     * @return the datatype
     */
    abstract MultiDataType getDataType();

}
