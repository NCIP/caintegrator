/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataServiceStub;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceDataTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.ValidationException;
import gov.nih.nci.caintegrator2.application.study.deployment.DnaAnalysisHandlerFactory;
import gov.nih.nci.caintegrator2.application.study.deployment.GenomicDataHelper;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.bioconductor.BioconductorService;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacadeStub;

import org.junit.Before;
import org.junit.Test;

public class GenomicDataHelperTest {
    
    private GenomicDataHelper helper;
    private CaArrayFacadeStub caArrayFacade = new CaArrayFacadeStub();
    private ArrayDataServiceStub arrayDataService = new ArrayDataServiceStub();
    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    private BioconductorService biocondutor;
    private DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory = new LocalDnaAnalysisHandlerFactoryImpl();
    private ExpressionHandlerFactory expressionHandlerFactory = new LocalExpressionHandlerFactoryImpl();
    
    @Before
    public void setUp() {
        caArrayFacade.reset();
        arrayDataService.reset();
        helper = new GenomicDataHelper(caArrayFacade, arrayDataService, dao, biocondutor, dnaAnalysisHandlerFactory );
        helper.setExpressionHandlerFactory(expressionHandlerFactory);
    }

    @Test
    public void testLoadData() throws ConnectionException, DataRetrievalException, ValidationException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataConfiguration = new GenomicDataSourceConfiguration();
        Sample sample = new Sample();
        genomicDataConfiguration.getSamples().add(sample);
        genomicDataConfiguration.setDataType(GenomicDataSourceDataTypeEnum.EXPRESSION);
        genomicDataConfiguration.setPlatformVendor("Affymetrix");
        genomicDataConfiguration.setUseSupplementalFiles(false);
        studyConfiguration.getGenomicDataSources().add(genomicDataConfiguration);
        helper.loadData(studyConfiguration);
        assertTrue(arrayDataService.saveCalled);
        assertTrue(caArrayFacade.retrieveDataCalled);
        assertEquals(1, sample.getArrayCollection().size());
        assertEquals(2, sample.getArrayDataCollection().size());
    }

    @Test
    public void testAgilentExpressionLoadData() throws ConnectionException, DataRetrievalException, ValidationException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataConfiguration = new GenomicDataSourceConfiguration();
        Sample sample = new Sample();
        sample.setName("testSample");
        genomicDataConfiguration.getSamples().add(sample);
        genomicDataConfiguration.setDataType(GenomicDataSourceDataTypeEnum.EXPRESSION);
        genomicDataConfiguration.setPlatformVendor("Agilent");
        genomicDataConfiguration.setSampleMappingFilePath(TestDataFiles.TEST_AGILENT_SAMPLE_MAPPING_FILE.getAbsolutePath());
        studyConfiguration.getGenomicDataSources().add(genomicDataConfiguration);
        genomicDataConfiguration.setStudyConfiguration(studyConfiguration);
        helper.loadData(studyConfiguration);
        assertTrue(arrayDataService.saveCalled);
        assertEquals(2, sample.getArrayCollection().size());
        assertEquals(3, sample.getArrayDataCollection().size());
    }

}
