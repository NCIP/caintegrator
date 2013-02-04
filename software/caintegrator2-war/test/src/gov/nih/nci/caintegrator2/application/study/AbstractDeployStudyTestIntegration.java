/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixSnpPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AgilentExpressionPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValueType;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataValues;
import gov.nih.nci.caintegrator2.application.arraydata.DataRetrievalRequest;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformHelper;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformLoadingException;
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
import gov.nih.nci.caintegrator2.domain.application.GeneNameCriterion;
import gov.nih.nci.caintegrator2.domain.application.GenomicDataQueryResult;
import gov.nih.nci.caintegrator2.domain.application.Query;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.ResultTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.test.AbstractTransactionalSpringContextTests;

public abstract class AbstractDeployStudyTestIntegration extends AbstractTransactionalSpringContextTests {
    
    private final Logger logger = Logger.getLogger(getClass());
    private long startTime;
    
    private StudyManagementService service;
    private DeploymentService deploymentService;
    private QueryManagementService queryManagementService;
    private WorkspaceService workspaceService;
    private StudyConfiguration studyConfiguration;
    private DelimitedTextClinicalSourceConfiguration sourceConfiguration;
    private CaIntegrator2Dao dao;
    private ArrayDataService arrayDataService;
    private Platform design;
    private FileManager fileManager;
    
    public AbstractDeployStudyTestIntegration() {
        setDefaultRollback(false);
    }
    
    protected String[] getConfigLocations() {
        return new String[] {"classpath*:/**/service-test-integration-config.xml"};
    }
    
    /**
     * @param caIntegrator2Dao the caIntegrator2Dao to set
     */
    public void setStudyManagementService(StudyManagementService studyManagementService) {
        this.service = studyManagementService;
    }
    
    public void deployStudy() throws Exception {
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
        authentication.setUsername("manager");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        loadDesigns();
        UserWorkspace userWorkspace = workspaceService.getWorkspace();
        studyConfiguration = new StudyConfiguration();
        studyConfiguration.getStudy().setShortTitleText(getStudyName());
        studyConfiguration.getStudy().setLongTitleText(getDescription());
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
        checkArrayData();
        checkQueries();
    }

    private void loadCopyNumberMappingFile() throws ConnectionException, ExperimentNotFoundException, IOException {
        if (getCopyNumberFile() != null) {
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.getServerProfile().setHostname(getCopyNumberCaArrayHostname());
            genomicSource.getServerProfile().setPort(getCopyNumberCaArrayPort());
            genomicSource.getServerProfile().setUsername(getCaArrayUsername());
            genomicSource.getServerProfile().setPassword(getCaArrayPassword());
            genomicSource.setExperimentIdentifier(getCopyNumberCaArrayId());
            genomicSource.setPlatformVendor(getPlatformVendor());
            genomicSource.setDataType(GenomicDataSourceDataTypeEnum.COPY_NUMBER);
            genomicSource.setUseSupplementalFiles(getUseSupplementalFiles());
            service.addGenomicSource(studyConfiguration, genomicSource);
            getService().saveDnaAnalysisMappingFile(genomicSource, getCopyNumberFile(), getCopyNumberFile().getName());
            configureSegmentationDataCalcuation(genomicSource.getDnaAnalysisDataConfiguration());
        }
    }

    protected boolean getUseSupplementalFiles() {
        return true;
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
            platform = arrayDataService.loadArrayDesign(configuration, null).getPlatform();
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
            genomicSource.setDataType(GenomicDataSourceDataTypeEnum.EXPRESSION);
            genomicSource.setUseSupplementalFiles(getUseSupplementalFiles());
            if (getSampleMappingFile() != null) {
                genomicSource.setSampleMappingFileName(getSampleMappingFile().getName());
            }
            service.addGenomicSource(studyConfiguration, genomicSource);
            assertTrue(genomicSource.getSamples().size() > 0);
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
    throws ConnectionException, DataRetrievalException, ValidationException {
        logStart();
        service.setStudyLastModifiedByCurrentUser(studyConfiguration, userWorkspace, null, LogEntry.getSystemLogDeploy(studyConfiguration.getStudy()));
        deploymentService.prepareForDeployment(studyConfiguration, null);
        Status status = deploymentService.performDeployment(studyConfiguration, null);
        logEnd();
        if (status.equals(Status.ERROR)) {
            fail(studyConfiguration.getStatusDescription());
        }
    }

    private void loadClinicalData()
    throws IOException, ValidationException, ConnectionException {
        logStart();
        sourceConfiguration = 
            service.addClinicalAnnotationFile(studyConfiguration, getSubjectAnnotationFile(), 
                    getSubjectAnnotationFile().getName(), true);
        if (getAnnotationGroupFile() == null) {
            sourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        }
        assertTrue(sourceConfiguration.isLoadable());
        service.loadClinicalAnnotation(studyConfiguration, sourceConfiguration);
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

    private void checkClinicalQuery() {
        logStart();
        Query query = createQuery();
        query.setResultType(ResultTypeEnum.CLINICAL);
        logEnd();
    }

    private void checkGenomicQuery() throws InvalidCriterionException {
        if (getLoadSamples() && getLoadDesign()) {
            logStart();
            Query query = createQuery();
            query.setResultType(ResultTypeEnum.GENOMIC);
            query.setReporterType(ReporterTypeEnum.GENE_EXPRESSION_PROBE_SET);
            GeneNameCriterion geneCriterion = new GeneNameCriterion();
            geneCriterion.setGeneSymbol("EGFR");
            geneCriterion.setPlatformName(design.getName());
            query.getCompoundCriterion().getCriterionCollection().add(geneCriterion);
            
            GenomicDataQueryResult result = queryManagementService.executeGenomicDataQuery(query);
            assertFalse(result.getColumnCollection().isEmpty());
            assertFalse(result.getRowCollection().isEmpty());
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
        return query;
    }

    protected String getPlatformName() {
        return null;
    }

    protected String getPlatformVendor() {
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

}
