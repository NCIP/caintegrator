/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caarray.external.v1_0.AbstractCaArrayEntity;
import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.array.ArrayDesign;
import gov.nih.nci.caarray.external.v1_0.data.DataSet;
import gov.nih.nci.caarray.external.v1_0.data.DesignElement;
import gov.nih.nci.caarray.external.v1_0.data.File;
import gov.nih.nci.caarray.external.v1_0.data.FileMetadata;
import gov.nih.nci.caarray.external.v1_0.data.FileStreamableContents;
import gov.nih.nci.caarray.external.v1_0.data.FloatColumn;
import gov.nih.nci.caarray.external.v1_0.data.HybridizationData;
import gov.nih.nci.caarray.external.v1_0.data.QuantitationType;
import gov.nih.nci.caarray.external.v1_0.experiment.Experiment;
import gov.nih.nci.caarray.external.v1_0.query.BiomaterialSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.DataSetRequest;
import gov.nih.nci.caarray.external.v1_0.query.ExampleSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.ExperimentSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.FileSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.HybridizationSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.LimitOffset;
import gov.nih.nci.caarray.external.v1_0.query.QuantitationTypeSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.SearchResult;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.Hybridization;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.DnaAnalysisReporter;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.healthmarketscience.rmiio.DirectRemoteInputStream;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class CaArrayFacadeTest extends AbstractMockitoTest {
    private GeneExpressionReporter reporter1 = createTestReporter("A_probeSet1");
    private GeneExpressionReporter reporter2 = createTestReporter("A_probeSet2");
    private DnaAnalysisReporter reporter3 = createDnaAnalysisTestReporter("A_probeSet1");
    private DnaAnalysisReporter reporter4 = createDnaAnalysisTestReporter("A_probeSet2");
    private DesignElement probeSet1 = createTestProbeSet("A_probeSet1");
    private DesignElement probeSet2 = createTestProbeSet("A_probeSet2");

    private CaArrayFacadeImpl caArrayFacade;
    private DataService dataService;
    private SearchService searchService;

    /**
     * Sets up objects necessary for unit testing.
     * @throws Exception on error
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        caArrayFacade = new CaArrayFacadeImpl();

        dataService = mock(DataService.class);
        when(dataService.getDataSet(any(DataSetRequest.class))).thenAnswer(new Answer<DataSet>() {
            @Override
            public DataSet answer(InvocationOnMock invocation) throws Throwable {
                DataSetRequest r = (DataSetRequest) invocation.getArguments()[0];
                DataSet result = new DataSet();
                result.getDesignElements().add(probeSet1);
                result.getDesignElements().add(probeSet2);
                for (CaArrayEntityReference ref : r.getHybridizations()) {
                    ArrayDesign design = new ArrayDesign();
                    design.setName("test design");
                    Hybridization hybridization = new Hybridization();
                    hybridization.setArrayDesign(design);

                    HybridizationData data = new HybridizationData();
                    data.setHybridization(hybridization);
                    result.getDatas().add(data);
                    FloatColumn column = new FloatColumn();
                    column.setValues(new float[] {1.1f, 2.2f});
                    data.getDataColumns().add(column);
                }
                return result;
            }
        });
        when(dataService.streamFileContents(any(CaArrayEntityReference.class), anyBoolean())).thenAnswer(new Answer<FileStreamableContents>() {

            @Override
            public FileStreamableContents answer(InvocationOnMock invocation) throws Throwable {
                StringBuffer dataFile = new StringBuffer();
                dataFile.append("ID\tChromosome\tPhysical.Position\tlogratio\n");
                dataFile.append("A_probeSet1\t1\t123456\t0.05\n");
                dataFile.append("DarkCorner\t1\t56789\t-0.0034\n");
                dataFile.append("*\n");
                dataFile.append("A_probeSet2\t1\t98765\t-0.1234\n");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzos = new GZIPOutputStream(baos);
                IOUtils.write(dataFile.toString().getBytes(), gzos);

                byte[] gzippedBytes = baos.toByteArray();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(gzippedBytes);
                FileStreamableContents contents =  new FileStreamableContents();
                contents.setContentStream(new DirectRemoteInputStream(byteArrayInputStream, false));
                return contents;
            }
        });

        searchService = mock(SearchService.class);
        when(searchService.searchForExperiments(any(ExperimentSearchCriteria.class), any(LimitOffset.class))).thenAnswer(new Answer<SearchResult<Experiment>>() {

            @Override
            public SearchResult<Experiment> answer(InvocationOnMock invocation) throws Throwable {
                SearchResult<Experiment> result = new SearchResult<Experiment>();
                ExperimentSearchCriteria crit = (ExperimentSearchCriteria) invocation.getArguments()[0];
                if (!StringUtils.equals("no-experiment", crit.getPublicIdentifier())) {
                    Experiment experiment = new Experiment();
                    experiment.setPublicIdentifier(crit.getPublicIdentifier());
                    experiment.setId(crit.getPublicIdentifier());
                    experiment.setLastDataModificationDate(DateUtils.addDays(new Date(), -1));
                    result.getResults().add(experiment);
                }
                return result;
            }
        });
        when(searchService.searchForBiomaterials(any(BiomaterialSearchCriteria.class), any(LimitOffset.class))).thenAnswer(new Answer<SearchResult<Biomaterial>>() {
            @Override
            public SearchResult<Biomaterial> answer(InvocationOnMock invocation) throws Throwable {
                Biomaterial sample = new Biomaterial();
                sample.setName("sample");
                sample.setLastModifiedDataTime(DateUtils.addDays(new Date(), -1));
                SearchResult<Biomaterial> result = new SearchResult<Biomaterial>();
                result.getResults().add(sample);
                result.setMaxAllowedResults(-1);
                return result;
            }
        });
        when(searchService.searchForFiles(any(FileSearchCriteria.class), any(LimitOffset.class))).then(new Answer<SearchResult<File>>() {
            @Override
            public SearchResult<File> answer(InvocationOnMock invocation) throws Throwable {
                SearchResult<File> result = new SearchResult<File>();
                File file = new File();
                file.setMetadata(new FileMetadata());
                file.getMetadata().setName("filename");
                result.getResults().add(file);
                return result;
            }
        });
        when(searchService.searchForHybridizations(any(HybridizationSearchCriteria.class), any(LimitOffset.class))).thenAnswer(new Answer<SearchResult<Hybridization>>() {
            @Override
            public SearchResult<Hybridization> answer(InvocationOnMock invocation) throws Throwable {
                ArrayDesign design = new ArrayDesign();
                design.setName("test design");

                Hybridization hybridization = new Hybridization();
                hybridization.setArrayDesign(design);

                SearchResult<Hybridization> result = new SearchResult<Hybridization>();
                result.getResults().add(hybridization);
                return result;
            }
        });
        when(searchService.searchByExample(any(ExampleSearchCriteria.class), any(LimitOffset.class))).then(new Answer<SearchResult<AbstractCaArrayEntity>>() {
            @Override
            public SearchResult<AbstractCaArrayEntity> answer(InvocationOnMock invocation) throws Throwable {
                ExampleSearchCriteria<AbstractCaArrayEntity> crit = (ExampleSearchCriteria<AbstractCaArrayEntity>) invocation.getArguments()[0];
                SearchResult<AbstractCaArrayEntity> result = new SearchResult<AbstractCaArrayEntity>();
                result.getResults().add(crit.getExample());
                return result;
            }
        });
        when(searchService.searchForQuantitationTypes(any(QuantitationTypeSearchCriteria.class))).thenAnswer(new Answer<List<QuantitationType>>() {
            @Override
            public List<QuantitationType> answer(InvocationOnMock invocation) throws Throwable {
                List<QuantitationType> results = new ArrayList<QuantitationType>();
                QuantitationType quantitationType = new QuantitationType();
                quantitationType.setName("DataMatrixCopyNumber.Log2Ratio");
                results.add(quantitationType);
                return results;
            }
        });

        CaArrayServiceFactory factory = mock(CaArrayServiceFactory.class);
        when(factory.createDataService(any(ServerConnectionProfile.class))).thenReturn(dataService);
        when(factory.createSearchService(any(ServerConnectionProfile.class))).thenReturn(searchService);
        caArrayFacade.setServiceFactory(factory);

        CaIntegrator2Dao dao = mock(CaIntegrator2Dao.class);
        when(dao.getPlatform(anyString())).thenReturn(createTestPlatform());
        caArrayFacade.setDao(dao);
    }

    @Test
    public void testGetSamples() throws ConnectionException, ExperimentNotFoundException {
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
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setExperimentIdentifier("test-data");
        genomicSource.setPlatformName("test design");
        genomicSource.setPlatformVendor(PlatformVendorEnum.AFFYMETRIX);
        genomicSource.setStudyConfiguration(new StudyConfiguration());
        Sample sample = new Sample();
        sample.setName("sample");
        genomicSource.getSamples().add(sample);
        ArrayDataValues values = caArrayFacade.retrieveData(genomicSource);
        assertNotNull(values);
        assertEquals(1, values.getArrayDatas().size());
        for (ArrayData arrayData : values.getArrayDatas()) {
            assertEquals((float) 1.1, values.getFloatValue(arrayData, reporter1, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
            assertEquals((float) 2.2, values.getFloatValue(arrayData, reporter2, ArrayDataValueType.EXPRESSION_SIGNAL), 0);
        }
    }

    @Test
    public void testAgilentRetrieveData() throws ConnectionException, DataRetrievalException {
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setExperimentIdentifier("test-data");
        genomicSource.setPlatformName("test design");
        genomicSource.setPlatformVendor(PlatformVendorEnum.AGILENT);
        genomicSource.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        genomicSource.setStudyConfiguration(new StudyConfiguration());
        Sample sample = new Sample();
        sample.setName("sample");
        genomicSource.getSamples().add(sample);
        List<ArrayDataValues> valuesList = caArrayFacade.retrieveDnaAnalysisData(genomicSource, arrayDataService);
        assertEquals(1, valuesList.size());
        ArrayDataValues arrayDataValues = valuesList.get(0);
        assertEquals(2, arrayDataValues.getReporters().size());
    }

    /**
     * Tests checking for sample updates.
     */
    @Test
    public void checkForSampleUpdates() throws ConnectionException, ExperimentNotFoundException {
       Map<String, Date> updates = caArrayFacade.checkForSampleUpdates("sample-exp", mock(ServerConnectionProfile.class));
       assertFalse(updates.isEmpty());
       assertEquals(1, updates.size());
       for (Date modifiedDate  : updates.values()) {
           assertNotNull(modifiedDate);
       }
    }

    /**
     * Tests retrieval of the data modification date.
     */
    @Test
    public void getLastDataModificationDate() throws ConnectionException, ExperimentNotFoundException {
        GenomicDataSourceConfiguration config = new GenomicDataSourceConfiguration();
        config.setExperimentIdentifier("sample-data");
        Date dataModDate = caArrayFacade.getLastDataModificationDate(config);
        assertNotNull(dataModDate);
    }

    private GeneExpressionReporter createTestReporter(String name) {
        GeneExpressionReporter reporter = new GeneExpressionReporter();
        reporter.setName(name);
        return reporter;
    }

    private DnaAnalysisReporter createDnaAnalysisTestReporter(String name) {
        DnaAnalysisReporter reporter = new DnaAnalysisReporter();
        reporter.setName(name);
        return reporter;
    }

    private DesignElement createTestProbeSet(String name) {
        DesignElement probeSet = new DesignElement();
        probeSet.setName(name);
        return probeSet;
    }

    private Platform createTestPlatform() {
        Platform platform = new Platform();
        ReporterList reporters = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        reporters.setId(1L);
        reporters.getReporters().add(reporter1);
        reporters.getReporters().add(reporter2);
        reporter1.setReporterList(reporters);
        reporter2.setReporterList(reporters);
        reporters = platform.addReporterList("reporterList2", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
        reporters.setId(2L);
        reporters.getReporters().add(reporter3);
        reporters.getReporters().add(reporter4);
        reporter3.setReporterList(reporters);
        reporter4.setReporterList(reporters);
        return  platform;
    }
}
