/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
@Transactional (propagation = Propagation.REQUIRED)
class GenericSupplementalSingleSamplePerFileHandler extends AbstractGenericSupplementalMappingFileHandler {
    
    private final Map<Sample, List<SupplementalDataFile>> sampleToDataFileMap =
        new HashMap<Sample, List<SupplementalDataFile>>();
    
    GenericSupplementalSingleSamplePerFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    void mappingSample(String subjectId, String sampleName, SupplementalDataFile supplementalDataFile)
            throws FileNotFoundException, ValidationException {
        Sample sample = getSample(sampleName, subjectId);
        addSupplementalFile(sample, supplementalDataFile);
    }

    private void addSupplementalFile(Sample sample, SupplementalDataFile supplementalDataFile) {
        List<SupplementalDataFile> supplementalDataFiles = sampleToDataFileMap.get(sample);
        if (supplementalDataFiles == null) {
            supplementalDataFiles = new ArrayList<SupplementalDataFile>();
            sampleToDataFileMap.put(sample, supplementalDataFiles);
        }
        supplementalDataFiles.add(supplementalDataFile);
    }

    List<ArrayDataValues> loadArrayDataValue()
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
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(getReporterType());
        ArrayData arrayData = createArrayData(sample, reporterLists, getDataType());
        getDao().save(arrayData);
        ArrayDataValues values = 
            new ArrayDataValues(platformHelper.getAllReportersByType(getReporterType()));
        for (SupplementalDataFile supplementalDataFile : supplementalDataFiles) {
            GenericSupplementalSingleSampleDataRetrieval parser =
                new GenericSupplementalSingleSampleDataRetrieval(supplementalDataFile,
                        getReporterType(), getDataValueType());
            parser.parseDataFile(values, arrayData, platformHelper, getCentralTendencyCalculator());
        }
        getArrayDataService().save(values);
        return values;
    }

 }
