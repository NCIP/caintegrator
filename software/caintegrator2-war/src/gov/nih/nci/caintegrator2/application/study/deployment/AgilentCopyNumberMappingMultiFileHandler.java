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
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
class AgilentCopyNumberMappingMultiFileHandler extends AbstractCopyNumberMappingFileHandler {
    
    static final String FILE_TYPE = "data";
    
    AgilentCopyNumberMappingMultiFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
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
        List<ArrayDataValues> values = new ArrayList<ArrayDataValues>();
        for (Sample sample : getSampleToFilenamesMap().keySet()) {
            values.add(loadArrayData(sample));
        }
        return values;
    }

    private ArrayDataValues loadArrayData(Sample sample) 
    throws ConnectionException, DataRetrievalException, ValidationException {
        List<File> dataFiles = new ArrayList<File>();
        try {
            for (String filename : getSampleToFilenamesMap().get(sample)) {
                dataFiles.add(getDataFile(filename));
            }
            return loadArrayData(sample, dataFiles);
        } finally {
            for (File file : dataFiles) {
                doneWithFile(file);
            }
        }
    }

    private ArrayDataValues loadArrayData(Sample sample, List<File> dataFiles) 
    throws DataRetrievalException, ValidationException {
        PlatformHelper platformHelper = new PlatformHelper(getDao().getPlatform(getGenomicSource().getPlatformName()));
        Set<ReporterList> reporterLists = platformHelper.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        ArrayData arrayData = createArrayData(sample, reporterLists);
        getDao().save(arrayData);
        ArrayDataValues values = 
            new ArrayDataValues(platformHelper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
        for (File dataFile : dataFiles) {
            AgilentCopyNumberDataRetrieval.INSTANCE.parseDataFile(dataFile, values, arrayData, platformHelper);
        }
        getArrayDataService().save(values);
        return values;
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

 }
