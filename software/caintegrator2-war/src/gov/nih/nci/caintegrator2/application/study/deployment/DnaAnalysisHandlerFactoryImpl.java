/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

/**
 * Default factory implementation.
 */
public class DnaAnalysisHandlerFactoryImpl implements DnaAnalysisHandlerFactory {

    /**
     * {@inheritDoc}
     */
    public AbstractDnaAnalysisMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao)
    throws DataRetrievalException {
        switch (PlatformVendorEnum.getByValue(genomicSource.getPlatformVendor())) {
        case AFFYMETRIX:
            if (genomicSource.isCopyNumberData()) {
                return new AffymetrixCopyNumberMappingFileHandler(genomicSource, caArrayFacade, arrayDataService, dao);
            }
            return new AffymetrixSnpMappingFileHandler(genomicSource, caArrayFacade, arrayDataService, dao);
        case AGILENT:
            return singleOrMultiFileHandler(genomicSource, caArrayFacade,
                        arrayDataService, dao);
        default:
            throw new DataRetrievalException("Unknown platform vendor.");
        }

    }
    
    private AbstractDnaAnalysisMappingFileHandler singleOrMultiFileHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        if (genomicSource.isSingleDataFile()) {
            return new AgilentCopyNumberMappingSingleFileHandler(genomicSource, caArrayFacade,
                    arrayDataService, dao);
        } else {
            return new AgilentCopyNumberMappingMultiFileHandler(genomicSource, caArrayFacade,
                    arrayDataService, dao);
        }
    }

}
