/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
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
        // TODO Auto-generated constructor stub
    }

    @Override
    String getFileType() {
        return FILE_TYPE;
    }

    @Override
    MultiDataType getDataType() {
        return MultiDataType.CopyNumberMultiDataType;
    }

}
