/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import java.io.FileNotFoundException;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.SupplementalDataFile;
import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
class AffymetrixCopyNumberMappingFileHandler extends AbstractAffymetrixDnaAnalysisMappingFileHandler {

    static final String FILE_TYPE = "cnchp";
    
    /**
     * @param genomicSource
     * @param caArrayFacade
     * @param arrayDataService
     * @param dao
     */
    AffymetrixCopyNumberMappingFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

    @Override
    MultiDataType getDataType() {
        return MultiDataType.CopyNumberMultiDataType;
    }

    @Override
    void mappingSample(String subjectId, String sampleName, SupplementalDataFile supplementalDataFile)
            throws ValidationException, FileNotFoundException {
        // Using CHP file not generic supplemental file
    }

}
