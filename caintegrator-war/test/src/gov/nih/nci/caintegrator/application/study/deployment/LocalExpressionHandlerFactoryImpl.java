/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.AbstractExpressionMappingFileHandler;
import gov.nih.nci.caintegrator.application.study.deployment.ExpressionHandlerFactory;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

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
