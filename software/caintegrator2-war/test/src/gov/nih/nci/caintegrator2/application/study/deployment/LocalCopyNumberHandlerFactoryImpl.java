/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.deployment.AbstractCopyNumberMappingFileHandler;
import gov.nih.nci.caintegrator2.application.study.deployment.CopyNumberHandlerFactory;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

/**
 * Returns a local copy number handler for expedited testing.
 */
public class LocalCopyNumberHandlerFactoryImpl implements CopyNumberHandlerFactory {

    /**
     * {@inheritDoc}
     */
    public AbstractCopyNumberMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        return new LocalCopyNumberMappingFileHandler(genomicSource, caArrayFacade, arrayDataService, dao);
    }

}
