/**
 * Copyright (c) 2012, 5AM Solutions, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of the author nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.application.analysis.AbstractKMParameters;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethod;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisMethodInvocation;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameter;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisParameterType;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.analysis.JobInfoWrapper;
import gov.nih.nci.caintegrator2.application.analysis.StringParameterValue;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformChannelTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlotImpl;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.Status;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayDataType;
import gov.nih.nci.caintegrator2.domain.genomic.GeneExpressionReporter;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;

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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.xwork.time.DateUtils;
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
    }

    /**
     * Sets up the caIntegrator dao mock objects.
     */
    protected void setUpDao() throws Exception {
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
    protected void setUpCaArrayFacade() throws Exception {
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
    protected void setUpArrayDataService() throws Exception {
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

    protected void setUpAnalysisService() throws Exception {
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

    protected void setUpDeploymentService() {
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

    protected void setUpConfigurationHelper() {
        configurationHelper = mock(ConfigurationHelper.class);
        when(configurationHelper.getString(any(ConfigurationParameter.class))).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                ConfigurationParameter cp = (ConfigurationParameter) invocation.getArguments()[0];
                return cp.getDefaultValue();
            }
        });
    }

    protected void setUpWorkspaceService() throws Exception {
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
