/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study.deployment;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.ValidationException;
import gov.nih.nci.caintegrator.application.study.deployment.DnaAnalysisHandlerFactory;
import gov.nih.nci.caintegrator.application.study.deployment.ExpressionHandlerFactory;
import gov.nih.nci.caintegrator.application.study.deployment.GenomicDataHelper;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.bioconductor.BioconductorService;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class GenomicDataHelperTest extends AbstractMockitoTest {
    private GenomicDataHelper helper;
    private CaIntegrator2Dao dao = new CaIntegrator2DaoStub();
    private BioconductorService biocondutor;
    private DnaAnalysisHandlerFactory dnaAnalysisHandlerFactory = new LocalDnaAnalysisHandlerFactoryImpl();
    private ExpressionHandlerFactory expressionHandlerFactory = new LocalExpressionHandlerFactoryImpl();

    @Before
    public void setUp() {
        helper = new GenomicDataHelper(caArrayFacade, arrayDataService, dao, biocondutor, dnaAnalysisHandlerFactory );
        helper.setExpressionHandlerFactory(expressionHandlerFactory);
    }

    @Test
    public void testLoadData()
    throws ConnectionException, DataRetrievalException, ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataConfiguration = new GenomicDataSourceConfiguration();
        Sample sample = new Sample();
        sample.setName("testSample");
        genomicDataConfiguration.getSamples().add(sample);
        genomicDataConfiguration.setDataType(PlatformDataTypeEnum.EXPRESSION);
        genomicDataConfiguration.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        genomicDataConfiguration.setLoadingType(ArrayDataLoadingTypeEnum.PARSED_DATA);
        studyConfiguration.getGenomicDataSources().add(genomicDataConfiguration);
        helper.loadData(studyConfiguration);
        verify(arrayDataService, atLeastOnce()).save(any(ArrayDataValues.class));
        verify(caArrayFacade, atLeastOnce()).retrieveData(any(GenomicDataSourceConfiguration.class));
        assertEquals(1, sample.getArrayCollection().size());
        assertEquals(2, sample.getArrayDataCollection().size());
    }

    @Test
    public void testAgilentExpressionLoadData()
    throws ConnectionException, DataRetrievalException, ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataConfiguration = new GenomicDataSourceConfiguration();
        Sample sample = new Sample();
        sample.setName("testSample");
        genomicDataConfiguration.getSamples().add(sample);
        genomicDataConfiguration.setDataType(PlatformDataTypeEnum.EXPRESSION);
        genomicDataConfiguration.setPlatformVendor(PlatformVendorEnum.AGILENT);
        genomicDataConfiguration.setLoadingType(ArrayDataLoadingTypeEnum.SINGLE_SAMPLE_PER_FILE);
        genomicDataConfiguration.setSampleMappingFilePath(TestDataFiles.TEST_AGILENT_SINGLE_SAMPLE_MAPPING_FILE.getAbsolutePath());
        studyConfiguration.getGenomicDataSources().add(genomicDataConfiguration);
        genomicDataConfiguration.setStudyConfiguration(studyConfiguration);
        helper.loadData(studyConfiguration);
        verify(arrayDataService, atLeastOnce()).save(any(ArrayDataValues.class));
        assertEquals(2, sample.getArrayCollection().size());
        assertEquals(3, sample.getArrayDataCollection().size());
    }

    @Test
    public void testAgilentExpressionMultiSampleLoadData()
    throws ConnectionException, DataRetrievalException, ValidationException, IOException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        GenomicDataSourceConfiguration genomicDataConfiguration = new GenomicDataSourceConfiguration();
        Sample sample = new Sample();
        sample.setName("testSample");
        genomicDataConfiguration.getSamples().add(sample);
        genomicDataConfiguration.setDataType(PlatformDataTypeEnum.EXPRESSION);
        genomicDataConfiguration.setPlatformVendor(PlatformVendorEnum.AGILENT);
        genomicDataConfiguration.setLoadingType(ArrayDataLoadingTypeEnum.MULTI_SAMPLE_PER_FILE);
        genomicDataConfiguration.setSampleMappingFilePath(TestDataFiles.TEST_AGILENT_MULTI_SAMPLE_MAPPING_FILE.getAbsolutePath());
        studyConfiguration.getGenomicDataSources().add(genomicDataConfiguration);
        genomicDataConfiguration.setStudyConfiguration(studyConfiguration);
        helper.loadData(studyConfiguration);
        verify(arrayDataService, atLeastOnce()).save(any(ArrayDataValues.class));
        assertEquals(2, sample.getArrayCollection().size());
        assertEquals(3, sample.getArrayDataCollection().size());
    }

}
