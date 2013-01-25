/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.arraydata.AbstractPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixDnaPlatformSource;
import gov.nih.nci.caintegrator2.application.arraydata.AffymetrixExpressionPlatformSource;
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
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
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
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.security.exceptions.CSException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.test.AbstractTransactionalSpringContextTests;

import au.com.bytecode.opencsv.CSVReader;

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
    
    public void deployStudy() throws ValidationException, IOException, ConnectionException, PlatformLoadingException, DataRetrievalException, ExperimentNotFoundException, InvalidCriterionException, CSException {
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
        authentication.setUsername("manager");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        loadDesigns();
        UserWorkspace userWorkspace = workspaceService.getWorkspace();
        studyConfiguration = new StudyConfiguration();
        studyConfiguration.getStudy().setShortTitleText(getStudyName());
        studyConfiguration.getStudy().setLongTitleText(getDescription());
        studyConfiguration.setUserWorkspace(userWorkspace);
        service.save(studyConfiguration);
        workspaceService.saveUserWorkspace(userWorkspace);
        service.createProtectionElement(studyConfiguration);
        clearStudyDirectory(studyConfiguration.getStudy());
        loadAnnotationDefinitions();
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
            service.addGenomicSource(studyConfiguration, genomicSource);
            getService().saveCopyNumberMappingFile(genomicSource, getCopyNumberFile(), getCopyNumberFile().getName());
            configureSegmentationDataCalcuation(genomicSource.getCopyNumberDataConfiguration());
        }
    }

    protected int getCopyNumberCaArrayPort() {
        return 31099;
    }

    protected void configureSegmentationDataCalcuation(CopyNumberDataConfiguration copyNumberDataConfiguration) {
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
            arrayDataService.savePlatformConfiguration(configuration);
            platform = arrayDataService.loadArrayDesign(configuration, null).getPlatform();
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
        } else if (platformSource instanceof AffymetrixDnaPlatformSource) {
            return ((AffymetrixDnaPlatformSource) platformSource).getPlatformName();
        } else {
            throw new IllegalArgumentException("Unknonw platform source type: " + platformSource.getClass().getName());
        }
    }

    protected abstract AbstractPlatformSource getPlatformSource();

    protected boolean getLoadDesign() {
        return true;
    }

    abstract protected String getNCIAServerUrl();
    
    private ImageDataSourceConfiguration loadImages() throws ConnectionException {
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
        return true;
    }

    abstract protected String getNCIATrialId();

    private void loadImageAnnotation(ImageDataSourceConfiguration imageSource) throws ValidationException, IOException {
        if (getLoadImageAnnotation()) {
            logStart();
            ImageAnnotationConfiguration imageAnnotationConfiguration = 
                service.addImageAnnotationFile(imageSource, getImageAnnotationFile(), 
                        getImageAnnotationFile().getName());
            imageSource.setImageAnnotationConfiguration(imageAnnotationConfiguration);
            imageAnnotationConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
            for (ImageDataSourceConfiguration configuration : studyConfiguration.getImageDataSources()) {
                service.loadImageAnnotation(configuration);    
            }
            logEnd();
        }
    }

    protected boolean getLoadImageAnnotation() {
        return true;
    }

    protected abstract File getImageAnnotationFile();

    private void mapImages(ImageDataSourceConfiguration imageSource) throws ValidationException, IOException {
        if (getMapImages()) {
            logStart();
            service.mapImageSeriesAcquisitions(studyConfiguration, imageSource, 
                    getImageMappingFile(), ImageDataSourceMappingTypeEnum.IMAGE_SERIES);
            logEnd();
        }
    }

    protected boolean getMapImages() {
        return true;
    }

    abstract protected File getImageMappingFile();

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

    protected abstract int getExpectedNumberOfGeneReporters();

    protected abstract int getExpectedNumberProbeSets();
    
    protected boolean getLoadSamples() {
        return true;
    }

    protected abstract int getExpectedSampleCount();

    protected abstract int getExpectedMappedSampleCount();

    protected abstract int getExpectedControlSampleCount();

    private void loadSamples() throws ConnectionException, ExperimentNotFoundException {
        if (getLoadSamples()) {
            logStart();
            GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
            genomicSource.getServerProfile().setHostname(getCaArrayHostname());
            genomicSource.getServerProfile().setPort(getCaArrayPort());
            genomicSource.getServerProfile().setUsername(getCaArrayUsername());
            genomicSource.getServerProfile().setPassword(getCaArrayPassword());
            genomicSource.setExperimentIdentifier(getCaArrayId());
            genomicSource.setPlatformVendor(getPlatformVendor());
            genomicSource.setDataType(GenomicDataSourceDataTypeEnum.EXPRESSION);
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

    abstract protected String getCaArrayId();
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

    abstract protected File getSampleMappingFile();

    abstract protected String getControlSampleSetName();

    abstract protected File getControlSamplesFile();

    abstract protected String getControlSamplesFileName();
    
    private void deploy(UserWorkspace userWorkspace) {
        logStart();
        service.setLastModifiedByCurrentUser(studyConfiguration, userWorkspace);
        deploymentService.prepareForDeployment(studyConfiguration, null);
        Status status = deploymentService.performDeployment(studyConfiguration, null);
        logEnd();
        if (status.equals(Status.ERROR)) {
            fail(studyConfiguration.getStatusDescription());
        }
    }

    private void loadClinicalData() throws IOException, ValidationException {
        logStart();
        sourceConfiguration = 
            service.addClinicalAnnotationFile(studyConfiguration, getSubjectAnnotationFile(), 
                    getSubjectAnnotationFile().getName());
        sourceConfiguration.getAnnotationFile().setIdentifierColumnIndex(0);
        assertTrue(sourceConfiguration.isLoadable());
        service.loadClinicalAnnotation(studyConfiguration, sourceConfiguration);
        assertTrue(sourceConfiguration.isCurrentlyLoaded());
        logEnd();
    }

    abstract protected File getSubjectAnnotationFile();

    private void loadAnnotationDefinitions() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(getAnnotationDefinitionsFile()));
        String[] fields;
        while ((fields = reader.readNext()) != null) {
            AnnotationDefinition definition = new AnnotationDefinition();
            definition.getCommonDataElement().setLongName(fields[0]);
            definition.setKeywords(definition.getDisplayName());
            definition.getCommonDataElement().getValueDomain().setDataType(AnnotationTypeEnum.getByValue(fields[1]));
            if (!StringUtils.isBlank(fields[2])) {
                Collection<PermissibleValue> permissibleValues = new HashSet<PermissibleValue>();
                String[] values = fields[2].split(";");
                for (String value : values) {
                    PermissibleValue permissibleValue = new PermissibleValue();
                    permissibleValue.setValue(value);
                    permissibleValues.add(permissibleValue);
                    dao.save(permissibleValue);
                }
                definition.getPermissibleValueCollection().addAll(permissibleValues);
            }
            dao.save(definition);
        }
    }
    
    private void loadSurvivalValueDefinition() {
        SurvivalValueDefinition definition = service.createNewSurvivalValueDefinition(studyConfiguration.getStudy());
        definition.setSurvivalStartDate(dao.getAnnotationDefinition(getSurvivalStartDateName()));
        definition.setDeathDate(dao.getAnnotationDefinition(getDeathDateName()));
        definition.setLastFollowupDate(dao.getAnnotationDefinition(getLastFollowupDateName()));
        definition.setName("Survival From Start Date");
        dao.save(definition);
    }
    
    abstract protected String getSurvivalStartDateName();
    abstract protected String getDeathDateName();
    abstract protected String getLastFollowupDateName();

    abstract protected File getAnnotationDefinitionsFile();
    
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

    abstract protected String getPlatformVendor();

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
