/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.AbstractUnparsedSupplementalMappingFileHandler;
import gov.nih.nci.caintegrator.application.study.deployment.DnaAnalysisHandlerFactory;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;

/**
 * Returns a local copy number handler for expedited testing.
 */
public class LocalDnaAnalysisHandlerFactoryImpl implements DnaAnalysisHandlerFactory {

    /**
     * {@inheritDoc}
     */
    public AbstractUnparsedSupplementalMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        return new LocalCopyNumberMappingFileHandler(genomicSource, caArrayFacade, arrayDataService, dao);
    }

}
