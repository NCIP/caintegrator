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
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
class AffymetrixCopyNumberMappingFileHandler extends AbstractCopyNumberMappingFileHandler {

    static final String FILE_TYPE = "cnchp";
    
    AffymetrixCopyNumberMappingFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    void doneWithFile(File cnchpFile) {
        cnchpFile.delete();
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
                validateDataFileExtension(filename);
                dataFiles.add(getDataFile(filename));
            }
            return loadArrayData(sample, dataFiles);
        } finally {
            for (File file : dataFiles) {
                doneWithFile(file);
            }
        }
    }

    private void validateDataFileExtension(String filename) throws ValidationException {
        if (!filename.endsWith(".cnchp")) {
            throw new ValidationException("Data file must be '.cnchp' type instead of: " + filename);
        }
    }

    private ArrayDataValues loadArrayData(Sample sample, List<File> cnchpFiles) 
    throws DataRetrievalException, ValidationException {
        List<AffymetrixCopyNumberChpParser> parsers = new ArrayList<AffymetrixCopyNumberChpParser>();
        Set<String> reporterListNames = new HashSet<String>();
        for (File cnchpFile : cnchpFiles) {
            AffymetrixCopyNumberChpParser parser = new AffymetrixCopyNumberChpParser(cnchpFile);
            parsers.add(parser);
            reporterListNames.add(parser.getArrayDesignName());
        }
        Platform platform = getPlatform(reporterListNames);
        return loadArrayData(sample, platform, parsers);
    }

    private ArrayDataValues loadArrayData(Sample sample, Platform platform, 
            List<AffymetrixCopyNumberChpParser> parsers) throws DataRetrievalException {
        PlatformHelper helper = new PlatformHelper(platform);
        Set<ReporterList> reporterLists = helper.getReporterLists(ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        ArrayData arrayData = createArrayData(sample, reporterLists);
        getDao().save(arrayData);
        ArrayDataValues values = 
            new ArrayDataValues(helper.getAllReportersByType(ReporterTypeEnum.DNA_ANALYSIS_REPORTER));
        for (AffymetrixCopyNumberChpParser parser : parsers) {
            parser.parse(values, arrayData);
        }
        getArrayDataService().save(values);
        return values;
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

}
