/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;

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
