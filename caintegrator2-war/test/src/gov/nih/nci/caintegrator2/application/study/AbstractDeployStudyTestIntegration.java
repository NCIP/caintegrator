/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixSnpPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.query.QueryManagementService;
import gov.nih.nci.caintegrator2.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator2.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.QueryResult;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.DataRetrievalException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator2.security.AuthorizationManagerFactory;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.exceptions.CSException;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/**/service-test-integration-config.xml"})
@TransactionConfiguration(defaultRollback = false)
public abstract class AbstractDeployStudyTestIntegration extends AbstractMockitoTest {
    private static final String USER_NCIMANAGER = "ncimanager";
    private static final String APPLICATION_CONTEXT_NAME = "caintegrator2";
    private final Logger logger = Logger.getLogger(getClass());
    private long startTime;

    @Autowired
    private StudyManagementService service;
    @Autowired
    private DeploymentService deploymentService;
    @Autowired
    private QueryManagementService queryManagementService;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private CaIntegrator2Dao dao;
    @Autowired
    private ArrayDataService arrayDataService;
    @Autowired
    private AuthorizationManagerFactory authorizationManagerFactory;
    @Autowired
    private FileManager fileManager;

    private StudyConfiguration studyConfiguration;
    private DelimitedTextClinicalSourceConfiguration sourceConfiguration;
    private Platform design;
    private boolean isPublicSubscription = Boolean.FALSE;

    /**
     * @param caIntegrator2Dao the caIntegrator2Dao to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.service = studyManagementService;
    }

    public void deployStudy() throws Exception {
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
        authentication.setUsername(USER_NCIMANAGER);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        loadDesigns();
        UserWorkspace userWorkspace = workspaceService.getWorkspace();
        userWorkspace.setUsername(USER_NCIMANAGER);
        studyConfiguration = new StudyConfiguration();
        studyConfiguration.getStudy().setShortTitleText(getStudyName());
        studyConfiguration.getStudy().setLongTitleText(getDescription());
        studyConfiguration.getStudy().setEnabled(Boolean.TRUE);
        studyConfiguration.setUserWorkspace(userWorkspace);
        studyConfiguration.getStudy().setStudyConfiguration(studyConfiguration);
        service.save(studyConfiguration);
        workspaceService.saveUserWorkspace(userWorkspace);
        service.createProtectionElement(studyConfiguration);
        clearStudyDirectory(studyConfiguration.getStudy());
        loadAnnotationGroup();
        loadClinicalData();
        loadSurvivalValueDefinition();
        loadSamples();
        mapSamples();
        loadControlSamples(0);
        loadCopyNumberMappingFile();
        ImageDataSourceConfiguration imageSource = loadImages();
        mapImages(imageSource);
        loadImageAnnotation(imageSource);
        deploy(userWorkspace);
        workspaceService.subscribe(userWorkspace, studyConfiguration.getStudy(), isPublicSubscription());
        authorizeStudyElements();
        checkArrayData();
        checkQueries();
    }

    private void loadCopyNumberMappingFile() throws ConnectionException, ExperimentNotFoundException, IOException, ValidationException {
        if (getCopyNumberFile() != null) {
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.getServerProfile().setHostname(getCopyNumberCaArrayHostname());
            genomicSource.getServerProfile().setPort(getCopyNumberCaArrayPort());
            genomicSource.getServerProfile().setUsername(getCaArrayUsername());
            genomicSource.getServerProfile().setPassword(getCaArrayPassword());
            genomicSource.setExperimentIdentifier(getCopyNumberCaArrayId());
            genomicSource.setPlatformVendor(getPlatformVendor());
            genomicSource.setPlatformName(getCopyNumberPlatformName());
            genomicSource.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
            genomicSource.setLoadingType(ArrayDataLoadingTypeEnum.CNCHP);
            genomicSource.setLoadingType(getCopyNumberLoadingType());
            service.addGenomicSource(studyConfiguration, genomicSource);
            getService().saveDnaAnalysisMappingFile(genomicSource, getCopyNumberFile(), getCopyNumberFile().getName());
            configureSegmentationDataCalcuation(genomicSource.getDnaAnalysisDataConfiguration());
        }
    }

    protected String getCopyNumberPlatformName() {
        return null;
    }

    protected ArrayDataLoadingTypeEnum getLoadingType() {
        return ArrayDataLoadingTypeEnum.PARSED_DATA;
    }

    protected ArrayDataLoadingTypeEnum getCopyNumberLoadingType() {
        return ArrayDataLoadingTypeEnum.SINGLE_SAMPLE_PER_FILE;
    }

    protected int getCopyNumberCaArrayPort() {
        return 31099;
    }

    protected void configureSegmentationDataCalcuation(DnaAnalysisDataConfiguration dnaAnalysisDataConfiguration) {
        // no-op
    }

    protected String getCopyNumberCaArrayId() {
        return null;
    }

    protected File getCopyNumberFile() {
        return null;
    }

    private void clearStudyDirectory(Study study) throws IOException {
        File studyDirectory = fileManager.getStudyDirectory(study);
        if (studyDirectory.exists()) {
            FileUtils.cleanDirectory(studyDirectory);
        }
    }

    abstract protected String getStudyName();

    protected String getDescription() {
        return getStudyName() + " demo study";
    }

    private void loadDesigns() throws PlatformLoadingException {
        if (getLoadDesign()) {
            logStart();
            design = getOrLoadDesign(getPlatformSource());
            for (AbstractPlatformSource platformSource : getAdditionalPlatformSources()) {
                getOrLoadDesign(platformSource);
            }
            logEnd();
        }
    }

    protected AbstractPlatformSource[] getAdditionalPlatformSources() {
        return new AbstractPlatformSource[0];
    }

    private Platform getOrLoadDesign(AbstractPlatformSource platformSource) throws PlatformLoadingException {
        Platform platform = getExistingDesign(platformSource);
        if (platform == null) {
            PlatformConfiguration configuration = new PlatformConfiguration(platformSource);
            configuration.setName(platformSource.getLoader().getPlatformName());
            configuration.setPlatformType(platformSource.getPlatformType());
            configuration.setPlatformChannelType(platformSource.getPlatformChannelType());
            arrayDataService.savePlatformConfiguration(configuration);
            platform = arrayDataService.loadArrayDesign(configuration).getPlatform();
            platform.setPlatformConfiguration(configuration);
        }
        return platform;
    }

    private void logStart() {
        startTime = System.currentTimeMillis();
        logger.info("start " + getMethodName() + "()");
    }

    private void logEnd() {
        long duration = System.currentTimeMillis() - startTime;
        logger.info("end " + getMethodName() + "(), duration: " + duration + " ms");
    }

    private String getMethodName() {
        Exception e = new Exception();
        e.fillInStackTrace();
        return e.getStackTrace()[2].getMethodName();
    }

    private Platform getExistingDesign(AbstractPlatformSource platformSource) {
        return dao.getPlatform(getPlatformName(platformSource));
    }

    private String getPlatformName(AbstractPlatformSource platformSource) {
        if (platformSource instanceof AgilentExpressionPlatformSource) {
            return ((AgilentExpressionPlatformSource) platformSource).getPlatformName();
        } else if (platformSource instanceof AffymetrixExpressionPlatformSource) {
            return platformSource.getAnnotationFiles().get(0).getName().split("\\.")[0];
        } else if (platformSource instanceof AffymetrixSnpPlatformSource) {
            return ((AffymetrixSnpPlatformSource) platformSource).getPlatformName();
        } else {
            throw new IllegalArgumentException("Unknonw platform source type: " + platformSource.getClass().getName());
        }
    }

    protected AbstractPlatformSource getPlatformSource() {
        return null;
    }

    protected boolean getLoadDesign() {
        return false;
    }

    protected String getNCIAServerUrl() {
        return null;
    }

    private ImageDataSourceConfiguration loadImages() throws ConnectionException, InvalidImagingCollectionException {
        if (getLoadImages()) {
            logStart();
            ImageDataSourceConfiguration imageSource = new ImageDataSourceConfiguration();
            imageSource.getServerProfile().setUrl(getNCIAServerUrl());
            imageSource.getServerProfile().setHostname(Cai2Util.getHostNameFromUrl(getNCIAServerUrl()));
            imageSource.setCollectionName(getNCIATrialId());
            imageSource.setMappingFileName(getImageMappingFile().getName());
            service.addImageSource(studyConfiguration, imageSource);
            logEnd();
            return imageSource;
        }
        return null;
    }

    protected boolean getLoadImages() {
        return false;
    }

    protected String getNCIATrialId() {
        return null;
    }

    private void loadImageAnnotation(ImageDataSourceConfiguration imageSource) throws ValidationException, IOException {
        if (getLoadImageAnnotation()) {
            logStart();
            ImageAnnotationConfiguration imageAnnotationConfiguration =
                service.addImageAnnotationFile(imageSource, getImageAnnotationFile(),
                        getImageAnnotationFile().getName(), true);
            imageSource.setImageAnnotationConfiguration(imageAnnotationConfiguration);
            imageAnnotationConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
            for (ImageDataSourceConfiguration configuration : studyConfiguration.getImageDataSources()) {
                service.loadImageAnnotation(configuration);
            }
            logEnd();
        }
    }

    protected boolean getLoadImageAnnotation() {
        return false;
    }

    protected File getImageAnnotationFile() {
        return null;
    }

    private void mapImages(ImageDataSourceConfiguration imageSource) throws ValidationException, IOException {
        if (getMapImages()) {
            logStart();
            service.mapImageSeriesAcquisitions(studyConfiguration, imageSource,
                    getImageMappingFile(), ImageDataSourceMappingTypeEnum.IMAGE_SERIES);
            logEnd();
        }
    }

    protected boolean getMapImages() {
        return false;
    }

    protected File getImageMappingFile() {
        return null;
    }

    protected boolean getAuthorizeStudy() {
        return false;
    }

    private void checkArrayData() {
        if (getLoadSamples()) {
            logStart();
            PlatformHelper platformHelper = new PlatformHelper(design);
            Set<ReporterList> probeSetReporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            for (ReporterList probeSetReporterList : probeSetReporterLists) {
                DataRetrievalRequest probeSetRequest = new DataRetrievalRequest();
                probeSetRequest.addArrayDatas(getStudyArrayDatas(probeSetReporterList.getArrayDatas()));
                probeSetRequest.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
                probeSetRequest.addReporters(probeSetReporterList.getReporters());
                DataRetrievalRequest geneRequest = new DataRetrievalRequest();
                Set<ReporterList> geneReporterLists = platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_GENE);
                for (ReporterList geneReporterList : geneReporterLists) {
                    geneRequest.addArrayDatas(getStudyArrayDatas(geneReporterList.getArrayDatas()));
                    geneRequest.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
                    geneRequest.addReporters(geneReporterList.getReporters());
                    ArrayDataValues values = arrayDataService.getData(probeSetRequest);
                    assertEquals(getExpectedSampleCount(), values.getArrayDatas().size());
                    assertEquals(getExpectedNumberProbeSets(), values.getReporters().size());
                    values = arrayDataService.getData(geneRequest);
                    assertEquals(getExpectedSampleCount(), values.getArrayDatas().size());
                    assertEquals(getExpectedNumberOfGeneReporters(), values.getReporters().size());
                }
            }
            logEnd();
        }
    }

    private Set<ArrayData> getStudyArrayDatas(Set<ArrayData> arrayDatas) {
        Set<ArrayData> studyArrayDatas = new HashSet<ArrayData>();
        for (ArrayData arrayData : arrayDatas) {
            if (studyConfiguration.getStudy().equals(arrayData.getStudy())) {
                studyArrayDatas.add(arrayData);
            }
        }
        return studyArrayDatas;
    }

    protected int getExpectedNumberOfGeneReporters() {
        return 0;
    }

    protected int getExpectedNumberProbeSets() {
        return 0;
    }

    protected boolean getLoadSamples() {
        return false;
    }

    protected int getExpectedSampleCount() {
        return 0;
    }

    protected int getExpectedMappedSampleCount() {
        return 0;
    }

    protected int getExpectedControlSampleCount() {
        return 0;
    }

    private void loadSamples() throws ConnectionException, ExperimentNotFoundException {
        if (getLoadSamples()) {
            logStart();
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.getServerProfile().setHostname(getCaArrayHostname());
            genomicSource.getServerProfile().setPort(getCaArrayPort());
            genomicSource.getServerProfile().setUsername(getCaArrayUsername());
            genomicSource.getServerProfile().setPassword(getCaArrayPassword());
            genomicSource.setExperimentIdentifier(getCaArrayId());
            genomicSource.setPlatformName(getPlatformName());
            genomicSource.setPlatformVendor(getPlatformVendor());
            genomicSource.setDataType(PlatformDataTypeEnum.EXPRESSION);
            genomicSource.setLoadingType(getLoadingType());
            if (getSampleMappingFile() != null) {
                genomicSource.setSampleMappingFileName(getSampleMappingFile().getName());
            }
            service.addGenomicSource(studyConfiguration, genomicSource);
            assertTrue(genomicSource.getSamples().size() > 0);
            assertTrue(studyConfiguration.getGenomicDataSources().get(0).getExperimentIdentifier().equalsIgnoreCase(getCaArrayId()));
            logEnd();
        }
    }

    protected int getCaArrayPort() {
        return 31099;
    }

    protected String getCopyNumberCaArrayHostname() {
        return "ncias-d227-v.nci.nih.gov";
    }

    protected String getCaArrayHostname() {
        return "ncias-d227-v.nci.nih.gov";
    }

    protected String getCaArrayId() {
        return null;
    }

    private void mapSamples() throws ValidationException, IOException {
        if (getLoadSamples()) {
            logStart();
            service.mapSamples(studyConfiguration, getSampleMappingFile(), studyConfiguration.getGenomicDataSources().get(0));
            assertEquals(getExpectedMappedSampleCount(), studyConfiguration.getGenomicDataSources().get(0).getMappedSamples().size());
            logEnd();
        }
    }

    private void loadControlSamples(int genomicSourceIndex) throws ValidationException, IOException {
        if (getLoadSamples() && getControlSamplesFile() != null) {
            logStart();
            GenomicDataSourceConfiguration genomicSource = studyConfiguration.getGenomicDataSources().get(genomicSourceIndex);
            service.addControlSampleSet(genomicSource, getControlSampleSetName(), getControlSamplesFile(), getControlSamplesFile().getName());
            assertEquals(getExpectedControlSampleCount(), studyConfiguration.
                    getControlSampleSet(getControlSampleSetName()).getSamples().size());
            logEnd();
        }
    }

    protected File getSampleMappingFile() {
        return null;
    }

    protected String getControlSampleSetName() {
        return null;
    }

    protected File getControlSamplesFile() {
        return null;
    }

    protected String getControlSamplesFileName() {
        return null;
    }

    private void deploy(UserWorkspace userWorkspace)
    throws ConnectionException, DataRetrievalException, ValidationException, IOException, InvalidCriterionException {
        logStart();
        service.setStudyLastModifiedByCurrentUser(studyConfiguration, userWorkspace, null, LogEntry.getSystemLogDeploy(studyConfiguration.getStudy()));
        deploymentService.prepareForDeployment(studyConfiguration);
        Status status = deploymentService.performDeployment(studyConfiguration, getHeatmapParameters()).getStatus();
        logEnd();
        if (status.equals(Status.ERROR)) {
            fail(studyConfiguration.getStatusDescription());
        }
    }

    private void authorizeStudyElements() throws ConnectionException, DataRetrievalException, ValidationException,
            IOException, InvalidCriterionException, CSException {
        if (getAuthorizeStudy()) {
            logStart();
            AuthorizedStudyElementsGroup authorizedStudyElementsGroup1 = new AuthorizedStudyElementsGroup();
            authorizedStudyElementsGroup1 = createAuthorizedStudyElementsGroup(studyConfiguration,
                    "Group 1 for " + getStudyName(), getQueryFieldDescriptorName(), getQueryAnnotationValue());
            service.addAuthorizedStudyElementsGroup(studyConfiguration, authorizedStudyElementsGroup1);

            AuthorizedStudyElementsGroup authorizedStudyElementsGroup2 = new AuthorizedStudyElementsGroup();
            authorizedStudyElementsGroup2 = createAuthorizedStudyElementsGroup(studyConfiguration,
                    "Group 2 for " + getStudyName(), "Age", StringUtils.EMPTY);
            service.addAuthorizedStudyElementsGroup(studyConfiguration, authorizedStudyElementsGroup2);

            service.deleteAuthorizedStudyElementsGroup(studyConfiguration, authorizedStudyElementsGroup2);
            logEnd();
        }
    }

    /**
     * This method creates and returns an AuthorizedStudyElementsGroup that consists of elements from the current
     * studyConfiguration.
     */
    protected AuthorizedStudyElementsGroup createAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
            String authorizedStudyElementsGroupName, String fieldDescriptorName,
            String annotationValue)  throws CSException {
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        authorizedStudyElementsGroup.setStudyConfiguration(studyConfiguration);
        String desc = "Created by integration test for study named: " + getStudyName();

        Group group = new Group();
        group.setGroupName(authorizedStudyElementsGroupName);
        group.setGroupDesc(desc);
        getAuthorizationManager().createGroup(group);

        authorizedStudyElementsGroup.setAuthorizedGroup(group);
        // add AuthorizedAnnotationFieldDescriptor
        AnnotationFieldDescriptor annotationFieldDescriptor = new AnnotationFieldDescriptor();
        annotationFieldDescriptor = studyConfiguration.getExistingFieldDescriptorInStudy(fieldDescriptorName);
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor = new AuthorizedAnnotationFieldDescriptor();
        authorizedAnnotationFieldDescriptor.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedAnnotationFieldDescriptor.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        authorizedStudyElementsGroup.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor);
        // add AuthorizedGenomicDataSourceConfigurations
        AuthorizedGenomicDataSourceConfiguration authorizedGenomicDataSourceConfiguration = new AuthorizedGenomicDataSourceConfiguration();
        authorizedGenomicDataSourceConfiguration.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedGenomicDataSourceConfiguration.setGenomicDataSourceConfiguration(studyConfiguration.getGenomicDataSources().get(0));
        authorizedStudyElementsGroup.getAuthorizedGenomicDataSourceConfigurations().add(authorizedGenomicDataSourceConfiguration);
        // add AuthorizedQuery
        Query query = new Query();
        query.setName("TestAuthorizationQuery");
        query.setDescription(desc);

        for (StudySubscription studySubscription : workspaceService.getWorkspace().getSubscriptionCollection()) {
            if (studySubscription.getStudy().getId().equals(studyConfiguration.getStudy().getId())) {
                query.setSubscription(studySubscription);
            }
        }

        query.setLastModifiedDate(new Date());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        StringComparisonCriterion stringComparisonCriterion = new StringComparisonCriterion();
        stringComparisonCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
        stringComparisonCriterion.setStringValue(annotationValue);
        stringComparisonCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        AbstractCriterion abstractCriterion = stringComparisonCriterion;
        HashSet<AbstractCriterion> abstractCriterionCollection = new HashSet<AbstractCriterion>();
        abstractCriterionCollection.add(abstractCriterion);
        query.getCompoundCriterion().setCriterionCollection(abstractCriterionCollection);
        AuthorizedQuery authorizedQuery = new AuthorizedQuery();
        authorizedQuery.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedQuery.setQuery(query);
        authorizedStudyElementsGroup.getAuthorizedQuerys().add(authorizedQuery);

        return authorizedStudyElementsGroup;
    }

    protected HeatmapParameters getHeatmapParameters() {
        HeatmapParameters heatmapParameters = new HeatmapParameters();
        heatmapParameters.setViewAllData(true);
        heatmapParameters.setLargeBinsFile(TestDataFiles.HEATMAP_LARGE_BINS_FILE.getAbsolutePath());
        heatmapParameters.setSmallBinsFile(TestDataFiles.HEATMAP_SMALL_BINS_FILE.getAbsolutePath());
        return heatmapParameters;
    }

    private void loadClinicalData()
    throws IOException, ValidationException, ConnectionException, InvalidFieldDescriptorException {
        logStart();
        sourceConfiguration =
            service.addClinicalAnnotationFile(studyConfiguration, getSubjectAnnotationFile(),
                    getSubjectAnnotationFile().getName(), true);
        if (getAnnotationGroupFile() == null) {
            sourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        }
        assertTrue(sourceConfiguration.isLoadable());
        sourceConfiguration = service.loadClinicalAnnotation(studyConfiguration.getId(), sourceConfiguration.getId());
        assertTrue(sourceConfiguration.isCurrentlyLoaded());
        logEnd();
    }

    abstract protected File getSubjectAnnotationFile();

    private void loadAnnotationGroup()
    throws IOException, ValidationException, ConnectionException {
        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setName(Study.DEFAULT_ANNOTATION_GROUP);
        annotationGroup.setDescription("Created by integration test");
        getService().saveAnnotationGroup(
                annotationGroup, studyConfiguration, getAnnotationGroupFile());
    }

    private void loadSurvivalValueDefinition() {
        SurvivalValueDefinition definition = new SurvivalValueDefinition();
        definition.setSurvivalStartDate(dao.getAnnotationDefinition(getSurvivalStartDateName(), AnnotationTypeEnum.DATE));
        definition.setDeathDate(dao.getAnnotationDefinition(getDeathDateName(), AnnotationTypeEnum.DATE));
        definition.setLastFollowupDate(dao.getAnnotationDefinition(getLastFollowupDateName(), AnnotationTypeEnum.DATE));
        definition.setName("Survival From Start Date");
        studyConfiguration.getStudy().getSurvivalValueDefinitionCollection().add(definition);
        service.save(getStudyConfiguration());
    }

    protected String getSurvivalStartDateName() {
        return null;
    }

    protected String getDeathDateName() {
        return null;
    }

    protected String getLastFollowupDateName() {
        return null;
    }

    abstract protected File getAnnotationGroupFile();

    public CaIntegrator2Dao getCaIntegrator2Dao() {
        return dao;
    }

    public void setCaIntegrator2Dao(CaIntegrator2Dao caIntegrator2Dao) {
        this.dao = caIntegrator2Dao;
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }

    /**
     * @param arrayDataService the arrayDataService to set
     */
    public void setArrayDataService(ArrayDataService arrayDataService) {
        this.arrayDataService = arrayDataService;
    }

    private void checkQueries() throws InvalidCriterionException {
        checkClinicalQuery();
        checkGenomicQuery();
    }

    private void checkClinicalQuery() throws InvalidCriterionException {
        logStart();
        Query query = createQuery();
        query.setResultType(ResultTypeEnum.CLINICAL);

        AnnotationFieldDescriptor annotationFieldDescriptor = new AnnotationFieldDescriptor();
        annotationFieldDescriptor = studyConfiguration.getExistingFieldDescriptorInStudy(getQueryFieldDescriptorName());
        StringComparisonCriterion stringComparisonCriterion = new StringComparisonCriterion();
        stringComparisonCriterion.setWildCardType(WildCardTypeEnum.WILDCARD_OFF);
        stringComparisonCriterion.setStringValue(getQueryAnnotationValue());
        stringComparisonCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        stringComparisonCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        AbstractCriterion abstractCriterion = stringComparisonCriterion;
        HashSet<AbstractCriterion> abstractCriterionCollection = new HashSet<AbstractCriterion>();
        abstractCriterionCollection.add(abstractCriterion);
        query.getCompoundCriterion().setCriterionCollection(abstractCriterionCollection);

        QueryResult result = queryManagementService.execute(query);
        assertFalse(result.getRowCollection().isEmpty());
        logEnd();
    }

    /**
     * This method returns the name of the Query AnnotationFieldDescriptor
     * that is to be used for constructing a query to limit access to study
     * data.  Override this method and replace the null value with the name
     * of the column that will used in the query criterion to authorize access
     * to subjects.
     * @return
     */
    protected String getQueryFieldDescriptorName() {
        return null;
    }

    /**
     * This method returns the name of the Query AnotationValue
     * that is to be used for constructing a query to limit access to study
     * data.  Override this method and replace the null value with the value
     * that will be used in the query criterion to authorize access
     * to subjects.
     * @return
     */
    protected String getQueryAnnotationValue() {
        return null;
    }

    private void checkGenomicQuery() throws InvalidCriterionException {
        if (getLoadSamples() && getLoadDesign()) {
            logStart();
            Query query = createQuery();
            query.setResultType(ResultTypeEnum.GENE_EXPRESSION);
            query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
            geneNameCriterion.setGeneSymbol("EGFR");
            geneNameCriterion.setPlatformName(design.getName());
            geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.GENE_EXPRESSION);

            HashSet<AbstractCriterion> abstractCriterionCollection = new HashSet<AbstractCriterion>();
            abstractCriterionCollection.add(geneNameCriterion);
            query.getCompoundCriterion().setCriterionCollection(abstractCriterionCollection);
            GenomicDataQueryResult result = queryManagementService.executeGenomicDataQuery(query);
            assertFalse(result.getColumnCollection().isEmpty());
            assertFalse(result.getFilteredRowCollection().isEmpty());
            logEnd();
        }
    }

    abstract protected Logger getLogger();

    private Query createQuery() {
        Query query = new Query();
        query.setColumnCollection(new HashSet<ResultColumn>());
        query.setCompoundCriterion(new CompoundCriterion());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);
        query.getCompoundCriterion().setCriterionCollection(new HashSet<AbstractCriterion>());
        query.setSubscription(new StudySubscription());
        query.getSubscription().setStudy(studyConfiguration.getStudy());
        query.getSubscription().setUserWorkspace(studyConfiguration.getUserWorkspace());
        return query;
    }

    protected String getPlatformName() {
        return null;
    }

    protected PlatformVendorEnum getPlatformVendor() {
        return null;
    }

    /**
     * @return the queryManagementService
     */
    public QueryManagementService getQueryManagementService() {
        return queryManagementService;
    }

    /**
     * @param queryManagementService the queryManagementService to set
     */
    public void setQueryManagementService(QueryManagementService queryManagementService) {
        this.queryManagementService = queryManagementService;
    }

    protected String getCaArrayUsername() {
        return null;
    }

    protected String getCaArrayPassword() {
        return null;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * @return the studyConfiguration
     */
    protected StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @return the workspaceService
     */
    public WorkspaceService getWorkspaceService() {
        return workspaceService;
    }

    /**
     * @param workspaceService the workspaceService to set
     */
    public void setWorkspaceService(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    protected StudyManagementService getService() {
        return service;
    }

    /**
     * @return the deploymentService
     */
    public DeploymentService getDeploymentService() {
        return deploymentService;
    }

    /**
     * @param deploymentService the deploymentService to set
     */
    public void setDeploymentService(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    /**
     * @return the isPublicSubscription
     */
    public boolean isPublicSubscription() {
        return isPublicSubscription;
    }

    /**
     * @param isPublicSubscription the isPublicSubscription to set
     */
    public void setPublicSubscription(boolean isPublicSubscription) {
        this.isPublicSubscription = isPublicSubscription;
    }

    /**
     * @return the authorizationManagerFactory
     */
    public AuthorizationManagerFactory getAuthorizationManagerFactory() {
        return authorizationManagerFactory;
    }

    /**
     * @param authorizationManagerFactory the authorizationManagerFactory to set
     */
    public void setAuthorizationManagerFactory(AuthorizationManagerFactory authorizationManagerFactory) {
        this.authorizationManagerFactory = authorizationManagerFactory;
    }

    /**
     * @return the authorization manager
     * @throws CSException on error
     */
    public AuthorizationManager getAuthorizationManager() throws CSException {
        return authorizationManagerFactory.getAuthorizationManager(APPLICATION_CONTEXT_NAME);
    }
}
