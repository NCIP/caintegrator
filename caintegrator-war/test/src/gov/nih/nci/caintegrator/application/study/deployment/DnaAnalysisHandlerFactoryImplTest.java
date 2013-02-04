/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.AbstractUnparsedSupplementalMappingFileHandler;
import gov.nih.nci.caintegrator.application.study.deployment.AffymetrixCopyNumberMappingFileHandler;
import gov.nih.nci.caintegrator.application.study.deployment.AffymetrixSnpMappingFileHandler;
import gov.nih.nci.caintegrator.application.study.deployment.DnaAnalysisHandlerFactory;
import gov.nih.nci.caintegrator.application.study.deployment.DnaAnalysisHandlerFactoryImpl;
import gov.nih.nci.caintegrator.application.study.deployment.GenericSupplementalMultiSamplePerFileHandler;
import gov.nih.nci.caintegrator.application.study.deployment.GenericSupplementalSingleSamplePerFileHandler;
import gov.nih.nci.caintegrator.external.DataRetrievalException;

import org.junit.Test;


/**
 * 
 */
public class DnaAnalysisHandlerFactoryImplTest {

    @Test
    public void testAll() throws DataRetrievalException {
        DnaAnalysisHandlerFactory factory = new DnaAnalysisHandlerFactoryImpl();
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setLoadingType(ArrayDataLoadingTypeEnum.CHP);
        AbstractUnparsedSupplementalMappingFileHandler handler;
        handler = factory.getHandler(genomicSource, null, null, null);
        assertTrue(handler instanceof AffymetrixSnpMappingFileHandler);
        genomicSource.setLoadingType(ArrayDataLoadingTypeEnum.CNCHP);
        handler = factory.getHandler(genomicSource, null, null, null);
        assertTrue(handler instanceof AffymetrixCopyNumberMappingFileHandler);
        genomicSource.setLoadingType(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE);
        handler = factory.getHandler(genomicSource, null, null, null);
        assertTrue(handler instanceof GenericSupplementalMultiSamplePerFileHandler);
        genomicSource.setLoadingType(ArrayDataLoadingTypeEnum.SINGLE_SAMPLE_PER_FILE);
        handler = factory.getHandler(genomicSource, null, null, null);
        assertTrue(handler instanceof GenericSupplementalSingleSamplePerFileHandler);
        
    }
}
