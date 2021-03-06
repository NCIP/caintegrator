/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import java.io.FileNotFoundException;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.caarray.SupplementalDataFile;
import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;

/**
 * Reads and retrieves copy number data from a caArray instance.
 */
class AffymetrixSnpMappingFileHandler extends AbstractAffymetrixDnaAnalysisMappingFileHandler {

    static final String FILE_TYPE = "chp";
    
    /**
     * @param genomicSource
     * @param caArrayFacade
     * @param arrayDataService
     * @param dao
     */
    AffymetrixSnpMappingFileHandler(GenomicDataSourceConfiguration genomicSource, CaArrayFacade caArrayFacade,
            ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        super(genomicSource, caArrayFacade, arrayDataService, dao);
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

    @Override
    MultiDataType getDataType() {
        return MultiDataType.GenotypeMultiDataType;
    }

    @Override
    void mappingSample(String subjectId, String sampleName, SupplementalDataFile supplementalDataFile)
            throws ValidationException, FileNotFoundException {
        // Using CHP file not generic supplemental file
    }

}
