/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

/**
 * Default factory implementation.
 */
public class DnaAnalysisHandlerFactoryImpl implements DnaAnalysisHandlerFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractUnparsedSupplementalMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao)
            throws DataRetrievalException {
        switch (genomicSource.getLoadingType()) {
        case CHP:
            return new AffymetrixSnpMappingFileHandler(genomicSource, caArrayFacade, arrayDataService, dao);
        case CNCHP:
            return new AffymetrixCopyNumberMappingFileHandler(genomicSource, caArrayFacade,
                    arrayDataService, dao);
        case SINGLE_SAMPLE_PER_FILE:
            return new GenericSupplementalSingleSamplePerFileHandler(genomicSource, caArrayFacade,
                    arrayDataService, dao);
        case MULTI_SAMPLE_PER_FILE:
            return new GenericSupplementalMultiSamplePerFileHandler(genomicSource, caArrayFacade,
                    arrayDataService, dao);
        default:
            throw new DataRetrievalException("Unknown platform vendor.");
        }

    }

}
