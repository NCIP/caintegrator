/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

/**
 * Returns a local copy number handler for expedited testing.
 */
public class LocalExpressionHandlerFactoryImpl implements ExpressionHandlerFactory {

    /**
     * {@inheritDoc}
     */
    public AbstractExpressionMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao)
    throws DataRetrievalException {
        switch (genomicSource.getLoadingType()) {
        case SINGLE_SAMPLE_PER_FILE:
            return new LocalExpressionSingleSampleHandler(genomicSource, caArrayFacade, arrayDataService, dao);
        case MULTI_SAMPLE_PER_FILE:
            return new LocalExpressionMultiSampleHandler(genomicSource, caArrayFacade, arrayDataService, dao);
        default:
            throw new DataRetrievalException("Unknown platform vendor.");
        }
    }

}
