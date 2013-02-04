/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import edu.wustl.icr.asrv1.segment.SampleWithChromosomalSegmentSet;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.analysis.AbstractKMParameters;
import gov.nih.nci.caintegrator.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator.application.analysis.AnalysisMethodInvocation;
import gov.nih.nci.caintegrator.application.analysis.AnalysisParameter;
import gov.nih.nci.caintegrator.application.analysis.AnalysisParameterType;
import gov.nih.nci.caintegrator.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator.application.analysis.CBSToHeatmapFactory;
import gov.nih.nci.caintegrator.application.analysis.GctDataset;
import gov.nih.nci.caintegrator.application.analysis.JobInfoWrapper;
import gov.nih.nci.caintegrator.application.analysis.SampleClassificationParameterValue;
import gov.nih.nci.caintegrator.application.analysis.StringParameterValue;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.application.kmplot.KMPlotImpl;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.Status;
import gov.nih.nci.caintegrator.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator.common.ConfigurationHelper;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.application.AbstractPersistedAnalysisJob;
import gov.nih.nci.caintegrator.domain.application.CopyNumberCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultsZipFile;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.SubjectList;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.Array;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator.external.ncia.NCIABasket;
import gov.nih.nci.caintegrator.external.ncia.NCIADicomJob;
import gov.nih.nci.caintegrator.external.ncia.NCIAFacade;
import gov.nih.nci.caintegrator.file.AnalysisFileManager;
import gov.nih.nci.caintegrator.file.FileManager;
import gov.nih.nci.caintegrator.plots.kaplanmeier.dto.KMSampleGroupCriteriaDTO;
import gov.nih.nci.caintegrator.plots.services.KMPlotService;
import gov.nih.nci.caintegrator.web.DisplayableStudySummary;
import gov.nih.nci.caintegrator.web.action.query.DisplayableResultRow;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.genepattern.gistic.Marker;
import org.genepattern.webservice.JobInfo;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public abstract class AbstractMockitoTest {
    protected CaArrayFacade caArrayFacade;
    protected CaIntegrator2Dao dao;
    protected ArrayDataService arrayDataService;
    protected AnalysisService analysisService;
    protected DeploymentService deploymentService;
    protected WorkspaceService workspaceService;
    protected ConfigurationHelper configurationHelper;
    protected NCIAFacade nciaFacade;
    protected AnalysisFileManager analysisFileManager;
    protected FileManager fileManager;
    protected QueryManagementService queryManagementService;
    protected KMPlotService kmPlotService;
    private StudySubscription studySubscription;

    /**
     * Sets up mocks.
     * @throws Exception on error
     */
    @Before
    public void setUpMocks() throws Exception {
        setUpCaArrayFacade();
        setUpDao();
        setUpArrayDataService();
        setUpAnalysisService();
        setUpDeploymentService();
        setUpConfigurationHelper();
        setUpWorkspaceService();
        setUpNCIAFacade();
        setUpAnalysisFileManager();
        setUpFileManager();
        setUpQueryManagementService();
        setUpKmPlotService();
    }

    /**
     * Sets up the caIntegrator dao mock objects.
     */
    private void setUpDao() throws Exception {
        Study study = new Study();
        study.setStudyConfiguration(new StudyConfiguration());

        GenomicDataSourceConfiguration dataSource = new GenomicDataSourceConfiguration();
        dataSource.setExperimentIdentifier("EXP-1");
        dataSource.setLastModifiedDate(new Date());
        study.getStudyConfiguration().getGenomicDataSources().add(dataSource);

        dao = mock(CaIntegrator2Dao.class);
        when(dao.getStudies(anyString())).thenReturn(Arrays.asList(study));
        when(dao.getAllGenomicDataSources()).thenReturn(Arrays.asList(dataSource));
    }

    /**
     * Sets up the caArray facade mock objects.
     */
    private void setUpCaArrayFacade() throws Exception {
        caArrayFacade = mock(CaArrayFacade.class);
        Sample sample = new Sample();
        sample.setName("testSample");
        when(caArrayFacade.getSamples(anyString(), any(ServerConnectionProfile.class))).thenReturn(Arrays.asList(sample));
        when(caArrayFacade.retrieveFile(any(GenomicDataSourceConfiguration.class), anyString())).thenReturn(ArrayUtils.EMPTY_BYTE_ARRAY);
        when(caArrayFacade.retrieveFilesForGenomicSource(any(GenomicDataSourceConfiguration.class))).thenReturn(Collections.<gov.nih.nci.caarray.external.v1_0.data.File>emptyList());
        when(caArrayFacade.getLastDataModificationDate(any(GenomicDataSourceConfiguration.class))).thenReturn(new Date());

        Map<String, Date> updateMap = new HashMap<String, Date>();
        updateMap.put("test1", new Date());
        updateMap.put("test2", new Date());
        updateMap.put("test3", new Date());
        when(caArrayFacade.checkForSampleUpdates(anyString(), any(ServerConnectionProfile.class))).thenReturn(updateMap);
        setUpCaArrayFacadeRetrieveData();
        setUpCaArrayFacadeRetrieveDnaAnalysisData();
    }

    /**
     * Sets up the Array Data Service mock object
     */
    private void setUpArrayDataService() throws Exception {
        arrayDataService = mock(ArrayDataService.class);
        when(arrayDataService.getData(any(DataRetrievalRequest.class))).thenAnswer(new Answer<ArrayDataValues>() {
            @Override
            public ArrayDataValues answer(InvocationOnMock invocation) throws Throwable {
                DataRetrievalRequest request = (DataRetrievalRequest) invocation.getArguments()[0];
                return generateArrayDataValues(request);
            }
        });
        when(arrayDataService.loadArrayDesign(any(PlatformConfiguration.class))).thenReturn(new PlatformConfiguration());
        when(arrayDataService.getFoldChangeValues(any(DataRetrievalRequest.class), anyListOf(ArrayDataValues.class),
                any(PlatformChannelTypeEnum.class))).thenAnswer(new Answer<ArrayDataValues>() {
                    @Override
                    public ArrayDataValues answer(InvocationOnMock invocation) throws Throwable {
                        DataRetrievalRequest request = (DataRetrievalRequest) invocation.getArguments()[0];
                        return generateArrayDataValues(request);
                    }
                });
        when(arrayDataService.getFoldChangeValues(any(DataRetrievalRequest.class), any(Query.class)))
            .thenAnswer(new Answer<ArrayDataValues>() {
                @Override
            public ArrayDataValues answer(InvocationOnMock invocation) throws Throwable {
                DataRetrievalRequest request = (DataRetrievalRequest) invocation.getArguments()[0];
                return generateArrayDataValues(request);
            }
        });

        List<Platform> platforms = new ArrayList<Platform>();
        Platform platform = new Platform();
        platform.setVendor(PlatformVendorEnum.AFFYMETRIX);
        platforms.add(platform);
        platform = new Platform();
        platform.setVendor(PlatformVendorEnum.AGILENT);
        platforms.add(platform);

        when(arrayDataService.getPlatforms()).thenReturn(platforms);

        List<PlatformConfiguration> platformConfigurations = new ArrayList<PlatformConfiguration>();
        PlatformConfiguration config1 = new PlatformConfiguration();
        config1.setId(1l);
        config1.setName("name");
        config1.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        config1.setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);
        config1.setStatus(Status.PROCESSING);
        config1.setDeploymentStartDate(new Date());
        PlatformConfiguration config2 = new PlatformConfiguration();
        config2.setId(1l);
        config2.setName("name2");
        config2.setStatus(Status.LOADED);
        config2.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        config2.setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);
        config2.setDeploymentStartDate(new Date());
        Platform affPlatform = new Platform();
        affPlatform.setName("name2");
        affPlatform.setVendor(PlatformVendorEnum.AFFYMETRIX);
        config2.setPlatform(affPlatform);
        platformConfigurations.add(config1);
        platformConfigurations.add(config2);
        when(arrayDataService.getPlatformConfigurations()).thenReturn(platformConfigurations);

        PlatformConfiguration config = new PlatformConfiguration();
        config.setId(1l);
        config.setName("name");
        config.setPlatformType(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION);
        config.setPlatformChannelType(PlatformChannelTypeEnum.ONE_COLOR);
        config.setStatus(Status.PROCESSING);
        config.setDeploymentStartDate(new Date());
        when(arrayDataService.getRefreshedPlatformConfiguration(anyLong())).thenReturn(config);

        when(arrayDataService.getPlatformsInStudy(any(Study.class), any(PlatformDataTypeEnum.class))).thenReturn(createPlatforms());
        when(arrayDataService.loadGeneLocationFile(any(File.class), any(GenomeBuildVersionEnum.class))).thenReturn(new GeneLocationConfiguration());
        when(arrayDataService.getPlatformsWithCghCallInStudy(any(Study.class), any(PlatformDataTypeEnum.class))).thenReturn(createPlatforms());
        when(arrayDataService.getPlatform(anyString())).thenAnswer(new Answer<Platform>() {
            @Override
            public Platform answer(InvocationOnMock invocation) throws Throwable {
                String platformName = (String) invocation.getArguments()[0];
                Platform platform = new Platform();
                platform.setName(platformName);
                ReporterList reporterList = new ReporterList("reporterName", ReporterTypeEnum.GENE_EXPRESSION_GENE);
                reporterList.setGenomeVersion(platformName);
                reporterList.setPlatform(platform);
                platform.addReporterList(reporterList);
                return platform;
            }
        });
    }

    private void setUpAnalysisService() throws Exception {
        analysisService = mock(AnalysisService.class);

        List<AnalysisMethod> methods = new ArrayList<AnalysisMethod>();
        AnalysisMethod method = new AnalysisMethod();
        method.setName("method");
        methods.add(method);
        AnalysisParameter parameter = new AnalysisParameter();
        parameter.setName("parameter");
        parameter.setType(AnalysisParameterType.STRING);
        StringParameterValue defaultValue = new StringParameterValue();
        defaultValue.setParameter(parameter);
        defaultValue.setValue("default");
        parameter.setDefaultValue(defaultValue);
        parameter.setRequired(true);
        method.getParameters().add(parameter);

        when(analysisService.getGenePatternMethods(any(ServerConnectionProfile.class))).thenReturn(methods);

        JobInfoWrapper jobInfo = new JobInfoWrapper();
        jobInfo.setUrl(new URL("http://localhost/resultUrl"));
        jobInfo.setJobInfo(new JobInfo());
        when(analysisService.executeGenePatternJob(any(ServerConnectionProfile.class), any(AnalysisMethodInvocation.class))).thenReturn(jobInfo);
        when(analysisService.createKMPlot(any(StudySubscription.class), any(AbstractKMParameters.class))).thenReturn(new KMPlotImpl());
    }

    private void setUpDeploymentService() {
        deploymentService = mock(DeploymentService.class);
        when(deploymentService.performDeployment(any(StudyConfiguration.class), any(HeatmapParameters.class))).thenAnswer(new Answer<StudyConfiguration>() {
            @Override
            public StudyConfiguration answer(InvocationOnMock invocation) throws Throwable {
                StudyConfiguration studyConfig = (StudyConfiguration) invocation.getArguments()[0];
                studyConfig.setStatus(Status.PROCESSING);
                return studyConfig;
            }
        });
        doAnswer(new Answer<StudyConfiguration>() {
            @Override
            public StudyConfiguration answer(InvocationOnMock invocation) throws Throwable {
                StudyConfiguration studyConfig = (StudyConfiguration) invocation.getArguments()[0];
                studyConfig.setStatus(Status.DEPLOYED);
                return null;
            }
        }).when(deploymentService).prepareForDeployment(any(StudyConfiguration.class));
    }

    private void setUpConfigurationHelper() {
        configurationHelper = mock(ConfigurationHelper.class);
        when(configurationHelper.getString(any(ConfigurationParameter.class))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                ConfigurationParameter cp = (ConfigurationParameter) invocation.getArguments()[0];
                return cp.getDefaultValue();
            }
        });
    }

    private void setUpWorkspaceService() throws Exception {
        workspaceService = mock(WorkspaceService.class);
        when(workspaceService.getWorkspace()).thenAnswer(new Answer<UserWorkspace>() {
            @Override
            public UserWorkspace answer(InvocationOnMock invocation) throws Throwable {
                UserWorkspace workspace = new UserWorkspace();
                workspace.setDefaultSubscription(getStudySubscription());
                workspace.getSubscriptionCollection().add(workspace.getDefaultSubscription());
                if (studySubscription != null) {
                    workspace.getSubscriptionCollection().add(studySubscription);
                }
                workspace.setUsername("username");
                return workspace;
            }
        });
        when(workspaceService.retrieveStudyConfigurationJobs(any(UserWorkspace.class))).thenAnswer(new Answer<Set<StudyConfiguration>>() {
            @Override
            public Set<StudyConfiguration> answer(InvocationOnMock invocation) throws Throwable {
                Set<StudyConfiguration> results = new HashSet<StudyConfiguration>();
                StudyConfiguration config = new StudyConfiguration();
                config.setStatus(Status.PROCESSING);
                Date today = new Date();
                config.setDeploymentStartDate(DateUtils.addHours(today, -13));
                results.add(config);

                config = new StudyConfiguration();
                config.setStatus(Status.PROCESSING);
                config.setDeploymentStartDate(today);
                results.add(config);
                return results;
            }
        });
        when(workspaceService.createDisplayableStudySummary(any(Study.class))).thenAnswer(new Answer<DisplayableStudySummary>() {
            @Override
            public DisplayableStudySummary answer(InvocationOnMock invocation) throws Throwable {
                Study study = (Study) invocation.getArguments()[0];
                return new DisplayableStudySummary(study);
            }
        });
        when(workspaceService.getRefreshedEntity(any(AbstractCaIntegrator2Object.class))).thenAnswer(new Answer<AbstractCaIntegrator2Object>() {
            @Override
            public AbstractCaIntegrator2Object answer(InvocationOnMock invocation) throws Throwable {
                AbstractCaIntegrator2Object obj = (AbstractCaIntegrator2Object) invocation.getArguments()[0];
                return obj;
            }
        });
        when(workspaceService.getWorkspaceReadOnly()).thenAnswer(new Answer<UserWorkspace>() {
            @Override
            public UserWorkspace answer(InvocationOnMock invocation) throws Throwable {
                UserWorkspace workspace = new UserWorkspace();
                workspace.setDefaultSubscription(getStudySubscription());
                workspace.getSubscriptionCollection().add(workspace.getDefaultSubscription());
                workspace.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
                return workspace;
            }
        });
        when(workspaceService.retrievePlatformsInStudy(any(Study.class))).thenReturn(new HashSet<Platform>());
    }

    private void setUpNCIAFacade() throws Exception {
        nciaFacade = mock(NCIAFacade.class);

        final File validFile = FileUtils.toFile(this.getClass().getResource(TestDataFiles.VALID_FILE_RESOURCE_PATH));

        when(nciaFacade.getAllCollectionNameProjects(any(ServerConnectionProfile.class))).thenReturn(new ArrayList<String>());
        when(nciaFacade.getImageSeriesAcquisitions(anyString(), any(ServerConnectionProfile.class))).thenReturn(Arrays.asList(new ImageSeriesAcquisition()));
        when(nciaFacade.retrieveDicomFiles(any(NCIADicomJob.class))).thenReturn(validFile);
    }

    private void setUpAnalysisFileManager() throws Exception {
        analysisFileManager = mock(AnalysisFileManager.class);
        when(analysisFileManager.getIGVDirectory(anyString())).thenReturn(getTmpFile());
        when(analysisFileManager.createIGVGctFile(any(GctDataset.class), anyString())).thenReturn(getTmpFile());
        when(analysisFileManager.createIGVSegFile(anyCollectionOf(SegmentData.class), anyString(),
                anyBoolean())).thenReturn(getTmpFile());
        when(analysisFileManager.createIGVSampleClassificationFile(any(QueryResult.class), anyString(),
                anyCollectionOf(ResultColumn.class), any(CopyNumberCriterionTypeEnum.class))).thenReturn(getTmpFile());
        when(analysisFileManager.createIGVGctFile(any(GctDataset.class), any(Study.class), anyString())).thenReturn(getTmpFile());
        when(analysisFileManager.createIGVSegFile(anyCollectionOf(SegmentData.class), any(Study.class), anyString(),
                anyBoolean())).thenReturn(getTmpFile());
        when(analysisFileManager.retrieveIGVFile(any(Study.class), any(IGVFileTypeEnum.class), anyString())).thenReturn(getTmpFile());
        when(analysisFileManager.createHeatmapSampleClassificationFile(any(QueryResult.class), anyString(),
                anyCollectionOf(ResultColumn.class))).thenReturn(getTmpFile());
        when(analysisFileManager.retrieveHeatmapFile(any(Study.class), any(HeatmapFileTypeEnum.class), anyString())).thenReturn(getTmpFile());
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                HeatmapResult result = (HeatmapResult) invocation.getArguments()[1];
                result.setGenomicDataFile(new File("Dummy"));
                return null;
            }
        }).when(analysisFileManager).createHeatmapGenomicFile(any(HeatmapParameters.class), any(HeatmapResult.class),
                anyCollectionOf(SegmentData.class), any(GeneLocationConfiguration.class), any(CBSToHeatmapFactory.class));
    }

    private void setUpFileManager() throws Exception {
        fileManager = mock(FileManager.class);
        when(fileManager.storeStudyFile(any(File.class), anyString(), any(StudyConfiguration.class))).thenAnswer(new Answer<File>() {
            @Override
            public File answer(InvocationOnMock invocation) throws Throwable {
                return (File) invocation.getArguments()[0];
            }
        });
        when(fileManager.getStudyDirectory(any(Study.class))).thenReturn(new File(System.getProperty("java.io.tmpdir")));
        when(fileManager.getNewTemporaryDirectory(anyString())).thenReturn(new File(System.getProperty("java.io.tmpdir")));
        when(fileManager.getUserDirectory(any(StudySubscription.class))).thenReturn(new File(System.getProperty("java.io.tmpdir")));
        when(fileManager.createNewStudySubscriptionFile(any(StudySubscription.class), anyString())).thenReturn(getTmpFile());
        when(fileManager.createClassificationFile(any(StudySubscription.class),
                any(SampleClassificationParameterValue.class), anyString())).thenReturn(getTmpFile());
        when(fileManager.renameCnvFile(any(File.class))).thenAnswer(new Answer<File>() {
            @Override
            public File answer(InvocationOnMock invocation) throws Throwable {
                return (File) invocation.getArguments()[0];
            }
        });
        when(fileManager.createGctFile(any(StudySubscription.class), any(GctDataset.class), anyString())).thenReturn(getTmpFile());
        when(fileManager.createMarkersFile(any(StudySubscription.class), any(Marker[].class))).thenReturn(getTmpFile());
        when(fileManager.createSamplesFile(any(StudySubscription.class), any(SampleWithChromosomalSegmentSet[].class))).thenReturn(getTmpFile());
        when(fileManager.getTempDirectory()).thenReturn(getTmpFile().getAbsolutePath());
        when(fileManager.createInputZipFile(any(StudySubscription.class), any(AbstractPersistedAnalysisJob.class),
                anyString(), any(File[].class))).thenAnswer(new Answer<ResultsZipFile>() {
            @Override
            public ResultsZipFile answer(InvocationOnMock invocation) throws Throwable {
                ResultsZipFile zipFile = new ResultsZipFile();
                zipFile.setPath(getTmpFile().getAbsolutePath());
                return zipFile;
            }
        });
    }

    private final File getTmpFile() {
        File tmpFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "tmpFile");
        tmpFile.deleteOnExit();
        return tmpFile;
    }


    private void setUpQueryManagementService() throws Exception {
        queryManagementService = mock(QueryManagementService.class);
        doAnswer(new Answer<Query>() {
            @Override
            public Query answer(InvocationOnMock invocation) throws Throwable {
                Query query = (Query) invocation.getArguments()[0];
                query.setId(1L);
                return query;
            }
        }).when(queryManagementService).save(any(Query.class));
        when(queryManagementService.execute(any(Query.class))).then(new Answer<QueryResult>() {
            @Override
            public QueryResult answer(InvocationOnMock invocation) throws Throwable {
                Query query = (Query) invocation.getArguments()[0];
                QueryResult qr = new QueryResult();
                qr.setQuery(query);
                return qr;
            }
        });
        when(queryManagementService.executeGenomicDataQuery(any(Query.class))).thenReturn(new GenomicDataQueryResult());
        when(queryManagementService.retrieveSegmentDataQuery(any(Query.class))).thenReturn(new ArrayList<SegmentData>());
        when(queryManagementService.createDicomJob(anyListOf(DisplayableResultRow.class))).thenReturn(new NCIADicomJob());
        when(queryManagementService.createNciaBasket(anyListOf(DisplayableResultRow.class))).thenReturn(new NCIABasket());
        when(queryManagementService.createCsvFileFromGenomicResults(any(GenomicDataQueryResult.class))).thenReturn(getTmpFile());
        when(queryManagementService.createQueryFromSubjectList(any(StudySubscription.class), any(SubjectList.class))).then(new Answer<Query>() {
            @Override
            public Query answer(InvocationOnMock invocation) throws Throwable {
                SubjectList sl = (SubjectList) invocation.getArguments()[1];
                Query query = new Query();
                query.setName(sl.getName());
                query.setSubjectListQuery(true);
                query.setSubjectListVisibility(sl.getVisibility());
                return query;
            }
        });
        when(queryManagementService.createQueriesFromSubjectLists(any(StudySubscription.class))).then(new Answer<List<Query>>() {
            @Override
            public List<Query> answer(InvocationOnMock invocation) throws Throwable {
                StudySubscription subscription = (StudySubscription) invocation.getArguments()[0];
                List<Query> results = new ArrayList<Query>();
                for (SubjectList subjectList : subscription.getStudy().getStudyConfiguration().getSubjectLists()) {
                    results.add(queryManagementService.createQueryFromSubjectList(subscription, subjectList));
                }
                for (SubjectList subjectList : subscription.getSubjectLists()) {
                    results.add(queryManagementService.createQueryFromSubjectList(subscription, subjectList));
                }
                return results;
            }
        });
        when(queryManagementService.getRefreshedEntity(any(AbstractCaIntegrator2Object.class))).then(new Answer<AbstractCaIntegrator2Object>() {
            @Override
            public AbstractCaIntegrator2Object answer(InvocationOnMock invocation) throws Throwable {
                AbstractCaIntegrator2Object obj = (AbstractCaIntegrator2Object) invocation.getArguments()[0];
                return obj;
            }
        });
        when(queryManagementService.validateGeneSymbols(any(StudySubscription.class), anyListOf(String.class))).thenReturn(new ArrayList<String>());
        when(queryManagementService.retrieveCopyNumberPlatformsForStudy(any(Study.class))).thenReturn(new HashSet<String>());
        when(queryManagementService.getAllSubjectsNotFoundInCriteria(any(Query.class))).thenReturn(new HashSet<String>());
        when(queryManagementService.retrieveCopyNumberPlatformsWithCghCallForStudy(any(Study.class))).thenReturn(new HashSet<String>());
        when(queryManagementService.retrieveQueryToExecute(any(Query.class))).then(new Answer<Query>() {
            @Override
            public Query answer(InvocationOnMock invocation) throws Throwable {
                return (Query) invocation.getArguments()[0];
            }
        });
    }

    private void setUpKmPlotService() {
        kmPlotService = mock(KMPlotService.class);
        when(kmPlotService.computeLogRankPValueBetween(any(KMSampleGroupCriteriaDTO.class), any(KMSampleGroupCriteriaDTO.class))).thenReturn(1.1);
    }

    private void setUpCaArrayFacadeRetrieveData() throws Exception {
        when(caArrayFacade.retrieveData(any(GenomicDataSourceConfiguration.class))).thenAnswer(new Answer<ArrayDataValues>() {
            @Override
            public ArrayDataValues answer(InvocationOnMock invocation) throws Throwable {
                GenomicDataSourceConfiguration dataSource = (GenomicDataSourceConfiguration) invocation.getArguments()[0];
                List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
                GeneExpressionReporter reporter = new GeneExpressionReporter();
                reporters.add(reporter);
                ArrayDataValues values = new ArrayDataValues(reporters);
                Platform platform = new Platform();
                ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
                reporter.setReporterList(reporterList);
                reporterList.getReporters().addAll(reporters);
                platform.addReporterList("reporterList2", ReporterTypeEnum.GENE_EXPRESSION_GENE);
                for (Sample sample : dataSource.getSamples()) {
                    addExpressionArrayData(sample, platform, reporterList, values);
                }
                return values;
            }
        });
    }

    private void setUpCaArrayFacadeRetrieveDnaAnalysisData() throws Exception {
        when(caArrayFacade.retrieveDnaAnalysisData(any(GenomicDataSourceConfiguration.class), any(ArrayDataService.class)))
        .thenAnswer(new Answer<List<ArrayDataValues>>() {
            @Override
            public List<ArrayDataValues> answer(InvocationOnMock invocation) throws Throwable {
                GenomicDataSourceConfiguration dataSource = (GenomicDataSourceConfiguration) invocation.getArguments()[0];
                List<ArrayDataValues> valuesList = new ArrayList<ArrayDataValues>();
                List<AbstractReporter> reporters = new ArrayList<AbstractReporter>();
                GeneExpressionReporter reporter = new GeneExpressionReporter();
                reporters.add(reporter);
                ArrayDataValues values = new ArrayDataValues(reporters);
                Platform platform = new Platform();
                ReporterList reporterList = platform.addReporterList("reporterList", ReporterTypeEnum.DNA_ANALYSIS_REPORTER);
                reporter.setReporterList(reporterList);
                reporterList.getReporters().addAll(reporters);
                for (Sample sample : dataSource.getSamples()) {
                    addDnaAnalysisArrayData(sample, platform, reporterList, values);
                }
                valuesList.add(values);
                return valuesList;
            }
        });
    }

    private void addExpressionArrayData(Sample sample, Platform platform, ReporterList reporterList, ArrayDataValues values) {
        Array array = new Array();
        array.setPlatform(platform);
        array.setName(sample.getName());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.GENE_EXPRESSION);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.getReporterLists().add(reporterList);
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        values.setFloatValues(arrayData, reporterList.getReporters(), ArrayDataValueType.EXPRESSION_SIGNAL,
                new float[reporterList.getReporters().size()]);
    }

    private void addDnaAnalysisArrayData(Sample sample, Platform platform, ReporterList reporterList, ArrayDataValues values) {
        Array array = new Array();
        array.setPlatform(platform);
        array.setName(sample.getName());
        array.getSampleCollection().add(sample);
        ArrayData arrayData = new ArrayData();
        arrayData.setType(ArrayDataType.COPY_NUMBER);
        arrayData.setArray(array);
        array.getArrayDataCollection().add(arrayData);
        arrayData.setSample(sample);
        arrayData.getReporterLists().add(reporterList);
        sample.getArrayCollection().add(array);
        sample.getArrayDataCollection().add(arrayData);
        values.setFloatValues(arrayData, reporterList.getReporters(), ArrayDataValueType.DNA_ANALYSIS_LOG2_RATIO,
                new float[reporterList.getReporters().size()]);
    }

    private ArrayDataValues generateArrayDataValues(DataRetrievalRequest request) {
        ArrayDataValues values = new ArrayDataValues(request.getReporters());
        for (AbstractReporter reporter : request.getReporters()) {
            for (ArrayData arrayData : request.getArrayDatas()) {
                for (ArrayDataValueType type : request.getTypes()) {
                    values.setFloatValue(arrayData, reporter, type, (float) 1.23);
                }
            }
        }
        return values;
    }

    private List<Platform> createPlatforms() {
        Platform platform = new Platform();
        platform.setName("0");
        return Arrays.asList(platform);
    }

    /**
     * @return the studySubscription
     */
    protected StudySubscription getStudySubscription() {
        if (studySubscription == null) {
            studySubscription = new StudySubscription();
            studySubscription.setId(Long.valueOf(1));
            studySubscription.setStudy(new Study());
            studySubscription.getStudy().setShortTitleText("Study Name");
            studySubscription.getStudy().setStudyConfiguration(new StudyConfiguration());
        }
        return studySubscription;
    }

    /**
     * @param studySubscription the studySubscription to set
     */
    protected void setStudySubscription(StudySubscription studySubscription) {
        this.studySubscription = studySubscription;
    }
}
