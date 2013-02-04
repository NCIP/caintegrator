/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.deployment.AbstractDnaAnalysisMappingFileHandler;
import gov.nih.nci.caintegrator2.application.study.deployment.DnaAnalysisHandlerFactory;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;

/**
 * Returns a local copy number handler for expedited testing.
 */
public class LocalDnaAnalysisHandlerFactoryImpl implements DnaAnalysisHandlerFactory {

    /**
     * {@inheritDoc}
     */
    public AbstractDnaAnalysisMappingFileHandler getHandler(GenomicDataSourceConfiguration genomicSource,
            CaArrayFacade caArrayFacade, ArrayDataService arrayDataService, CaIntegrator2Dao dao) {
        return new LocalCopyNumberMappingFileHandler(genomicSource, caArrayFacade, arrayDataService, dao);
    }

}
