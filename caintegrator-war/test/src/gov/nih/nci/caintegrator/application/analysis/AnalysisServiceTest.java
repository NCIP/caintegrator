/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator.application.analysis.AbstractKMParameters;
import gov.nih.nci.caintegrator.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator.application.analysis.AnalysisMethodInvocation;
import gov.nih.nci.caintegrator.application.analysis.AnalysisParameter;
import gov.nih.nci.caintegrator.application.analysis.AnalysisParameterType;
import gov.nih.nci.caintegrator.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator.application.analysis.AnalysisServiceImpl;
import gov.nih.nci.caintegrator.application.analysis.AnalysisServiceType;
import gov.nih.nci.caintegrator.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator.application.analysis.ExpressionTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.GctDataset;
import gov.nih.nci.caintegrator.application.analysis.GenePatternClientFactory;
import gov.nih.nci.caintegrator.application.analysis.IntegerParameterValue;
import gov.nih.nci.caintegrator.application.analysis.InvalidSurvivalValueDefinitionException;
import gov.nih.nci.caintegrator.application.analysis.KMAnnotationBasedParameters;
import gov.nih.nci.caintegrator.application.analysis.KMGeneExpressionBasedParameters;
import gov.nih.nci.caintegrator.application.analysis.KMQueryBasedParameters;
import gov.nih.nci.caintegrator.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator.application.analysis.SessionAnalysisResultsManager;
import gov.nih.nci.caintegrator.application.analysis.StatusUpdateListener;
import gov.nih.nci.caintegrator.application.analysis.StringParameterValue;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.AbstractGEPlotParameters;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.ControlSamplesNotMappedException;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.GEPlotAnnotationBasedParameters;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.GEPlotClinicalQueryBasedParameters;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.GEPlotGenomicQueryBasedParameters;
import gov.nih.nci.caintegrator.application.analysis.geneexpression.GenesNotFoundInStudyException;
import gov.nih.nci.caintegrator.application.analysis.grid.GenePatternGridRunner;
import gov.nih.nci.caintegrator.application.analysis.grid.comparativemarker.ComparativeMarkerSelectionParameters;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticParameters;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticRefgeneFileEnum;
import gov.nih.nci.caintegrator.application.analysis.grid.pca.PCAParameters;
import gov.nih.nci.caintegrator.application.analysis.grid.preprocess.PreprocessDatasetParameters;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVResult;
import gov.nih.nci.caintegrator.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotServiceImpl;
import gov.nih.nci.caintegrator.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator.application.kmplot.KMPlotServiceCaIntegratorImpl;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;
import gov.nih.nci.caintegrator.data.CaIntegrator2DaoStub;
import gov.nih.nci.caintegrator.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.AnalysisJobStatusEnum;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.ComparativeMarkerSelectionAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultColumn;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultRow;
import gov.nih.nci.caintegrator.domain.application.GenomicDataResultValue;
import gov.nih.nci.caintegrator.domain.application.GisticAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.PrincipalComponentAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.ParameterException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.file.FileManagerImpl;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMCriteriaDTO;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.genepattern.gistic.Marker;
import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.TaskExecutor;
import org.genepattern.webservice.TaskInfo;
import org.genepattern.webservice.WebServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AnalysisServiceTest extends AbstractMockitoTest {

    private AnalysisService service;
    private GenePatternClientFactory genePatternClientFactory;
    private DaoForAnalysisServiceStub daoStub = new DaoForAnalysisServiceStub();
    private SessionAnalysisResultsManager sessionAnalysisResultsManager = new SessionAnalysisResultsManager();

    private GenePatternGridRunner genePatternGridRunner;
    private GenePatternClient genePatternClient;

    private PlotTypeEnum kmPlotType;

    @Before
    public void setUp() throws Exception {
        when(queryManagementService.execute(any(Query.class))).then(new Answer<QueryResult>() {
            @Override
            public QueryResult answer(InvocationOnMock invocation) throws Throwable {
                Query q = (Query) invocation.getArguments()[0];
                KMPlotStudyCreator creator = new KMPlotStudyCreator();
                QueryResult qr = creator.retrieveFakeQueryResults(q);
                if (kmPlotType == PlotTypeEnum.ANNOTATION_BASED) {
                    qr = creator.retrieveQueryResultForAnnotationBased(q);
                }
                return qr;
            }
        });
        when(queryManagementService.executeGenomicDataQuery(any(Query.class))).then(new Answer<GenomicDataQueryResult>() {
            @Override
            public GenomicDataQueryResult answer(InvocationOnMock invocation) throws Throwable {
                Query q = (Query) invocation.getArguments()[0];
                GenomicDataQueryResult result = new GenomicDataQueryResult();
                q.setGeneExpressionPlatform(daoStub.getPlatform("platformName"));
                result.setQuery(q);
                GenomicDataResultRow row = new GenomicDataResultRow();
                GenomicDataResultValue value = new GenomicDataResultValue();
                GenomicDataResultColumn column = result.addColumn();
                SampleAcquisition sampleAcquisition = new SampleAcquisition();
                StudySubjectAssignment assignment = new StudySubjectAssignment();
                assignment.setId(Long.valueOf(1));
                sampleAcquisition.setAssignment(assignment);
                column.setSampleAcquisition(sampleAcquisition);
                Sample sample = new Sample();
                sample.setName("sample");
                sampleAcquisition.setSample(sample);
                value.setColumn(column);
                value.setValue(1f);
                GeneExpressionReporter reporter = new GeneExpressionReporter();
                Gene gene = new Gene();
                gene.setSymbol("EGFR");
                reporter.getGenes().add(gene);
                row.setReporter(reporter);
                row.getValues().add(value);
                result.getRowCollection().add(row);
                return result;
            }
        });

        when(queryManagementService.retrieveSegmentDataQuery(any(Query.class))).then(new Answer<Collection<SegmentData>>() {
            @Override
            public Collection<SegmentData> answer(InvocationOnMock invocation) throws Throwable {
                Set<SegmentData> data = new HashSet<SegmentData>();
                SegmentData sd = new SegmentData();
                sd.setArrayData(new ArrayData());
                data.add(sd);
                return data;
            }
        });
        AnalysisServiceImpl serviceImpl = new AnalysisServiceImpl();
        KMPlotServiceCaIntegratorImpl caKMPlotService = new KMPlotServiceCaIntegratorImpl();
        caKMPlotService.setCaIntegratorPlotService(kmPlotService);
        GeneExpressionPlotServiceImpl gePlotService = new GeneExpressionPlotServiceImpl();
        FileManagerImpl fileManagerImpl = new FileManagerImpl();
        fileManagerImpl.setConfigurationHelper(configurationHelper);

        setUpGenePatternClient();
        genePatternClientFactory = mock(GenePatternClientFactory.class);
        when(genePatternClientFactory.retrieveClient(any(ServerConnectionProfile.class))).thenReturn(genePatternClient);
        when(genePatternClientFactory.retrieveOldGenePatternClient(any(ServerConnectionProfile.class))).thenReturn(genePatternClient);

        when(analysisFileManager.getFileManager()).thenReturn(fileManagerImpl);

        serviceImpl.setGenePatternClientFactory(genePatternClientFactory);
        serviceImpl.setDao(daoStub);
        serviceImpl.setKmPlotService(caKMPlotService);
        serviceImpl.setGePlotService(gePlotService);
        serviceImpl.setQueryManagementService(queryManagementService);
        serviceImpl.setFileManager(fileManager);
        serviceImpl.setAnalysisFileManager(analysisFileManager);
        service = serviceImpl;

        genePatternGridRunner = mock(GenePatternGridRunner.class);
        serviceImpl.setGenePatternGridRunner(genePatternGridRunner);

        serviceImpl.setArrayDataService(arrayDataService);
        serviceImpl.setSessionAnalysisResultsManager(sessionAnalysisResultsManager);

    }

    @SuppressWarnings("unchecked")
    private void setUpGenePatternClient() throws Exception {
        genePatternClient = mock(GenePatternClient.class);
        when(genePatternClient.runAnalysis(anyString(), anyListOf(ParameterInfo.class))).thenReturn(new JobInfo());
        when(genePatternClient.getStatus(any(JobInfo.class))).thenAnswer(new Answer<JobInfo>() {
            @Override
            public JobInfo answer(InvocationOnMock invocation) throws Throwable {
                JobInfo jobInfo = (JobInfo) invocation.getArguments()[0];
                jobInfo.setStatus("Completed");
                return jobInfo;
            }
        });

        TaskInfo[] tasks = new TaskInfo[4];
        tasks[0] = new TaskInfo();
        tasks[0].setName("task1");
        tasks[0].setDescription("description1");
        tasks[0].setTaskInfoAttributes(new HashMap<String, String>());
        tasks[0].setParameterInfoArray(new ParameterInfo[3]);
        tasks[0].getParameterInfoArray()[0] = new ParameterInfo();
        tasks[0].getParameterInfoArray()[0].setName("parameter1");
        tasks[0].getParameterInfoArray()[0].setAttributes(new HashMap<String, String>());
        tasks[0].getParameterInfoArray()[0].getAttributes().put("type", "java.lang.String");
        tasks[0].getParameterInfoArray()[0].getAttributes().put("optional", "");
        tasks[0].getParameterInfoArray()[0].getAttributes().put(TaskExecutor.PARAM_INFO_DEFAULT_VALUE[0], "default");


        tasks[0].getParameterInfoArray()[1] = new ParameterInfo();
        tasks[0].getParameterInfoArray()[1].setName("parameter2");
        tasks[0].getParameterInfoArray()[1].setAttributes(new HashMap<String, String>());
        tasks[0].getParameterInfoArray()[1].getAttributes().put("type", "java.io.File");
        tasks[0].getParameterInfoArray()[1].getAttributes().put("fileFormat", "gct;res");
        tasks[0].getParameterInfoArray()[1].getAttributes().put("optional", "on");

        tasks[0].getParameterInfoArray()[2] = new ParameterInfo();
        tasks[0].getParameterInfoArray()[2].setName("parameter3");
        tasks[0].getParameterInfoArray()[2].setAttributes(new HashMap<String, String>());
        tasks[0].getParameterInfoArray()[2].getAttributes().put(TaskExecutor.PARAM_INFO_DEFAULT_VALUE[0], "2");
        tasks[0].getParameterInfoArray()[2].getAttributes().put("type", "java.lang.Integer");
        tasks[0].getParameterInfoArray()[2].setValue("1=choice1;2=choice2");

        tasks[1] = new TaskInfo();
        tasks[1].setName("task2");
        tasks[1].setDescription("description2");
        tasks[1].setParameterInfoArray(new ParameterInfo[2]);
        tasks[1].setTaskInfoAttributes(new HashMap<String, String>());
        tasks[1].getParameterInfoArray()[0] = new ParameterInfo();
        tasks[1].getParameterInfoArray()[0].setName("parameter1");
        tasks[1].getParameterInfoArray()[0].setAttributes(new HashMap<String, String>());
        tasks[1].getParameterInfoArray()[0].getAttributes().put("type", "java.lang.String");
        tasks[1].getParameterInfoArray()[1] = new ParameterInfo();
        tasks[1].getParameterInfoArray()[1].setName("parameter2");
        tasks[1].getParameterInfoArray()[1].setAttributes(new HashMap<String, String>());
        tasks[1].getParameterInfoArray()[1].getAttributes().put("type", "java.io.File");
        tasks[1].getParameterInfoArray()[1].getAttributes().put("fileFormat", "cls");

        tasks[2] = new TaskInfo();
        tasks[2].setName("task3");
        tasks[2].setDescription("description3");
        tasks[2].setTaskInfoAttributes(new HashMap<String, String>());
        tasks[2].setParameterInfoArray(new ParameterInfo[3]);
        tasks[2].getParameterInfoArray()[0] = new ParameterInfo();
        tasks[2].getParameterInfoArray()[0].setName("parameter1");
        tasks[2].getParameterInfoArray()[0].setAttributes(new HashMap<String, String>());
        tasks[2].getParameterInfoArray()[0].getAttributes().put("type", "java.lang.Float");

        tasks[2].getParameterInfoArray()[1] = new ParameterInfo();
        tasks[2].getParameterInfoArray()[1].setName("parameter2");
        tasks[2].getParameterInfoArray()[1].setAttributes(new HashMap<String, String>());
        tasks[2].getParameterInfoArray()[1].getAttributes().put("type", "java.io.File");
        tasks[2].getParameterInfoArray()[1].getAttributes().put("fileFormat", "gct;res");

        tasks[2].getParameterInfoArray()[2] = new ParameterInfo();
        tasks[2].getParameterInfoArray()[2].setName("parameter3");
        tasks[2].getParameterInfoArray()[2].setAttributes(new HashMap<String, String>());
        tasks[2].getParameterInfoArray()[2].getAttributes().put("type", "java.io.File");
        tasks[2].getParameterInfoArray()[2].getAttributes().put("fileFormat", "cls");

        tasks[3] = new TaskInfo();
        tasks[3].setName("task3");
        tasks[3].setDescription("description3");
        Map<String, String> task4Attributes = new HashMap<String, String>();
        task4Attributes.put("taskType", "Visualizer");
        tasks[3].setTaskInfoAttributes(task4Attributes);
        tasks[3].setParameterInfoArray(new ParameterInfo[3]);
        when(genePatternClient.getTasks()).thenReturn(tasks);
    }

    @Test
    public void testGetGenePatternMethods() throws WebServiceException {
        ServerConnectionProfile server = new ServerConnectionProfile();
        List<AnalysisMethod> methods = service.getGenePatternMethods(server);
        assertEquals(2, methods.size());
        assertEquals(AnalysisServiceType.GENEPATTERN, methods.get(0).getServiceType());
        assertEquals("task1", methods.get(0).getName());
        assertEquals("description1", methods.get(0).getDescription());
        assertEquals(3, methods.get(0).getParameters().size());
        assertEquals("parameter1", methods.get(0).getParameters().get(0).getName());
        assertEquals(true, methods.get(0).getParameters().get(0).isRequired());
        assertEquals(false, methods.get(0).getParameters().get(1).isRequired());
        assertEquals(AnalysisParameterType.STRING, methods.get(0).getParameters().get(0).getType());
        AnalysisParameter parameterWithChoices = methods.get(0).getParameters().get(2);
        IntegerParameterValue value = (IntegerParameterValue) parameterWithChoices.getDefaultValue();
        assertEquals(methods.get(0).getParameters().get(2), value.getParameter());
        assertEquals(Integer.valueOf(2), value.getValue());
        assertEquals("parameter3", parameterWithChoices.getName());
        assertEquals(2, parameterWithChoices.getChoiceKeys().size());
        assertEquals(Integer.valueOf(1), ((IntegerParameterValue) parameterWithChoices.getChoice("choice1")).getValue());
    }

    @Test
    public void testGisticWebService() throws ConnectionException, InvalidCriterionException, ParameterException, IOException, DataRetrievalException {
        StatusUpdateTestListener listener = new StatusUpdateTestListener();
        GisticAnalysisJob job = new GisticAnalysisJob();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        daoStub.subscription = subscription;
        job.setSubscription(subscription);
        GisticParameters parameters = new GisticParameters();
        job.getGisticAnalysisForm().setGisticParameters(parameters);
        parameters.getServer().setUrl("http://genepattern.broadinstitute.org/gp/services/Analysis");
        parameters.setRefgeneFile(GisticRefgeneFileEnum.HUMAN_HG16);
        service.executeGridGistic(listener, job);
        assertEquals(1, subscription.getCopyNumberAnalysisCollection().size());
        assertTrue(listener.statuses.contains(AnalysisJobStatusEnum.PROCESSING_LOCALLY));
        assertTrue(listener.statuses.contains(AnalysisJobStatusEnum.PROCESSING_REMOTELY));
        verify(fileManager, times(1)).createMarkersFile(any(StudySubscription.class), any(Marker[].class));
        verify(fileManager, times(1)).createSamplesFile(any(StudySubscription.class), any(SampleWithChromosomalSegmentSet[].class));

        verify(genePatternGridRunner, never()).runGistic(any(StatusUpdateListener.class),
                any(GisticAnalysisJob.class), any(File.class), any(File.class), any(File.class));
        parameters.setCnvSegmentsToIgnoreFile(new File(System.getProperty("java.io.tmpdir") + File.separator + "tmpFile"));
        parameters.getServer().setUrl("something/Gistic");
        service.executeGridGistic(listener, job);
        verify(genePatternGridRunner, atLeastOnce()).runGistic(any(StatusUpdateListener.class),
                any(GisticAnalysisJob.class), any(File.class), any(File.class), any(File.class));
        verify(fileManager, times(1)).renameCnvFile(any(File.class));
        Query clinicalQuery = new Query();
        clinicalQuery.setName("clinicalQueryName");
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName("sampleSetName");
        parameters.setExcludeControlSampleSet(sampleSet);
        parameters.setClinicalQuery(clinicalQuery);
        parameters.getServer().setHostname("hostname");
        assertTrue(job.toString().contains(clinicalQuery.getName()));
        assertTrue(job.toString().contains(sampleSet.getName()));
        assertTrue(job.toString().contains(parameters.getServer().getHostname()));
    }

    private static class StatusUpdateTestListener implements StatusUpdateListener {
        private Set<AnalysisJobStatusEnum> statuses = new HashSet<AnalysisJobStatusEnum>();

        public void updateStatus(AbstractPersistedAnalysisJob job) {
            statuses.add(job.getStatus());
        }
    }

    @Test
    public void testExecuteIGV() throws ConnectionException, InvalidCriterionException {
        Query query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.setName("queryNameString");

        StudySubscription subscription = new StudySubscription();
        subscription.setId(1L);
        String IGVDownloadUrl = ConfigurationParameter.BROAD_HOSTED_IGV_URL.getDefaultValue();
        IGVParameters igvParameters = new IGVParameters();
        igvParameters.setUrlPrefix("http://localhost:8080/caintegrator2/igv/runIgv.do?JSESSIONID=sessionId&file=");
        igvParameters.setSessionId("sessionId");
        igvParameters.setQuery(query);
        igvParameters.setStudySubscription(subscription);
        try {
            service.executeIGV(igvParameters);
        } catch (Exception e) {
            assertEquals("Unable to create IGV viewer: No data found from selection.", e.getMessage());
        }
        verify(analysisFileManager, times(0)).createIGVGctFile(any(GctDataset.class), anyString());
        verify(analysisFileManager, times(0)).createIGVGctFile(any(GctDataset.class), any(Study.class), anyString());

        igvParameters.getStudySubscription().setId(2L);
        query.getColumnCollection().add(new ResultColumn());
        igvParameters.setQuery(query);
        String resultURL = service.executeIGV(igvParameters);

        verify(analysisFileManager, times(1)).createIGVGctFile(any(GctDataset.class), anyString());
        verify(analysisFileManager, times(1)).createIGVSegFile(anyCollectionOf(SegmentData.class), anyString(), anyBoolean());
        verify(analysisFileManager, times(1)).createIGVSampleClassificationFile(any(QueryResult.class), anyString(),
                anyCollectionOf(ResultColumn.class), any(CopyNumberCriterionTypeEnum.class));
        verify(analysisFileManager, times(1)).createIGVSessionFile(any(IGVParameters.class), any(IGVResult.class));
        assertEquals(
                IGVDownloadUrl + "http://localhost:8080/caintegrator2/igv/runIgv.do%3FJSESSIONID%3DsessionId%26file%3DigvSession.xml",
                resultURL);

        // Now test the All Platforms way.
        Platform platform1 = new Platform();
        platform1.setId(1l);
        PlatformConfiguration platformConfiguration1 = new PlatformConfiguration();
        platform1.setPlatformConfiguration(platformConfiguration1);
        platformConfiguration1.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        platformConfiguration1.setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);

        Platform platform2 = new Platform();
        platform2.setId(2l);
        PlatformConfiguration platformConfiguration2 = new PlatformConfiguration();
        platform2.setPlatformConfiguration(platformConfiguration2);
        platformConfiguration2.setPlatformType(PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER);
        platformConfiguration2.setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);
        igvParameters.setViewAllData(true);
        igvParameters.setUseCGHCall(true);
        igvParameters.getPlatforms().add(platform1);
        igvParameters.getPlatforms().add(platform2);
        resultURL = service.executeIGV(igvParameters);
        verify(analysisFileManager, times(1)).createIGVGctFile(any(GctDataset.class), anyString());
        verify(analysisFileManager, times(1)).createIGVSegFile(anyCollectionOf(SegmentData.class), anyString(), anyBoolean());
        verify(analysisFileManager, times(2)).createIGVSampleClassificationFile(any(QueryResult.class), anyString(),
                anyCollectionOf(ResultColumn.class), any(CopyNumberCriterionTypeEnum.class));
        verify(analysisFileManager, times(2)).createIGVSessionFile(any(IGVParameters.class), any(IGVResult.class));
    }

    @Test
    public void testExecuteHeatmap() throws ConnectionException, InvalidCriterionException, IOException {
        Query query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.setName("queryNameString");

        StudySubscription subscription = new StudySubscription();
        Study study = new Study();
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        study.setStudyConfiguration(studyConfiguration);
        subscription.setStudy(study);
        query.setSubscription(subscription);
        HeatmapParameters heatmapParameters = new HeatmapParameters();
        heatmapParameters.setUrlPrefix("http://localhost:8080/caintegrator2/viewer/runViewer.do?JSESSIONID=sessionId&file=");
        heatmapParameters.setSessionId("sessionId");
        heatmapParameters.setQuery(query);
        heatmapParameters.setStudySubscription(subscription);
        heatmapParameters.setViewAllData(true);
        heatmapParameters.setPlatform(new Platform());

        GenomicDataSourceConfiguration genomicSource2 = new GenomicDataSourceConfiguration();
        genomicSource2.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
        studyConfiguration.getGenomicDataSources().add(genomicSource2);
        query.getColumnCollection().add(new ResultColumn());
        heatmapParameters.setQuery(query);
        String resultURL = service.executeHeatmap(heatmapParameters);
        verify(analysisFileManager, times(1)).createHeatmapGenomicFile(any(HeatmapParameters.class), any(HeatmapResult.class),
                anyCollectionOf(SegmentData.class), any(GeneLocationConfiguration.class), any(CBSToHeatmapFactory.class));
        verify(analysisFileManager, times(1)).createHeatmapSampleClassificationFile(any(QueryResult.class),
                anyString(), anyCollectionOf(ResultColumn.class));
        verify(analysisFileManager, times(1)).createHeatmapJnlpFile(any(HeatmapParameters.class), any(HeatmapResult.class));
        assertEquals(
                "http://localhost:8080/caintegrator2/viewer/runViewer.do?JSESSIONID=sessionId&file=heatmapLaunch.jnlp",
                resultURL);

        heatmapParameters.setUseCGHCall(true);
        resultURL = service.executeHeatmap(heatmapParameters);
        verify(analysisFileManager, times(2)).createHeatmapGenomicFile(any(HeatmapParameters.class), any(HeatmapResult.class),
                anyCollectionOf(SegmentData.class), any(GeneLocationConfiguration.class), any(CBSToHeatmapFactory.class));
        verify(analysisFileManager, times(2)).createHeatmapSampleClassificationFile(any(QueryResult.class),
                anyString(), anyCollectionOf(ResultColumn.class));
        verify(analysisFileManager, times(2)).createHeatmapJnlpFile(any(HeatmapParameters.class), any(HeatmapResult.class));
    }

    @Test
    public void testExecuteGridPCA() throws ConnectionException, InvalidCriterionException {
        StatusUpdateTestListener updater = new StatusUpdateTestListener();
        PrincipalComponentAnalysisJob job = new PrincipalComponentAnalysisJob();
        PCAParameters pcaParameters = new PCAParameters();
        Query query = new Query();
        query.setCompoundCriterion(new CompoundCriterion());
        query.setName("queryNameString");
        pcaParameters.getClinicalQueries().add(query);
        SampleSet sampleSet = new SampleSet();
        sampleSet.setId(1l);
        sampleSet.setName("sampleSetNameString");
        pcaParameters.setExcludedControlSampleSet(sampleSet);
        job.getForm().setPcaParameters(pcaParameters);
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        job.setSubscription(subscription);
        service.executeGridPCA(updater, job);
        verify(genePatternGridRunner, atLeastOnce()).runPCA(any(StatusUpdateListener.class),
                any(PrincipalComponentAnalysisJob.class), any(File.class));
        verify(fileManager, times(1)).createGctFile(any(StudySubscription.class), any(GctDataset.class), anyString());
        verify(fileManager, times(1)).createInputZipFile(any(StudySubscription.class),
                any(AbstractPersistedAnalysisJob.class), anyString(), any(File[].class));
        assertTrue(job.toString().contains(query.getName()));
        assertTrue(job.toString().contains(sampleSet.getName()));
    }

    @Test
    public void testExecuteGridPreprocessComparativeMarker() throws ConnectionException, InvalidCriterionException {
        StatusUpdateTestListener updater = new StatusUpdateTestListener();
        ComparativeMarkerSelectionAnalysisJob job = new ComparativeMarkerSelectionAnalysisJob();
        PreprocessDatasetParameters preprocessParams = new PreprocessDatasetParameters();
        ComparativeMarkerSelectionParameters cmsParams = new ComparativeMarkerSelectionParameters();
        Query query = new Query();
        ResultColumn column = new ResultColumn();
        AnnotationFieldDescriptor descriptor = new AnnotationFieldDescriptor();
        descriptor.setDefinition(new AnnotationDefinition());
        descriptor.setAnnotationEntityType(EntityTypeEnum.SUBJECT);
        column.setAnnotationFieldDescriptor(descriptor);
        column.setColumnIndex(0);
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.getColumnCollection().add(column);
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setName("queryNameString");
        cmsParams.getClinicalQueries().add(query);
        SampleSet sampleSet = new SampleSet();
        sampleSet.setId(1l);
        sampleSet.setName("sampleSetNameString");
        preprocessParams.setExcludedControlSampleSet(sampleSet);
        job.getForm().setComparativeMarkerSelectionParameters(cmsParams);
        job.getForm().setPreprocessDatasetparameters(preprocessParams);
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        query.setSubscription(subscription);
        subscription.setStudy(study);
        job.setSubscription(subscription);
		kmPlotType = PlotTypeEnum.ANNOTATION_BASED;
        service.executeGridPreprocessComparativeMarker(updater, job);
        verify(fileManager, times(1)).createClassificationFile(any(StudySubscription.class),
                any(SampleClassificationParameterValue.class), anyString());
        verify(fileManager, times(1)).createGctFile(any(StudySubscription.class), any(GctDataset.class), anyString());
        verify(genePatternGridRunner, atLeastOnce()).runPreprocessComparativeMarkerSelection(any(StatusUpdateListener.class),
                any(ComparativeMarkerSelectionAnalysisJob.class), any(File.class), any(File.class));
        assertTrue(job.toString().contains(query.getName()));
        assertTrue(job.toString().contains(sampleSet.getName()));
    }

    @Test
    public void testExecuteGenePatternJob() throws WebServiceException {
        AnalysisMethodInvocation invocation = new AnalysisMethodInvocation();
        ServerConnectionProfile server = new ServerConnectionProfile();
        AnalysisMethod method = new AnalysisMethod();
        server.setUrl("http://localhost");
        method.setName("method");
        invocation.setMethod(method);
        StringParameterValue parameterValue = new StringParameterValue();
        parameterValue.setValue("value");
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setType(AnalysisParameterType.STRING);
        parameter.setName("parameter");
        method.getParameters().add(parameter);
        parameterValue.setParameter(parameter);
        invocation.setParameterValue(parameter, parameterValue);
        service.executeGenePatternJob(server, invocation);
        verify(genePatternClient, times(1)).runAnalysis(anyString(), anyListOf(ParameterInfo.class));
    }

    private void runKMPlotTest(KMPlotStudyCreator studyCreator, StudySubscription subscription, AbstractKMParameters annotationParameters)
    throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlot kmPlot = service.createKMPlot(subscription, annotationParameters);

        assertNotNull(kmPlot);
        verify(kmPlotService, atLeastOnce()).computeLogRankPValueBetween(any(KMSampleGroupCriteriaDTO.class), any(KMSampleGroupCriteriaDTO.class));
        verify(kmPlotService, atLeastOnce()).getChart(any(KMCriteriaDTO.class));
        boolean exceptionCaught = false;
        try { // Try giving no survival value definition.
            annotationParameters.setSurvivalValueDefinition(null);
            kmPlot = service.createKMPlot(subscription, annotationParameters);
        } catch (InvalidSurvivalValueDefinitionException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);

        exceptionCaught = false;
        studyCreator.getSurvivalValueDefinition().setLastFollowupDate(null);
        try { // Try giving survivalValueDefinition without a followup date
            annotationParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
            kmPlot = service.createKMPlot(subscription, annotationParameters);
        } catch (InvalidSurvivalValueDefinitionException e) {
            exceptionCaught = true;
        }
        assertTrue(exceptionCaught);
    }

    @Test
    public void testCreateAnnotationBasedKMPlot() throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        kmPlotType = PlotTypeEnum.ANNOTATION_BASED;

        KMAnnotationBasedParameters annotationParameters = new KMAnnotationBasedParameters();
        assertFalse(annotationParameters.validate());
        annotationParameters.setSelectedAnnotation(studyCreator.getGroupAnnotationFieldDescriptor());
        annotationParameters.getSelectedValues().addAll(studyCreator.getPlotGroupValues());
        annotationParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
        assertTrue(annotationParameters.validate());
        runKMPlotTest(studyCreator, subscription, annotationParameters);

    }

    @Test
    public void testCreateGeneExpressionBasedKMPlot() throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);

        KMGeneExpressionBasedParameters geneExpressionParameters = new KMGeneExpressionBasedParameters();
        geneExpressionParameters.setExpressionType(ExpressionTypeEnum.FOLD_CHANGE);
        assertFalse(geneExpressionParameters.validate());
        geneExpressionParameters.setGeneSymbol("EGFR");
        geneExpressionParameters.setOverValue(2.0F);
        geneExpressionParameters.setUnderValue(2.0F);
        geneExpressionParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
        geneExpressionParameters.setControlSampleSetName("name");
        assertTrue(geneExpressionParameters.validate());
        runKMPlotTest(studyCreator, subscription, geneExpressionParameters);
        studyCreator.createKMPlotStudy();
        geneExpressionParameters.setExpressionType(ExpressionTypeEnum.EXPRESSION_LEVEL);
        assertFalse(geneExpressionParameters.validate());
        geneExpressionParameters.setOverValue(12.0F);
        geneExpressionParameters.setUnderValue(2.0F);
        geneExpressionParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
        geneExpressionParameters.setControlSampleSetName(null);
        assertTrue(geneExpressionParameters.validate());
        runKMPlotTest(studyCreator, subscription, geneExpressionParameters);
    }

    @Test
    public void testCreateQueryBasedKMPlot() throws InvalidCriterionException, GenesNotFoundInStudyException, InvalidSurvivalValueDefinitionException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        KMQueryBasedParameters queryBasedParameters = new KMQueryBasedParameters();
        assertFalse(queryBasedParameters.validate());
        queryBasedParameters.setExclusiveGroups(true);
        queryBasedParameters.setAddPatientsNotInQueriesGroup(true);
        Query query1 = new Query();
        query1.setId(Long.valueOf(1));
        query1.setSubscription(subscription);
        query1.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        queryBasedParameters.getQueries().add(query1);
        Query query2 = new Query();
        query2.setId(Long.valueOf(2));
        query2.setSubscription(subscription);
        queryBasedParameters.getQueries().add(query2);
        queryBasedParameters.setSurvivalValueDefinition(studyCreator.getSurvivalValueDefinition());
        assertTrue(queryBasedParameters.validate());
        runKMPlotTest(studyCreator, subscription, queryBasedParameters);
        assertEquals(ResultTypeEnum.CLINICAL, query1.getResultType());
    }

    private void runGEPlotTest(StudySubscription subscription, AbstractGEPlotParameters parameters)
    throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        GeneExpressionPlotGroup gePlot = service.createGeneExpressionPlot(subscription, parameters);
        assertNotNull(gePlot.getPlot(PlotCalculationTypeEnum.MEAN));
    }

    @Test
    public void testCreateAnnotationBasedGEPlot() throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        kmPlotType = PlotTypeEnum.ANNOTATION_BASED;

        GEPlotAnnotationBasedParameters annotationParameters = new GEPlotAnnotationBasedParameters();
        annotationParameters.setSelectedAnnotation(studyCreator.getGroupAnnotationFieldDescriptor());
        annotationParameters.getSelectedValues().addAll(studyCreator.getPlotGroupValues());
        annotationParameters.setGeneSymbol("egfr");
        annotationParameters.setAddControlSamplesGroup(true);
        annotationParameters.setAddPatientsNotInQueriesGroup(true);
        annotationParameters.setControlSampleSetName("samples");

        assertTrue(annotationParameters.validate());
        runGEPlotTest(subscription, annotationParameters);

    }

    @Test
    public void testCreateGenomicQueryBasedGEPlot() throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        kmPlotType = PlotTypeEnum.QUERY_BASED;
        GEPlotGenomicQueryBasedParameters genomicQueryParameters = new GEPlotGenomicQueryBasedParameters();
        Query query1 = new Query();
        query1.setName("Query1");
        query1.setId(Long.valueOf(1));
        query1.setSubscription(subscription);
        query1.setResultType(ResultTypeEnum.GENE_EXPRESSION);
        query1.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_GENE);
        query1.setCompoundCriterion(new CompoundCriterion());
        assertFalse(genomicQueryParameters.validate());
        genomicQueryParameters.setQuery(query1);
        genomicQueryParameters.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
        runGEPlotTest(subscription, genomicQueryParameters);
        assertEquals(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET, query1.getReporterType());
    }

    @Test
    public void testCreateClinicalQueryBasedGEPlot() throws ControlSamplesNotMappedException, InvalidCriterionException, GenesNotFoundInStudyException {
        KMPlotStudyCreator studyCreator = new KMPlotStudyCreator();
        Study study = studyCreator.createKMPlotStudy();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(study);
        kmPlotType = PlotTypeEnum.QUERY_BASED;
        GEPlotClinicalQueryBasedParameters genomicQueryParameters = new GEPlotClinicalQueryBasedParameters();
        genomicQueryParameters.setAddPatientsNotInQueriesGroup(true);
        genomicQueryParameters.setExclusiveGroups(true);
        Query query1 = new Query();
        query1.setName("Query1");
        query1.setId(Long.valueOf(1));
        query1.setSubscription(subscription);
        query1.setResultType(ResultTypeEnum.CLINICAL);
        genomicQueryParameters.getQueries().add(query1);
        Query query2 = new Query();
        query2.setName("Query2");
        query2.setId(Long.valueOf(2));
        query2.setSubscription(subscription);
        genomicQueryParameters.getQueries().add(query2);
        assertFalse(genomicQueryParameters.validate());
        genomicQueryParameters.setGeneSymbol("EGFR");
        assertTrue(genomicQueryParameters.validate());
        runGEPlotTest(subscription, genomicQueryParameters);
    }

    @Test
    public void testDeleteGisticAnalysis() {
        GisticAnalysis gisticAnalysis = new GisticAnalysis();
        StudySubscription subscription = new StudySubscription();
        subscription.setStudy(new Study());
        subscription.getCopyNumberAnalysisCollection().add(gisticAnalysis);
        gisticAnalysis.setStudySubscription(subscription);
        service.deleteGisticAnalysis(gisticAnalysis);
        assertFalse(subscription.getCopyNumberAnalysisCollection().contains(gisticAnalysis));
        verify(arrayDataService, never()).deleteGisticAnalysisNetCDFFile(any(Study.class), anyLong());
        subscription.getCopyNumberAnalysisCollection().add(gisticAnalysis);
        gisticAnalysis.setStudySubscription(subscription);
        ReporterList reporterList = new ReporterList("test", ReporterTypeEnum.GISTIC_GENOMIC_REGION_REPORTER);
        gisticAnalysis.setReporterList(reporterList);
        service.deleteGisticAnalysis(gisticAnalysis);
        verify(arrayDataService, atLeastOnce()).deleteGisticAnalysisNetCDFFile(any(Study.class), anyLong());
    }

    private static class DaoForAnalysisServiceStub extends CaIntegrator2DaoStub {

        public boolean returnNoGeneSymbols = false;
        public StudySubscription subscription;

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Long id, Class<T> objectClass) {
            if (objectClass.equals(StudySubscription.class)) {
                if (subscription == null) {
                     subscription = new StudySubscription();
                     Study study = new Study();
                     StudyConfiguration studyConfiguration = new StudyConfiguration();
                     study.setStudyConfiguration(studyConfiguration);
                     subscription.setStudy(study);
                }
                if (Long.valueOf(2).equals(id)) {
                    GenomicDataSourceConfiguration genomicSource1 = new GenomicDataSourceConfiguration();
                    genomicSource1.setDataType(PlatformDataTypeEnum.EXPRESSION);
                    GenomicDataSourceConfiguration genomicSource2 = new GenomicDataSourceConfiguration();
                    genomicSource2.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
                    subscription.getStudy().getStudyConfiguration().getGenomicDataSources().add(genomicSource1);
                    subscription.getStudy().getStudyConfiguration().getGenomicDataSources().add(genomicSource2);
                }
                 return (T) subscription;
            } else {
                return super.get(id, objectClass);
            }
        }

        @Override
        public void clear() {
            super.clear();
            returnNoGeneSymbols = false;
        }

        @Override
        public Set<String> retrieveGeneSymbolsInStudy(Collection<String> symbols, Study study) {
            Set<String> newSymbols = new HashSet<String>();
            for (String symbol : symbols) {
               newSymbols.add(symbol);
            }
            return returnNoGeneSymbols ? new HashSet<String>() : newSymbols;
        }
    }
}
