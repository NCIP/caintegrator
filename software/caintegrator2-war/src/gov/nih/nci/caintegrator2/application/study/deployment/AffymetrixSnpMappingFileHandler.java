/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import affymetrix.calvin.data.CHPMultiDataData.MultiDataType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

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

}
