/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.array.ArrayDesign;
import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caarray.external.v1_0.data.FileMetadata;
import gov.nih.nci.caarray.external.v1_0.data.FileStreamableContents;
import gov.nih.nci.caarray.external.v1_0.data.FloatColumn;
import gov.nih.nci.caarray.external.v1_0.data.HybridizationData;
import gov.nih.nci.caarray.external.v1_0.experiment.Experiment;
import gov.nih.nci.caarray.external.v1_0.query.BiomaterialSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.ExperimentSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.FileSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.HybridizationSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.LimitOffset;
import gov.nih.nci.caarray.external.v1_0.query.SearchResult;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.InvalidReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.UnsupportedCategoryException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.DataTransferException;
import gov.nih.nci.caarray.services.external.v1_0.data.InconsistentDataSetsException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.healthmarketscience.rmiio.DirectRemoteInputStream;

@SuppressWarnings("PMD")
public class CaArrayFacadeTest {

    private CaArrayFacade caArrayFacade;
    private GeneExpressionReporter reporter1 = createTestReporter("A_probeSet1");
    private GeneExpressionReporter reporter2 = createTestReporter("A_probeSet2");
    
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext("caarray-test-config.xml", CaArrayFacadeTest.class); 
        caArrayFacade = (CaArrayFacade) context.getBean("CaArrayFacade"); 
    }

    @Test
    public void testGetSamples() throws ConnectionException, ExperimentNotFoundException {
        ((CaArrayFacadeImpl) caArrayFacade).setServiceFactory(new RetrieveDataServiceFactoryStub());
        List<Sample> samples = caArrayFacade.getSamples("samples", null);
        assertFalse(samples.isEmpty());
        assertTrue(samples.get(0).getName().equals("sample"));
        boolean experimentNotFound = false;
        try {
            samples = caArrayFacade.getSamples("no-experiment", null);   
        } catch (ExperimentNotFoundException e) {
            experimentNotFound = true;
        }
        assertTrue(experimentNotFound);
    }
    
    @Test
    public void testAffymetrixRetrieveData() throws ConnectionException, DataRetrievalException {
        RetrieveDataDaoStub daoStub = new RetrieveDataDaoStub();
        ((CaArrayFacadeImpl) caArrayFacade).setServiceFactory(new RetrieveDataServiceFactoryStub());
        ((CaArrayFacadeImpl) caArrayFacade).setDao(daoStub);
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setExperimentIdentifier("test-data");
        genomicSource.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX.getValue());
        genomicSource.setStudyConfiguration(new StudyConfiguration());
        Sample sample = new Sample();
        sample.setName("sample");
        genomicSource.getSamples().add(sample);
        ArrayDataValues values = caArrayFacade.retrieveData(genomicSource);
        assertNotNull(values);
        assertEquals(1, values.getArrayDatas().size());
        for (ArrayData arrayData : values.getArrayDatas()) {
            assertEquals((float) 1.1, (float) values.getFloatValue(arrayData, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
            assertEquals((float) 2.2, (float) values.getFloatValue(arrayData, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
        }
    }
    
    @Test
    public void testAgilentRetrieveData() throws ConnectionException, DataRetrievalException {
        RetrieveDataDaoStub daoStub = new RetrieveDataDaoStub();
        ((CaArrayFacadeImpl) caArrayFacade).setServiceFactory(new RetrieveDataServiceFactoryStub());
        ((CaArrayFacadeImpl) caArrayFacade).setDao(daoStub);
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setExperimentIdentifier("test-data");
        genomicSource.setPlatformVendor(PlatformVendorEnum.AGILENT.getValue());
        genomicSource.setStudyConfiguration(new StudyConfiguration());
        Sample sample = new Sample();
        sample.setName("sample");
        genomicSource.getSamples().add(sample);
        ArrayDataValues values = caArrayFacade.retrieveData(genomicSource);
        assertNotNull(values);
        assertEquals(1, values.getArrayDatas().size());
        for (ArrayData arrayData : values.getArrayDatas()) {
            assertEquals((float) 0.05, (float) values.getFloatValue(arrayData, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
            assertEquals((float) -0.1234, (float) values.getFloatValue(arrayData, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
        }
    }
    
    @Test
    public void testRetrieveFile() throws FileNotFoundException, ConnectionException {
        ((CaArrayFacadeImpl) caArrayFacade).setServiceFactory(new RetrieveDataServiceFactoryStub());
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        byte[] bytes = caArrayFacade.retrieveFile(source, "filename");
        assertEquals(124, bytes.length);
    }

    private GeneExpressionReporter createTestReporter(String name) {
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setName(name);
        return reporter;
    }

    private class RetrieveDataDaoStub extends CaIntegrator2DaoStub {
        
        private Platform platform = createTestPlatform();

        @Override
        public Platform getPlatform(String name) {
            return platform;
        }

        private Platform createTestPlatform() {
            Platform platform = new Platform();
            ReporterList reporters = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            reporters.setId(1L);
            reporters.getReporters().add(reporter1);
            reporters.getReporters().add(reporter2);
            reporter1.setReporterList(reporters);
            reporter2.setReporterList(reporters);
            return  platform;
        }
        
    }

    private class RetrieveDataServiceFactoryStub extends ServiceStubFactory {

        private final ArrayDesign arrayDesign = createTestArrayDesign();
        DesignElement probeSet1 = createTestProbeSet("A_probeSet1");
        DesignElement probeSet2 = createTestProbeSet("A_probeSet2");
        private final Hybridization hybridization = createTestHybridization();

        @Override
        public SearchService createSearchService(ServerConnectionProfile profile) throws ConnectionException {
            return new LocalSearchServiceStub();
        }

        private DesignElement createTestProbeSet(String name) {
            DesignElement probeSet = new DesignElement();
            probeSet.setName(name);
            return probeSet;
        }

        private Hybridization createTestHybridization() {
            Hybridization hybridization = new Hybridization();
            hybridization.setArrayDesign(arrayDesign);
            return hybridization;
        }

        @Override
        public DataService createDataService(ServerConnectionProfile profile) {
            return new LocalDataServiceStub();
        }

        private ArrayDesign createTestArrayDesign() {
            ArrayDesign design = new ArrayDesign();
            design.setName("test design");
            return design;
        }

        private class LocalDataServiceStub extends DataServiceStub {

            @Override
            @SuppressWarnings("unused")
            public DataSet getDataSet(DataSetRequest request) throws InvalidReferenceException,
                    InconsistentDataSetsException, IllegalArgumentException {
                DataSet dataSet = new DataSet();
                dataSet.getDesignElements().add(probeSet1);
                dataSet.getDesignElements().add(probeSet2);
                for (CaArrayEntityReference reference : request.getHybridizations()) {
                    HybridizationData data = new HybridizationData();
                    data.setHybridization(hybridization);
                    dataSet.getDatas().add(data);
                    FloatColumn column = new FloatColumn();
                    column.setValues(new float[] {1.1f, 2.2f});
                    data.getDataColumns().add(column);
                }
                return dataSet;
            }
            
            /**
             * {@inheritDoc}
             */
            @Override
            public FileStreamableContents streamFileContents(CaArrayEntityReference arg0, boolean arg1)
                    throws InvalidReferenceException, DataTransferException {
                StringBuffer dataFile = new StringBuffer();
                dataFile.append("ID\tChromosome\tPhysical.Position\tlogratio\n");
                dataFile.append("A_probeSet1\t1\t123456\t0.05\n");
                dataFile.append("DarkCorner\t1\t56789\t-0.0034\n");
                dataFile.append("*\n");
                dataFile.append("A_probeSet2\t1\t98765\t-0.1234\n");
                byte[] gzippedBytes = getGzippedBytes(dataFile.toString());
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gzippedBytes);
                FileStreamableContents contents =  new FileStreamableContents();
                contents.setContentStream(new DirectRemoteInputStream(byteArrayInputStream, false));
                return contents;
            }

            private byte[] getGzippedBytes(String value) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                    gzipOutputStream.write(value.getBytes());
                    gzipOutputStream.close();
                    return byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    throw new IllegalStateException();
                }
            }
        }

        private class LocalSearchServiceStub extends SearchServiceStub {

            private final Experiment experiment = createTestDataExperiment();
            private final Biomaterial sample = creatTestSample("sample"); 
            
            @Override
            public SearchResult<Experiment> searchForExperiments(ExperimentSearchCriteria criteria, LimitOffset offset)
                    throws InvalidReferenceException, UnsupportedCategoryException {
                SearchResult<Experiment> result = new SearchResult<Experiment>();
                if (!"no-experiment".equals(criteria.getPublicIdentifier())) {
                    result.getResults().add(experiment);
                }
                return result;
            }

            private Biomaterial creatTestSample(String name) {
                Biomaterial sample = new Biomaterial();
                sample.setName(name);
                return sample;
            }

            @Override
            public SearchResult<Biomaterial> searchForBiomaterials(BiomaterialSearchCriteria arg0, LimitOffset arg1)
                    throws InvalidReferenceException, UnsupportedCategoryException {
                SearchResult<Biomaterial> result = new SearchResult<Biomaterial>();
                result.getResults().add(sample);
                result.setMaxAllowedResults(-1);
                return result;
            }

            @Override
            public SearchResult<File> searchForFiles(FileSearchCriteria criteria, LimitOffset arg1)
                    throws InvalidReferenceException {
                SearchResult<File> result = new SearchResult<File>();
                File file = new File();
                file.setMetadata(new FileMetadata());
                file.getMetadata().setName("filename");
                result.getResults().add(file);
                return result;
            }

            @Override
            public SearchResult<Hybridization> searchForHybridizations(HybridizationSearchCriteria criteria,
                    LimitOffset arg1) throws InvalidReferenceException {
                SearchResult<Hybridization> result = new SearchResult<Hybridization>();
                result.getResults().add(hybridization);
                return result;
            }
            
            private Experiment createTestDataExperiment() {
                Experiment experiment = new Experiment();
                experiment.setPublicIdentifier("samples");
                return experiment;
            }
            
        }
    }
   
}
