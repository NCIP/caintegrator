/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.AffymetrixSnpPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.AgilentExpressionPlatformSource;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator.application.arraydata.PlatformLoadingException;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.application.query.QueryManagementService;
import gov.nih.nci.caintegrator.application.study.deployment.DeploymentService;
import gov.nih.nci.caintegrator.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator.common.Cai2Util;
import gov.nih.nci.caintegrator.common.QueryUtil;
import gov.nih.nci.caintegrator.data.CaIntegrator2Dao;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.BooleanOperatorEnum;
import gov.nih.nci.caintegrator.domain.application.CompoundCriterion;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator.domain.application.GenomicCriterionTypeEnum;
import gov.nih.nci.caintegrator.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator.domain.application.Query;
import gov.nih.nci.caintegrator.domain.application.QueryResult;
import gov.nih.nci.caintegrator.domain.application.ResultColumn;
import gov.nih.nci.caintegrator.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator.domain.application.SelectedValueCriterion;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator.domain.genomic.Platform;
import gov.nih.nci.caintegrator.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.DataRetrievalException;
import gov.nih.nci.caintegrator.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator.file.FileManager;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator.security.AuthorizationManagerFactory;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.authorization.domainobjects.Group;
import gov.nih.nci.security.authorization.domainobjects.ProtectionElement;
import gov.nih.nci.security.exceptions.CSException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Base class for study deployment integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
@TransactionConfiguration(defaultRollback = false)
public abstract class AbstractDeployStudyTestIntegration extends AbstractMockitoTest {
    private static final String USER_NCIMANAGER = "ncimanager";
    private static final String USER_NCIMANAGER_ID = "3";
    private static final String PROTECTION_GROUP_ID = "2";
    private static final String APPLICATION_CONTEXT_NAME = "caintegrator";
    private static final int CAARRAY_JNDI_PORT = 31099;
    private static final String CAARRAY_HOSTNAME = "ncias-d227-v.nci.nih.gov";


    @Autowired
    private StudyManagementService studyManagementService;
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

    /**
     * Deploys the configured study.
     * @throws Exception on unexpected error
     */
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
        studyManagementService.save(studyConfiguration);
        workspaceService.saveUserWorkspace(userWorkspace);
        studyManagementService.createProtectionElement(studyConfiguration);
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
        workspaceService.subscribe(userWorkspace, studyConfiguration.getStudy(), Boolean.FALSE);
        authorizeStudyElements();
        checkArrayData();
        checkQueries();
    }

    private void loadCopyNumberMappingFile() throws ConnectionException, ExperimentNotFoundException, IOException,
        ValidationException {
        if (getCopyNumberFile() != null) {
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.getServerProfile().setHostname(CAARRAY_HOSTNAME);
            genomicSource.getServerProfile().setPort(CAARRAY_JNDI_PORT);
            genomicSource.setExperimentIdentifier(getCopyNumberCaArrayId());
            genomicSource.setPlatformVendor(getPlatformVendor());
            genomicSource.setPlatformName(getCopyNumberPlatformName());
            genomicSource.setDataType(PlatformDataTypeEnum.COPY_NUMBER);
            genomicSource.setLoadingType(ArrayDataLoadingTypeEnum.CNCHP);
            genomicSource.setLoadingType(getCopyNumberLoadingType());
            studyManagementService.addGenomicSource(studyConfiguration, genomicSource);
            studyManagementService.saveDnaAnalysisMappingFile(genomicSource, getCopyNumberFile(),
                    getCopyNumberFile().getName());
            configureSegmentationDataCalcuation(genomicSource.getDnaAnalysisDataConfiguration());
        }
    }

    /**
     * @return the copy number platform name
     */
    protected String getCopyNumberPlatformName() {
        return null;
    }

    /**
     * @return the copy number loading type
     */
    protected ArrayDataLoadingTypeEnum getCopyNumberLoadingType() {
        return ArrayDataLoadingTypeEnum.SINGLE_SAMPLE_PER_FILE;
    }

    /**
     * @param dnaAnalysisDataConfiguration the dna anlysis configuration
     */
    protected void configureSegmentationDataCalcuation(DnaAnalysisDataConfiguration dnaAnalysisDataConfiguration) {
        // no-op
    }

    /**
     * @return the copy number caArray Id
     */
    protected String getCopyNumberCaArrayId() {
        return null;
    }

    /**
     * @return the copy number file
     */
    protected File getCopyNumberFile() {
        return null;
    }

    private void clearStudyDirectory(Study study) throws IOException {
        File studyDirectory = fileManager.getStudyDirectory(study);
        if (studyDirectory.exists()) {
            FileUtils.cleanDirectory(studyDirectory);
        }
    }

    /**
     * @return the study name
     */
    protected abstract String getStudyName();

    /**
     * @return the study description
     */
    protected String getDescription() {
        return getStudyName() + " demo study";
    }

    private void loadDesigns() throws PlatformLoadingException {
        if (getLoadDesign()) {
            design = getOrLoadDesign(getPlatformSource());
            for (AbstractPlatformSource platformSource : getAdditionalPlatformSources()) {
                getOrLoadDesign(platformSource);
            }
        }
    }

    /**
     * @return additional platform sources
     */
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

    /**
     * @return the platform source
     */
    protected AbstractPlatformSource getPlatformSource() {
        return null;
    }

    /**
     * @return whether or not to load the array design
     */
    protected boolean getLoadDesign() {
        return false;
    }

    /**
     * @return the NCIA server url
     */
    protected String getNCIAServerUrl() {
        return null;
    }

    private ImageDataSourceConfiguration loadImages() throws ConnectionException, InvalidImagingCollectionException {
        if (getLoadImages()) {
            ImageDataSourceConfiguration imageSource = new ImageDataSourceConfiguration();
            imageSource.getServerProfile().setUrl(getNCIAServerUrl());
            imageSource.getServerProfile().setHostname(Cai2Util.getHostNameFromUrl(getNCIAServerUrl()));
            imageSource.setCollectionName(getNCIATrialId());
            imageSource.setMappingFileName(getImageMappingFile().getName());
            studyManagementService.addImageSource(studyConfiguration, imageSource);
            return imageSource;
        }
        return null;
    }

    /**
     * @return whether or not to load images
     */
    protected boolean getLoadImages() {
        return false;
    }

    /**
     * @return the NCIA Trial Id
     */
    protected String getNCIATrialId() {
        return null;
    }

    private void loadImageAnnotation(ImageDataSourceConfiguration imageSource) throws ValidationException, IOException {
        if (getLoadImageAnnotation()) {
            ImageAnnotationConfiguration imageAnnotationConfiguration =
                studyManagementService.addImageAnnotationFile(imageSource, getImageAnnotationFile(),
                        getImageAnnotationFile().getName(), true);
            imageSource.setImageAnnotationConfiguration(imageAnnotationConfiguration);
            imageAnnotationConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
            for (ImageDataSourceConfiguration configuration : studyConfiguration.getImageDataSources()) {
                studyManagementService.loadImageAnnotation(configuration);
            }
        }
    }

    /**
     * @return whether to load image annotations
     */
    protected boolean getLoadImageAnnotation() {
        return false;
    }

    /**
     * @return the image annotation file
     */
    protected File getImageAnnotationFile() {
        return null;
    }

    private void mapImages(ImageDataSourceConfiguration imageSource) throws ValidationException, IOException {
        if (getMapImages()) {
            studyManagementService.mapImageSeriesAcquisitions(studyConfiguration, imageSource,
                    getImageMappingFile(), ImageDataSourceMappingTypeEnum.IMAGE_SERIES);
        }
    }

    /**
     * @return whether or not to map images to the study
     */
    protected boolean getMapImages() {
        return false;
    }

    /**
     * @return the image mapping file
     */
    protected File getImageMappingFile() {
        return null;
    }

    /**
     * @return whether or not to add authorization elements to the study
     */
    protected boolean getAuthorizeStudy() {
        return false;
    }

    private void checkArrayData() {
        if (getLoadSamples()) {
            PlatformHelper platformHelper = new PlatformHelper(design);
            Set<ReporterList> probeSetReporterLists =
                    platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            for (ReporterList probeSetReporterList : probeSetReporterLists) {
                DataRetrievalRequest probeSetRequest = new DataRetrievalRequest();
                probeSetRequest.addArrayDatas(getStudyArrayDatas(probeSetReporterList.getArrayDatas()));
                probeSetRequest.addType(ArrayDataValueType.EXPRESSION_SIGNAL);
                probeSetRequest.addReporters(probeSetReporterList.getReporters());
                DataRetrievalRequest geneRequest = new DataRetrievalRequest();
                Set<ReporterList> geneReporterLists =
                        platformHelper.getReporterLists(ReporterTypeEnum.GENE_EXPRESSION_GENE);
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

    /**
     * @return the expected number of gene reporters
     */
    protected int getExpectedNumberOfGeneReporters() {
        return 0;
    }

    /**
     * @return the expected number of probe sets
     */
    protected int getExpectedNumberProbeSets() {
        return 0;
    }

    /**
     * @return whether or not to load samples
     */
    protected boolean getLoadSamples() {
        return false;
    }

    /**
     * @return the example number of total samples
     */
    protected int getExpectedSampleCount() {
        return 0;
    }

    /**
     * @return the expected number of mapped samples
     */
    protected int getExpectedMappedSampleCount() {
        return 0;
    }

    /**
     * @return the expected number of control samples
     */
    protected int getExpectedControlSampleCount() {
        return 0;
    }

    private void loadSamples() throws ConnectionException, ExperimentNotFoundException {
        if (getLoadSamples()) {
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.getServerProfile().setHostname(CAARRAY_HOSTNAME);
            genomicSource.getServerProfile().setPort(CAARRAY_JNDI_PORT);
            genomicSource.setExperimentIdentifier(getCaArrayId());
            genomicSource.setPlatformName(getPlatformName());
            genomicSource.setPlatformVendor(getPlatformVendor());
            genomicSource.setDataType(PlatformDataTypeEnum.EXPRESSION);
            genomicSource.setLoadingType(ArrayDataLoadingTypeEnum.PARSED_DATA);
            if (getSampleMappingFile() != null) {
                genomicSource.setSampleMappingFileName(getSampleMappingFile().getName());
            }
            studyManagementService.addGenomicSource(studyConfiguration, genomicSource);
            assertTrue(genomicSource.getSamples().size() > 0);
            assertTrue(studyConfiguration.getGenomicDataSources().get(0).getExperimentIdentifier()
                    .equalsIgnoreCase(getCaArrayId()));
        }
    }

    /**
     * @return the caArray id
     */
    protected String getCaArrayId() {
        return null;
    }

    private void mapSamples() throws ValidationException, IOException {
        if (getLoadSamples()) {
            studyManagementService.mapSamples(studyConfiguration, getSampleMappingFile(),
                    studyConfiguration.getGenomicDataSources().get(0));
            assertEquals(getExpectedMappedSampleCount(),
                    studyConfiguration.getGenomicDataSources().get(0).getMappedSamples().size());
        }
    }

    private void loadControlSamples(int genomicSourceIndex) throws ValidationException, IOException {
        if (getLoadSamples() && getControlSamplesFile() != null) {
            GenomicDataSourceConfiguration genomicSource =
                    studyConfiguration.getGenomicDataSources().get(genomicSourceIndex);
            studyManagementService.addControlSampleSet(genomicSource, getControlSampleSetName(),
                    getControlSamplesFile(), getControlSamplesFile().getName());
            assertEquals(getExpectedControlSampleCount(), studyConfiguration.
                    getControlSampleSet(getControlSampleSetName()).getSamples().size());
        }
    }

    /**
     * @return the sample mapping file
     */
    protected File getSampleMappingFile() {
        return null;
    }

    /**
     * @return the control sample set name
     */
    protected String getControlSampleSetName() {
        return null;
    }

    /**
     * @return the control samples file
     */
    protected File getControlSamplesFile() {
        return null;
    }

    private void deploy(UserWorkspace userWorkspace)
    throws ConnectionException, DataRetrievalException, ValidationException, IOException, InvalidCriterionException {
        studyManagementService.setStudyLastModifiedByCurrentUser(studyConfiguration, userWorkspace, null,
                LogEntry.getSystemLogDeploy(studyConfiguration.getStudy()));
        deploymentService.prepareForDeployment(studyConfiguration);
        Status status = deploymentService.performDeployment(studyConfiguration, getHeatmapParameters()).getStatus();
        if (status.equals(Status.ERROR)) {
            fail(studyConfiguration.getStatusDescription());
        }
    }

    private void authorizeStudyElements() throws ConnectionException, DataRetrievalException, ValidationException,
            IOException, InvalidCriterionException, CSException {
        if (getAuthorizeStudy()) {
            AuthorizedStudyElementsGroup authorizedStudyElementsGroup =
                    createAuthorizedStudyElementsGroup(studyConfiguration, "Group 1 for " + getStudyName(),
                            getQueryFieldDescriptorName(), getQueryAnnotationValue());
            studyManagementService.daoSave(authorizedStudyElementsGroup);
        }
    }

    /**
     * This method creates and returns an AuthorizedStudyElementsGroup that consists of elements from the current
     * studyConfiguration.
     */
    private AuthorizedStudyElementsGroup createAuthorizedStudyElementsGroup(StudyConfiguration studyConfig,
            String authorizedStudyElementsGroupName, String fieldDescriptorName,
            String annotationValue)  throws CSException {
        AuthorizedStudyElementsGroup authorizedStudyElementsGroup = new AuthorizedStudyElementsGroup();
        authorizedStudyElementsGroup.setStudyConfiguration(studyConfig);
        String desc = "Created by integration test for study named: " + getStudyName();

        Group group = new Group();
        group.setGroupName(authorizedStudyElementsGroupName);
        group.setGroupDesc(desc);
        getAuthorizationManager().createGroup(group);

        getAuthorizationManager().addUsersToGroup(group.getGroupId().toString(), new String[] {USER_NCIMANAGER_ID});
        authorizedStudyElementsGroup.setAuthorizedGroup(group);

        studyManagementService.addAuthorizedStudyElementsGroup(getStudyConfiguration(), authorizedStudyElementsGroup);

        ProtectionElement pe =
                getAuthorizationManager().getProtectionElement(authorizedStudyElementsGroup.getClass().getName());
        getAuthorizationManager().addProtectionElements(PROTECTION_GROUP_ID,
                new String[] {pe.getProtectionElementId().toString()});

        // add AuthorizedAnnotationFieldDescriptor
        AnnotationFieldDescriptor annotationFieldDescriptor =
                studyConfig.getExistingFieldDescriptorInStudy(fieldDescriptorName);
        AuthorizedAnnotationFieldDescriptor authorizedAnnotationFieldDescriptor =
                new AuthorizedAnnotationFieldDescriptor();
        authorizedAnnotationFieldDescriptor.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedAnnotationFieldDescriptor.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        authorizedStudyElementsGroup.getAuthorizedAnnotationFieldDescriptors().add(authorizedAnnotationFieldDescriptor);

        // add AuthorizedGenomicDataSourceConfigurations
        AuthorizedGenomicDataSourceConfiguration authorizedGenomicDataSourceConfiguration =
                new AuthorizedGenomicDataSourceConfiguration();
        authorizedGenomicDataSourceConfiguration.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedGenomicDataSourceConfiguration
            .setGenomicDataSourceConfiguration(studyConfig.getGenomicDataSources().get(0));
        authorizedStudyElementsGroup
            .getAuthorizedGenomicDataSourceConfigurations().add(authorizedGenomicDataSourceConfiguration);

        //Add AuthorizedQuery
        Query query = new Query();
        query.setName("Authorized Query for " + getStudyName());
        query.setLastModifiedDate(new Date());
        query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.OR);
        query.getCompoundCriterion().getCriterionCollection()
            .add(QueryUtil.createStringComparisonCriterion(annotationFieldDescriptor, annotationValue));

        AuthorizedQuery authorizedQuery = new AuthorizedQuery();
        authorizedQuery.setAuthorizedStudyElementsGroup(authorizedStudyElementsGroup);
        authorizedQuery.setQuery(query);
        authorizedStudyElementsGroup.getAuthorizedQuerys().add(authorizedQuery);
        return authorizedStudyElementsGroup;
    }

    private HeatmapParameters getHeatmapParameters() {
        HeatmapParameters heatmapParameters = new HeatmapParameters();
        heatmapParameters.setViewAllData(true);
        heatmapParameters.setLargeBinsFile(TestDataFiles.HEATMAP_LARGE_BINS_FILE.getAbsolutePath());
        heatmapParameters.setSmallBinsFile(TestDataFiles.HEATMAP_SMALL_BINS_FILE.getAbsolutePath());
        return heatmapParameters;
    }

    private void loadClinicalData() throws IOException, ValidationException, ConnectionException,
    InvalidFieldDescriptorException {
        sourceConfiguration = studyManagementService.addClinicalAnnotationFile(studyConfiguration,
                getSubjectAnnotationFile(), getSubjectAnnotationFile().getName(), true);
        if (getAnnotationGroupFile() == null) {
            sourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        }
        assertTrue(sourceConfiguration.isLoadable());
        sourceConfiguration = studyManagementService.loadClinicalAnnotation(studyConfiguration.getId(),
                sourceConfiguration.getId());
        assertTrue(sourceConfiguration.isCurrentlyLoaded());

        if (getAuthorizeStudy()) {
            for (AnnotationFieldDescriptor afd
                    : studyConfiguration.getClinicalConfigurationCollection().get(0).getAnnotationDescriptors()) {
                if (StringUtils.equalsIgnoreCase(getQueryFieldDescriptorName(), afd.getName())) {
                    afd.setShowInAuthorization(true);
                    break;
                }
            }
            studyManagementService.save(studyConfiguration);
        }
    }

    private void loadAnnotationGroup() throws IOException, ValidationException, ConnectionException {
        AnnotationGroup annotationGroup = new AnnotationGroup();
        annotationGroup.setName(Study.DEFAULT_ANNOTATION_GROUP);
        annotationGroup.setDescription("Created by integration test");
        studyManagementService.saveAnnotationGroup(annotationGroup, studyConfiguration, getAnnotationGroupFile());
    }

    private void loadSurvivalValueDefinition() {
        SurvivalValueDefinition definition = new SurvivalValueDefinition();
        definition.setSurvivalStartDate(dao.getAnnotationDefinition(getSurvivalStartDateName(),
                AnnotationTypeEnum.DATE));
        definition.setDeathDate(dao.getAnnotationDefinition(getDeathDateName(), AnnotationTypeEnum.DATE));
        definition.setLastFollowupDate(dao.getAnnotationDefinition(getLastFollowupDateName(), AnnotationTypeEnum.DATE));
        definition.setName("Survival From Start Date");
        studyConfiguration.getStudy().getSurvivalValueDefinitionCollection().add(definition);
        studyManagementService.save(getStudyConfiguration());
    }

    /**
     * @return the survival state date name
     */
    protected String getSurvivalStartDateName() {
        return null;
    }

    /**
     * @return the death date name
     */
    protected String getDeathDateName() {
        return null;
    }

    /**
     * @return the last followup date name
     */
    protected String getLastFollowupDateName() {
        return null;
    }

    /**
     * @return the annotation group file
     */
    protected abstract File getAnnotationGroupFile();

    /**
     * @return the subject annotation file
     */
    protected abstract File getSubjectAnnotationFile();

    private void checkQueries() throws InvalidCriterionException {
        checkClinicalQuery();
        checkGenomicQuery();
    }

    private void checkClinicalQuery() throws InvalidCriterionException {
        Query query = createQuery();
        query.setResultType(ResultTypeEnum.CLINICAL);
        query.getCompoundCriterion().getCriterionCollection().add(createAnnotationCriterion());

        QueryResult result = queryManagementService.execute(query);
        assertFalse(result.getRowCollection().isEmpty());
    }

    /**
     * This method returns the name of the Query AnnotationFieldDescriptor
     * that is to be used for constructing a query to limit access to study
     * data.  Override this method and replace the null value with the name
     * of the column that will used in the query criterion to authorize access
     * to subjects.
     * @return the field descriptor name
     */
    protected abstract String getQueryFieldDescriptorName();

    /**
     * This method returns the name of the Query AnotationValue
     * that is to be used for constructing a query to limit access to study
     * data.  Override this method and replace the null value with the value
     * that will be used in the query criterion to authorize access
     * to subjects.
     * @return the annotation value
     */
    protected abstract String getQueryAnnotationValue();

    private void checkGenomicQuery() throws InvalidCriterionException {
        if (getLoadSamples() && getLoadDesign()) {
            Query query = createQuery();
            query.setResultType(ResultTypeEnum.GENE_EXPRESSION);
            query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            query.getCompoundCriterion().setBooleanOperator(BooleanOperatorEnum.AND);

            GeneNameCriterion geneNameCriterion = new GeneNameCriterion();
            geneNameCriterion.setGeneSymbol("EGFR");
            geneNameCriterion.setGenomicCriterionType(GenomicCriterionTypeEnum.GENE_EXPRESSION);

            query.getCompoundCriterion().getCriterionCollection().add(geneNameCriterion);
            if (getAuthorizeStudy()) {
                query.getCompoundCriterion().getCriterionCollection().add(createAnnotationCriterion());
            }
            GenomicDataQueryResult result = queryManagementService.executeGenomicDataQuery(query);
            assertFalse(result.getColumnCollection().isEmpty());
            assertFalse(result.getFilteredRowCollection().isEmpty());
        }
    }

    private SelectedValueCriterion createAnnotationCriterion() {
        AnnotationFieldDescriptor annotationFieldDescriptor =
                studyConfiguration.getExistingFieldDescriptorInStudy(getQueryFieldDescriptorName());
        SelectedValueCriterion annotationCriterion = new SelectedValueCriterion();
        annotationCriterion.setAnnotationFieldDescriptor(annotationFieldDescriptor);
        annotationCriterion.setEntityType(EntityTypeEnum.SUBJECT);
        annotationCriterion.setFinalMaskApplied(false);
        for (PermissibleValue pv : annotationFieldDescriptor.getPermissibleValues()) {
            if (StringUtils.equalsIgnoreCase(getQueryAnnotationValue(), pv.getValue())) {
                annotationCriterion.setValueCollection(Arrays.asList(pv));
                break;
            }
        }
        return annotationCriterion;
    }

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

    /**
     * @return the platform name
     */
    protected String getPlatformName() {
        return null;
    }

    /**
     * @return the platform vendor
     */
    protected PlatformVendorEnum getPlatformVendor() {
        return null;
    }

    /**
     * @return the studyConfiguration
     */
    protected StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @return the authorization manager
     * @throws CSException on error
     */
    private AuthorizationManager getAuthorizationManager() throws CSException {
        return authorizationManagerFactory.getAuthorizationManager(APPLICATION_CONTEXT_NAME);
    }

    /**
     * @return the arrayDataService
     */
    public ArrayDataService getArrayDataService() {
        return arrayDataService;
    }
}
