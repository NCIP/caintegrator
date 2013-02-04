/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.application.CaIntegrator2BaseService;
import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.application.arraydata.ArrayDataLoadingTypeEnum;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator2.application.workspace.WorkspaceService;
import gov.nih.nci.caintegrator2.common.AnnotationUtil;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.common.HibernateUtil;
import gov.nih.nci.caintegrator2.common.PermissibleValueUtil;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.SubjectAnnotation;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Array;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.aim.AIMFacade;
import gov.nih.nci.caintegrator2.external.aim.ImageSeriesAnnotationsWrapper;
import gov.nih.nci.caintegrator2.external.caarray.CaArrayFacade;
import gov.nih.nci.caintegrator2.external.caarray.DnaAnalysisFilesNotFoundException;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.caintegrator2.external.caarray.SamplesNotFoundException;
import gov.nih.nci.caintegrator2.external.cadsr.CaDSRFacade;
import gov.nih.nci.caintegrator2.external.ncia.NCIAFacade;
import gov.nih.nci.caintegrator2.file.FileManager;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSSecurityException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Entry point to the StudyManagementService subsystem.
 */
@Transactional(propagation = Propagation.REQUIRED)
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveClassLength" })   // see configure study
public class StudyManagementServiceImpl extends CaIntegrator2BaseService implements StudyManagementService {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = Logger.getLogger(StudyManagementServiceImpl.class);
    private static final int DEFINITION_LENGTH = 1000;
    private static final int MAX_ERROR_MESSAGE_LENGTH = 500;
    private FileManager fileManager;
    private CaDSRFacade caDSRFacade;
    private NCIAFacade nciaFacade;
    private AIMFacade aimFacade;
    private CaArrayFacade caArrayFacade;
    private WorkspaceService workspaceService;
    private SecurityManager securityManager;
    private AnalysisService analysisService;

    /**
     * {@inheritDoc}
     */
    public void save(StudyConfiguration studyConfiguration) {
        if (isNew(studyConfiguration)) {
            configureNew(studyConfiguration);
            getWorkspaceService().subscribe(
                    getWorkspaceService().getWorkspace(), studyConfiguration.getStudy(), false);
        }
        daoSave(studyConfiguration);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = ValidationException.class)
    public void save(AnnotationDefinition definition) throws ValidationException {
        if (!definition.getAnnotationValueCollection().isEmpty()) {
            Set<AbstractAnnotationValue> valuesToUpdate = new HashSet<AbstractAnnotationValue>();
            valuesToUpdate.addAll(definition.getAnnotationValueCollection());
            for (AbstractAnnotationValue value : valuesToUpdate) {
                value.convertAnnotationValue(definition);
            }
        }
        daoSave(definition);
    }
    
    /**
     * {@inheritDoc}
     */
    public void saveSubjectSourceStatus(AbstractClinicalSourceConfiguration source) {
        getDao().saveSubjectSourceStatus(source);
    }
    /**
     * {@inheritDoc}
     */
    public void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        securityManager.createProtectionElement(studyConfiguration);
    }
    
    /**
     * {@inheritDoc}
     */
    public void delete(StudyConfiguration studyConfiguration) throws CSException {
        securityManager.deleteProtectionElement(studyConfiguration);
        fileManager.deleteStudyDirectory(studyConfiguration);
        getWorkspaceService().unsubscribeAll(studyConfiguration.getStudy());
        daoSave(studyConfiguration.getUserWorkspace());
        studyConfiguration.setUserWorkspace(null);
        getDao().delete(studyConfiguration);
    }
    
    private boolean isNew(StudyConfiguration studyConfiguration) {
        return studyConfiguration.getId() == null;
    }

    private void configureNew(StudyConfiguration studyConfiguration) {
        configureNew(studyConfiguration.getStudy());
    }

    @SuppressWarnings({"PMD.CyclomaticComplexity", 
                        "PMD.ExcessiveMethodLength", 
                        "PMD.NPathComplexity" })   // multiple simple null checks
    private void configureNew(Study study) {
        if (study.getDefaultTimepoint() == null) {
            Timepoint defaultTimepoint = new Timepoint();
            String studyTitle = "";
            if (study.getShortTitleText() != null) {
                studyTitle = study.getShortTitleText();
            } else if (study.getLongTitleText() != null) {
                studyTitle = study.getLongTitleText();
            }
            defaultTimepoint.setDescription("Default Timepoint For Study '" + studyTitle + "'");
            defaultTimepoint.setName("Default");
            study.setDefaultTimepoint(defaultTimepoint);
        }
    }

    /**
     * {@inheritDoc}
     */
    public DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration,
            File inputFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException {
        File permanentFile = getFileManager().storeStudyFile(inputFile, filename, studyConfiguration);
        AnnotationFile annotationFile = AnnotationFile.load(permanentFile, getDao(), studyConfiguration,
                EntityTypeEnum.SUBJECT, createNewAnnotationDefinition);
        DelimitedTextClinicalSourceConfiguration clinicalSourceConfig = 
            new DelimitedTextClinicalSourceConfiguration(annotationFile, studyConfiguration);
        daoSave(clinicalSourceConfig);
        daoSave(studyConfiguration);
        return clinicalSourceConfig;
    }
    
    /**
     * {@inheritDoc}
     */
    public void addStudyLogo(StudyConfiguration studyConfiguration,
                             File imageFile,
                             String fileName, 
                             String fileType) throws IOException {
        if (studyConfiguration.getStudyLogo() == null) {
            studyConfiguration.setStudyLogo(new StudyLogo());
        }
        studyConfiguration.getStudyLogo().setFileName(fileName);
        studyConfiguration.getStudyLogo().setFileType(fileType);
        File studyLogoFile = getFileManager().storeStudyFile(imageFile, fileName, studyConfiguration);
        studyConfiguration.getStudyLogo().setPath(studyLogoFile.getPath());
        daoSave(studyConfiguration);
    }
    
    /**
     * {@inheritDoc}
     */
    public StudyLogo retrieveStudyLogo(Long studyId, String studyShortTitleText) {
        return getDao().retrieveStudyLogo(studyId, studyShortTitleText);
    }

    /**
     * {@inheritDoc}
     * @throws InvalidFieldDescriptorException 
     */
    @Transactional(rollbackFor = ValidationException.class)
    public DelimitedTextClinicalSourceConfiguration loadClinicalAnnotation(Long studyConfigurationId,
            Long clinicalSourceId)
        throws ValidationException, InvalidFieldDescriptorException {
        StudyConfiguration studyConfiguration = getRefreshedStudyConfiguration(studyConfigurationId);
        DelimitedTextClinicalSourceConfiguration clinicalSourceConfiguration 
                                                    = studyConfiguration.getClinicalSource(clinicalSourceId);
        if (validateAnnotationFieldDescriptors(studyConfiguration, 
                clinicalSourceConfiguration.getAnnotationDescriptors(), EntityTypeEnum.SUBJECT)) {
            clinicalSourceConfiguration.loadAnnotation();
            save(studyConfiguration);
            return clinicalSourceConfiguration;
        } else {
            throw new InvalidFieldDescriptorException(
                "Unable to load clinical source due to invalid values being loaded.  " 
                    + "Check the annotations on the edit screen for more details.");
        }
    }

    private boolean validateAnnotationFieldDescriptors(StudyConfiguration studyConfiguration,
            Collection<AnnotationFieldDescriptor> descriptors, EntityTypeEnum entityType)
    throws ValidationException {
        boolean isValid = true;
        for (AnnotationFieldDescriptor descriptor : descriptors) {
            populatePermissibleValues(studyConfiguration.getStudy(), entityType, descriptor);
            AnnotationDefinition definition = descriptor.getDefinition();
            if (definition != null && !definition.getPermissibleValueCollection().isEmpty()) {
                try {
                    validateAnnotationDefinition(descriptor, studyConfiguration.getStudy(), 
                        entityType, definition);
                    if (descriptor.isHasValidationErrors()) {
                        makeFieldDescriptorValid(descriptor);
                    }
                } catch (ValidationException e) {
                    isValid = false;
                    descriptor.setHasValidationErrors(true);
                    String invalidMessage = e.getResult().getInvalidMessage();
                    descriptor.setValidationErrorMessage(invalidMessage.length() >= MAX_ERROR_MESSAGE_LENGTH
                            ? invalidMessage.substring(0, MAX_ERROR_MESSAGE_LENGTH - 1) : invalidMessage);
                    daoSave(descriptor);
                }
            }
        }
        return isValid;
    }

    private void populatePermissibleValues(Study study, EntityTypeEnum entityType,
            AnnotationFieldDescriptor descriptor) throws ValidationException {
        if (descriptor.isUsePermissibleValues() && AnnotationFieldType.ANNOTATION.equals(descriptor.getType())
                && descriptor.getDefinition().getPermissibleValueCollection().isEmpty()) {
            Set<Object> uniqueValues = validateAndRetrieveUniqueValues(study, entityType, 
                    descriptor, descriptor.getDefinition());
            descriptor.getDefinition().addPermissibleValues(uniqueValues);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void makeFieldDescriptorValid(AnnotationFieldDescriptor descriptor) {
        descriptor.setHasValidationErrors(false);
        descriptor.setValidationErrorMessage(null);
        daoSave(descriptor);
    }

    /**
     * {@inheritDoc}
     */
    public StudyConfiguration reLoadClinicalAnnotation(Long studyConfigurationId) throws ValidationException {
        StudyConfiguration studyConfiguration = getRefreshedStudyConfiguration(studyConfigurationId);
        deleteClinicalAnnotation(studyConfiguration);
        for (AbstractClinicalSourceConfiguration configuration 
                : studyConfiguration.getClinicalConfigurationCollection()) {
            configuration.reLoadAnnotation();
        }
        getDao().removeObjects(studyConfiguration.removeObsoleteSubjectAssignment());
        save(studyConfiguration);
        return studyConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public void unloadAllClinicalAnnotation(StudyConfiguration studyConfiguration) {
        deleteClinicalAnnotation(studyConfiguration);
        for (AbstractClinicalSourceConfiguration configuration 
                : studyConfiguration.getClinicalConfigurationCollection()) {
            configuration.unloadAnnotation();
        }
        if (studyConfiguration.isDeployed()) {
            studyConfiguration.setStatus(Status.NOT_DEPLOYED);
        }
        getDao().removeObjects(studyConfiguration.removeObsoleteSubjectAssignment());
        save(studyConfiguration);
    }

    private void deleteClinicalAnnotation(StudyConfiguration studyConfiguration) {
        for (StudySubjectAssignment subjectAssignment : studyConfiguration.getStudy().getAssignmentCollection()) {
            deleteSubjectAnnotations(subjectAssignment);
            deleteSampleAcquisitions(subjectAssignment);
            subjectAssignment.getImageStudyCollection().clear();
        }
        deleteSampleMappingFiles(studyConfiguration);
        deleteImagingMappingFiles(studyConfiguration);
    }

    private void deleteSubjectAnnotations(StudySubjectAssignment subjectAssignment) {
        for (SubjectAnnotation subjectAnnotation : subjectAssignment.getSubjectAnnotationCollection()) {
            subjectAnnotation.removeValueFromDefinition();
            getDao().delete(subjectAnnotation);
        }
        subjectAssignment.getSubjectAnnotationCollection().clear();
    }

    private void deleteSampleAcquisitions(StudySubjectAssignment subjectAssignment) {
        for (SampleAcquisition sampleAcquisition : subjectAssignment.getSampleAcquisitionCollection()) {
            if (sampleAcquisition.getTimepoint() != null) {
                sampleAcquisition.getTimepoint().getSampleAcquisitionCollection().clear();
            }
            sampleAcquisition.getSample().setSampleAcquisition(null);
            for (AbstractAnnotationValue annotationValue : sampleAcquisition.getAnnotationCollection()) {
                annotationValue.setSampleAcquisition(null);
            }
        }
        getDao().removeObjects(subjectAssignment.getSampleAcquisitionCollection());
        subjectAssignment.getSampleAcquisitionCollection().clear();
    }

    private void deleteSampleMappingFiles(StudyConfiguration studyConfiguration) {
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration
                : studyConfiguration.getGenomicDataSources()) {
            genomicDataSourceConfiguration.deleteSampleMappingFile();
        }
    }

    private void deleteImagingMappingFiles(StudyConfiguration studyConfiguration) {
        for (ImageDataSourceConfiguration imageDataSourceConfiguration
                : studyConfiguration.getImageDataSources()) {
            imageDataSourceConfiguration.deleteMappingFile();
        }
    }

    /**
     * {@inheritDoc}
     */
    public StudyConfiguration deleteClinicalSource(Long studyConfigurationId,
            Long clinicalSourceId) throws ValidationException {
        StudyConfiguration studyConfiguration = getRefreshedStudyConfiguration(studyConfigurationId);
        DelimitedTextClinicalSourceConfiguration clinicalSourceConfiguration 
                                                    = studyConfiguration.getClinicalSource(clinicalSourceId);
        studyConfiguration.setStatus(Status.NOT_DEPLOYED);
        studyConfiguration.getClinicalConfigurationCollection().remove(clinicalSourceConfiguration);
        getDao().delete(clinicalSourceConfiguration);
        save(studyConfiguration);
        return reLoadClinicalAnnotation(studyConfiguration.getId());
    }
    
    /**
     * {@inheritDoc}
     */
    public void delete(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        studyConfiguration.getGenomicDataSources().remove(genomicSource);
        if (genomicSource.isCopyNumberData()) {
            Set<GisticAnalysis> gisticAnalysisCollection = 
                getDao().getGisticAnalysisUsingGenomicSource(genomicSource);
            for (GisticAnalysis gisticAnalysis : gisticAnalysisCollection) {
                analysisService.deleteGisticAnalysis(gisticAnalysis);
            }
        }
        for (Sample sample : genomicSource.getSamples()) {
            sample.removeSampleAcquisitionAssociations();
            for (Array array : sample.getArrayCollection()) {
                array.getSampleCollection().remove(sample);
                if (array.getSampleCollection().isEmpty()) {
                    getDao().delete(array);
                }
            }
            sample.clearArrayData();
        }
        getDao().refresh(genomicSource); // refresh to prevent hibernate resave error
        getDao().delete(genomicSource);
    }
    
    /**
     * {@inheritDoc}
     */
    public void delete(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource) 
        throws ValidationException {
        studyConfiguration.getImageDataSources().remove(imageSource);
        getDao().delete(imageSource);
        
        reLoadImageAnnotation(studyConfiguration);
    }
    
    /**
     * {@inheritDoc}
     */
    public void delete(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList) {
        studyConfiguration.getExternalLinkLists().remove(externalLinkList);
        getDao().delete(externalLinkList);
    }
    
    private void deleteImageAnnotation(StudyConfiguration studyConfiguration) {
        Study study = studyConfiguration.getStudy();
        for (StudySubjectAssignment studySubjectAssignment : study.getAssignmentCollection()) {
            for (ImageSeriesAcquisition imageSeriesAcquisition : studySubjectAssignment.getImageStudyCollection()) {
                getDao().delete(imageSeriesAcquisition);
            }
            studySubjectAssignment.getImageStudyCollection().clear();
        }
    }
    
    private void reLoadImageAnnotation(StudyConfiguration studyConfiguration) throws ValidationException {
        deleteImageAnnotation(studyConfiguration);
        for (ImageDataSourceConfiguration configuration 
                : studyConfiguration.getImageDataSources()) {
            if (configuration.getImageAnnotationConfiguration() != null) {
                configuration.getImageAnnotationConfiguration().reLoadAnnontation();
            }
        }
        getDao().removeObjects(studyConfiguration.removeObsoleteSubjectAssignment());
        save(studyConfiguration);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {ValidationException.class, IOException.class })
    public void mapSamples(StudyConfiguration studyConfiguration, File mappingFile, 
            GenomicDataSourceConfiguration genomicSource)
        throws ValidationException, IOException {
        unmapSamples(genomicSource);
        new SampleMappingHelper(studyConfiguration, mappingFile, genomicSource).mapSamples();
        if (!Status.LOADED.equals(genomicSource.getStatus())
                || !ArrayDataLoadingTypeEnum.PARSED_DATA.equals(genomicSource.getLoadingType())) {
            genomicSource.setStatus(genomicSource.getMappedSamples().isEmpty()
                ? Status.NOT_MAPPED : Status.READY_FOR_LOAD);
            studyConfiguration.setStatus(Status.NOT_DEPLOYED);
        }
        save(studyConfiguration);
    }
    
    private void unmapSamples(GenomicDataSourceConfiguration genomicSource) {
        for (Sample sample : genomicSource.getSamples()) {
            sample.removeSampleAcquisitionAssociations();
            if (!ArrayDataLoadingTypeEnum.PARSED_DATA.equals(genomicSource.getLoadingType())
                    || PlatformDataTypeEnum.COPY_NUMBER.equals(genomicSource.getDataType())) {
                for (Array array : sample.getArrayCollection()) {
                    array.getSampleCollection().remove(sample);
                    if (array.getSampleCollection().isEmpty()) {
                        getDao().delete(array);
                    }
                }
                sample.clearArrayData();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addControlSampleSet(GenomicDataSourceConfiguration genomicSource,
            String controlSampleSetName, File controlSampleFile, String controlSampleFileName)
            throws ValidationException, IOException {
        new ControlSampleHelper(genomicSource, controlSampleFile).addControlSamples(controlSampleSetName,
                controlSampleFileName);
        save(genomicSource.getStudyConfiguration());
    }
    
    /**
     * {@inheritDoc}
     */
    public void addGenomicSource(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource)
        throws ConnectionException, ExperimentNotFoundException {
        addGenomicSourceToStudy(studyConfiguration, genomicSource);
        loadGenomicSource(genomicSource);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addGenomicSourceToStudy(StudyConfiguration studyConfiguration, 
                                        GenomicDataSourceConfiguration genomicSource) {
        studyConfiguration.getGenomicDataSources().add(genomicSource);
        genomicSource.setStudyConfiguration(studyConfiguration);
        daoSave(studyConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    public void loadGenomicSource(GenomicDataSourceConfiguration genomicSource) 
    throws ConnectionException, ExperimentNotFoundException {
        if (ArrayDataLoadingTypeEnum.PARSED_DATA.equals(genomicSource.getLoadingType())
                || genomicSource.isExpressionData()) {
            loadSamples(genomicSource);
        }
        if (!ArrayDataLoadingTypeEnum.PARSED_DATA.equals(genomicSource.getLoadingType())) {
            checkSupplementalFiles(genomicSource);
        }
        genomicSource.setStatus(Status.NOT_MAPPED);
        daoSave(genomicSource);
    }

    private void loadSamples(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
            ExperimentNotFoundException {
        List<Sample> samples = getCaArrayFacade().getSamples(genomicSource.getExperimentIdentifier(), 
                genomicSource.getServerProfile());
        if (samples.isEmpty()) {
            throw new SamplesNotFoundException(
                    "No samples found for this caArray experiment (verify that sample data is accessible in caArray)");
        }
        genomicSource.setSamples(samples);
        for (Sample sample : samples) {
            sample.setGenomicDataSource(genomicSource);
        }
    }
    
    private void checkSupplementalFiles(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
    ExperimentNotFoundException {
        String errorMessage = 
            "No samples found for this caArray experiment (verify that sample data is accessible in caArray)";
        try {
            if (getCaArrayFacade().retrieveFilesForGenomicSource(genomicSource).isEmpty()) {
                throw new DnaAnalysisFilesNotFoundException(errorMessage);
            }
        } catch (FileNotFoundException e) {
            throw new DnaAnalysisFilesNotFoundException(errorMessage, e);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public GenomicDataSourceConfiguration getRefreshedGenomicSource(Long id) {
        GenomicDataSourceConfiguration genomicSource = new GenomicDataSourceConfiguration();
        genomicSource.setId(id);
        genomicSource = getRefreshedEntity(genomicSource);
        HibernateUtil.loadCollection(genomicSource.getStudyConfiguration());
        return genomicSource;
    }
    
    /**
     * {@inheritDoc}
     */
    public StudyConfiguration getRefreshedSecureStudyConfiguration(String username, Long id) 
    throws CSSecurityException {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(id);
        studyConfiguration = getRefreshedEntity(studyConfiguration);
        Set<StudyConfiguration> managedStudyConfigurations = new HashSet<StudyConfiguration>();
        try {
            managedStudyConfigurations = 
                securityManager.retrieveManagedStudyConfigurations(username, getDao().getStudies(username)); 
        } catch (CSException e) {
            throw new IllegalStateException("Error retrieving CSM data from SecurityManager.");
        }
        if (!managedStudyConfigurations.contains(studyConfiguration)) {
            throw new CSSecurityException("User doesn't have access to this study.");
        } 
        return studyConfiguration;
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
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<AnnotationDefinition> getMatchingDefinitions(List<String> keywords) {
        return getDao().findMatches(keywords);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public List<CommonDataElement> getMatchingDataElements(List<String> keywords) {
        return caDSRFacade.retreiveCandidateDataElements(keywords);
    }

    /**
     * @return the caDSRFacade
     */
    public CaDSRFacade getCaDSRFacade() {
        return caDSRFacade;
    }

    /**
     * @param caDSRFacade the caDSRFacade to set
     */
    public void setCaDSRFacade(CaDSRFacade caDSRFacade) {
        this.caDSRFacade = caDSRFacade;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.ExcessiveMethodLength")
    @Transactional(rollbackFor = {ConnectionException.class, ValidationException.class })
    public void setDataElement(AnnotationFieldDescriptor fieldDescriptor, 
                                CommonDataElement dataElement, 
                                Study study, 
                                EntityTypeEnum entityType,
                                String keywords) 
    throws ConnectionException, ValidationException {
        if (dataElement.getValueDomain() == null) {
            retrieveValueDomain(dataElement);
        }
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.setCommonDataElement(dataElement);
        addDefinitionToStudy(fieldDescriptor, study, entityType, annotationDefinition);
        if (dataElement.getDefinition().length() > DEFINITION_LENGTH) {
            dataElement.setDefinition(dataElement.getDefinition().substring(0, DEFINITION_LENGTH - 7) + "...");
        }
        annotationDefinition.setKeywords(dataElement.getLongName());
        daoSave(annotationDefinition);
        validateAnnotationDefinition(fieldDescriptor, study, entityType, annotationDefinition);
        daoSave(fieldDescriptor);
    }

    private void validateAnnotationDefinition(AnnotationFieldDescriptor fieldDescriptor, 
            Study study, EntityTypeEnum entityType,
            AnnotationDefinition annotationDefinition) throws ValidationException {
        Set<Object> uniqueValues = validateAndRetrieveUniqueValues(study, entityType, 
                fieldDescriptor, annotationDefinition); 
        if (!annotationDefinition.getPermissibleValueCollection().isEmpty()) {
            validateValuesWithPermissibleValues(uniqueValues, annotationDefinition);
        }
    }
    
    private Set<Object> validateAndRetrieveUniqueValues(Study study, EntityTypeEnum entityType, 
            AnnotationFieldDescriptor fieldDescriptor, 
            AnnotationDefinition annotationDefinition) throws ValidationException {
        AnnotationTypeEnum annotationType = annotationDefinition.getDataType();
        if (annotationType == null) {
            throw new IllegalArgumentException("Data Type for the Annotation Definition is unknown.");
        }
        Set<Object> valueObjects = new HashSet<Object>();
        List<FileColumn> fileColumns = getDao().getFileColumnsUsingAnnotationFieldDescriptor(fieldDescriptor);
        for (FileColumn fileColumn : fileColumns) {
            valueObjects.addAll(retrieveAndValidateValuesForFileColumn(study, entityType, annotationDefinition, 
                    annotationType, fileColumn));
        }
        return valueObjects;
    }

    @SuppressWarnings("unchecked") // For the "class" type.
    private Set<Object> retrieveAndValidateValuesForFileColumn(Study study, EntityTypeEnum entityType,
            AnnotationDefinition annotationDefinition, AnnotationTypeEnum annotationType,
            FileColumn fileColumn)
            throws ValidationException {
        if (Boolean.valueOf(fileColumn.getAnnotationFile().getCurrentlyLoaded())) {
            annotationDefinition.validateValuesWithType();
            return new HashSet<Object>(getDao().retrieveUniqueValuesForStudyAnnotation(study, annotationDefinition, 
                    entityType, annotationType.getClassType()));
        } else {
            return fileColumn.getUniqueDataValues(annotationType.getClassType());
        }
    }
    
    private void validateValuesWithPermissibleValues(Set<Object> uniqueValues, 
            AnnotationDefinition annotationDefinition) throws ValidationException {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setValid(true);
        Set<String> invalidValues =  
                PermissibleValueUtil.retrieveValuesNotPermissible(uniqueValues, annotationDefinition);
        if (!invalidValues.isEmpty()) {
            StringBuffer message = new StringBuffer();
            message.append("The following values exist that are NOT permissible for '" 
                            + annotationDefinition.getDisplayName() + "': {");
            for (String invalidValue : invalidValues) {
                message.append(" '" + invalidValue + "' ");
            }
            message.append("} Please select a different Data Element.");
            validationResult.setValid(false);
            validationResult.setInvalidMessage(message.toString());
            throw new ValidationException(validationResult);
        }
    }

    private void retrieveValueDomain(CommonDataElement dataElement)
            throws ConnectionException {
        ValueDomain valueDomain;
        String dataElementVersion = dataElement.getVersion(); 
        valueDomain = caDSRFacade.retrieveValueDomainForDataElement(dataElement.getPublicID(), 
                                                    NumberUtils.isNumber(dataElementVersion) 
                                                    ? Float.valueOf(dataElementVersion) : null);
        dataElement.setValueDomain(valueDomain);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = ValidationException.class)
    public void setDefinition(Study study, AnnotationFieldDescriptor fieldDescriptor, 
            AnnotationDefinition annotationDefinition, EntityTypeEnum entityType) throws ValidationException {
        if (fieldDescriptor.getDefinition() == null 
            || !fieldDescriptor.getDefinition().equals(annotationDefinition)) {
            addDefinitionToStudy(fieldDescriptor, study, entityType, annotationDefinition);
            validateAnnotationDefinition(fieldDescriptor, study, entityType, annotationDefinition);
            
            daoSave(annotationDefinition);
            daoSave(fieldDescriptor);
            daoSave(study);
        }
    }
    
    /**
     * @return the nciaFacade
     */
    public NCIAFacade getNciaFacade() {
        return nciaFacade;
    }

    /**
     * @param nciaFacade the nciaFacade to set
     */
    public void setNciaFacade(NCIAFacade nciaFacade) {
        this.nciaFacade = nciaFacade;
    }

    /**
     * @return the aimFacade
     */
    public AIMFacade getAimFacade() {
        return aimFacade;
    }

    /**
     * @param aimFacade the aimFacade to set
     */
    public void setAimFacade(AIMFacade aimFacade) {
        this.aimFacade = aimFacade;
    }

    /**
     * @return the caArrayFacade
     */
    public CaArrayFacade getCaArrayFacade() {
        return caArrayFacade;
    }

    /**
     * @param caArrayFacade the caArrayFacade to set
     */
    public void setCaArrayFacade(CaArrayFacade caArrayFacade) {
        this.caArrayFacade = caArrayFacade;
    }

    /**
     * {@inheritDoc}
     */
    public File saveFileToStudyDirectory(StudyConfiguration studyConfiguration, File file) throws IOException {
        return fileManager.storeStudyFile(file, file.getName(), studyConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    public ImageAnnotationConfiguration addImageAnnotationFile(
            ImageDataSourceConfiguration imageDataSourceConfiguration,
            File inputFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException {
        File permanentFile = getFileManager().storeStudyFile(inputFile, filename,
                imageDataSourceConfiguration.getStudyConfiguration());
        AnnotationFile annotationFile = AnnotationFile.load(permanentFile, getDao(), 
                imageDataSourceConfiguration.getStudyConfiguration(),
                EntityTypeEnum.IMAGESERIES, createNewAnnotationDefinition);
        ImageAnnotationConfiguration imageAnnotationConfiguration = 
            new ImageAnnotationConfiguration(annotationFile, imageDataSourceConfiguration);
        imageAnnotationConfiguration.setImageDataSourceConfiguration(imageDataSourceConfiguration);
        imageDataSourceConfiguration.setImageAnnotationConfiguration(imageAnnotationConfiguration);
        imageDataSourceConfiguration.setStatus(retrieveImageSourceStatus(imageDataSourceConfiguration));
        daoSave(imageDataSourceConfiguration.getStudyConfiguration());
        return imageAnnotationConfiguration;
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageAnnotationConfiguration addAimAnnotationSource(ServerConnectionProfile aimConnection, 
            ImageDataSourceConfiguration imageSource) {
        ImageAnnotationConfiguration annotationConfiguration = new ImageAnnotationConfiguration();
        annotationConfiguration.setUploadType(ImageAnnotationUploadType.AIM);
        annotationConfiguration.setAimServerProfile(aimConnection);
        annotationConfiguration.setImageDataSourceConfiguration(imageSource);
        imageSource.setImageAnnotationConfiguration(annotationConfiguration);
        imageSource.setStatus(retrieveImageSourceStatus(imageSource));
        daoSave(imageSource.getStudyConfiguration());
        return annotationConfiguration;
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, 
            rollbackFor = {ConnectionException.class, ValidationException.class })
    // Using Propagation.NOT_SUPPORTED because when it was being run it wouldn't always save the 
    // AnnotationValue.AnnotationDefinition or AnnotationValue.ImageSeries, so when it's outside 
    // of the transaction and using a daoSave it correctly saves the objects
    public void loadAimAnnotations(ImageDataSourceConfiguration imageSource) 
        throws ConnectionException, ValidationException {
        List<ImageSeries> imageSeriesCollection = new ArrayList<ImageSeries>();
        for (ImageSeriesAcquisition imageSeriesAcquisition : imageSource.getImageSeriesAcquisitions()) {
            for (ImageSeries imageSeries : imageSeriesAcquisition.getSeriesCollection()) {
                imageSeriesCollection.add(imageSeries);
            }
        }
        Map<ImageSeries, ImageSeriesAnnotationsWrapper> imageSeriesAnnotationsMap = aimFacade
                .retrieveImageSeriesAnnotations(imageSource.getImageAnnotationConfiguration().getAimServerProfile(),
                        imageSeriesCollection);
        createAnnotationValuesForImageSeries(imageSource, imageSeriesAnnotationsMap);
        imageSource.setStatus(Status.LOADED);
        daoSave(imageSource.getStudyConfiguration());
    }

    private void createAnnotationValuesForImageSeries(ImageDataSourceConfiguration imageSource,
            Map<ImageSeries, ImageSeriesAnnotationsWrapper> imageSeriesAnnotationsMap) throws ValidationException {
        for (ImageSeries imageSeries : imageSeriesAnnotationsMap.keySet()) {
            ImageSeriesAnnotationsWrapper imageSeriesAnnotations = imageSeriesAnnotationsMap.get(imageSeries);
            for (String groupName : imageSeriesAnnotations.getAnnotationGroupNames()) {
                for (String definitionName : imageSeriesAnnotations.getAnnotationDefinitions(groupName)) {
                    String value = imageSeriesAnnotations.getAnnotationValueForGroupDefinition(groupName,
                            definitionName);
                    AnnotationFieldDescriptor annotationDescriptor = AnnotationUtil.retrieveOrCreateFieldDescriptor(
                            getDao(), imageSource.getStudyConfiguration(), EntityTypeEnum.IMAGESERIES, true,
                            definitionName, groupName);
                    AbstractAnnotationValue annotationValue = 
                        AnnotationUtil.createAnnotationValue(annotationDescriptor, value);
                    daoSave(annotationDescriptor.getAnnotationGroup());
                    daoSave(annotationDescriptor);
                    annotationValue.setImageSeries(imageSeries);
                    imageSeries.getAnnotationCollection().add(annotationValue);
                    daoSave(imageSeries);
                    daoSave(annotationValue);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource) 
        throws ConnectionException, InvalidImagingCollectionException {
        addImageSourceToStudy(studyConfiguration, imageSource);
        loadImageSource(imageSource);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addImageSourceToStudy(StudyConfiguration studyConfiguration, 
            ImageDataSourceConfiguration imageSource) {
        imageSource.setStudyConfiguration(studyConfiguration);
        studyConfiguration.getImageDataSources().add(imageSource);
        daoSave(imageSource);
        daoSave(studyConfiguration);
    }
    
    /**
     * {@inheritDoc} 
     */
    public void loadImageSource(ImageDataSourceConfiguration imageSource) 
        throws ConnectionException, InvalidImagingCollectionException {
        List<ImageSeriesAcquisition> acquisitions = getNciaFacade().getImageSeriesAcquisitions(
                imageSource.getCollectionName(), imageSource.getServerProfile());
        imageSource.getImageSeriesAcquisitions().addAll(acquisitions);
        for (ImageSeriesAcquisition acquisition : acquisitions) {
            acquisition.setImageDataSource(imageSource);
        }
        imageSource.setStatus(imageSource.getMappedImageSeriesAcquisitions().isEmpty()
                ? Status.NOT_MAPPED : Status.LOADED);
        daoSave(imageSource);
    }

    /**
     * {@inheritDoc}
     */
    public void loadImageAnnotation(ImageDataSourceConfiguration imageDataSource) throws ValidationException {
        ImageAnnotationConfiguration imageAnnotationConfiguration = imageDataSource.getImageAnnotationConfiguration();
        if (validateAnnotationFieldDescriptors(imageDataSource.getStudyConfiguration(), 
                imageAnnotationConfiguration.getAnnotationDescriptors(), EntityTypeEnum.IMAGESERIES)) {
            imageAnnotationConfiguration.loadAnnontation();
            imageDataSource.setStatus(retrieveImageSourceStatus(imageDataSource));
            daoSave(imageDataSource);
        } else {
            throw new ValidationException("Unable to load image source due to invalid values being loaded.  " 
                + "Check the annotations on the edit screen for more details.");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void mapImageSeriesAcquisitions(StudyConfiguration studyConfiguration, 
            ImageDataSourceConfiguration imageSource, File mappingFile, ImageDataSourceMappingTypeEnum mappingType)
        throws ValidationException, IOException {
        new ImageSeriesAcquisitionMappingHelper(studyConfiguration, mappingFile, 
                mappingType, imageSource).mapImageSeries();
        imageSource.setStatus(retrieveImageSourceStatus(imageSource));
        daoSave(imageSource);
    }
    
    /**
     * {@inheritDoc}
     */
    public void updateImageDataSourceStatus(StudyConfiguration studyConfiguration) {
        for (ImageDataSourceConfiguration imageSource : studyConfiguration.getImageDataSources()) {
            if (Status.PROCESSING.equals(imageSource.getStatus())) {
                continue;
            }
            Status status = retrieveImageSourceStatus(imageSource);
            if (imageSource.getStatus() != status) {
                imageSource.setStatus(status);
                daoSave(imageSource);
            }
        }
    }

    private Status retrieveImageSourceStatus(ImageDataSourceConfiguration imageSource) {
        if (imageSource.getMappedImageSeriesAcquisitions().isEmpty()) {
            return Status.NOT_MAPPED;
        }
        ImageAnnotationConfiguration annotationConfiguration = imageSource.getImageAnnotationConfiguration();
        if (annotationConfiguration != null) {
            if (annotationConfiguration.isLoadable() 
                 && !annotationConfiguration.isCurrentlyLoaded()) {
                return Status.NOT_LOADED;
            } else if (annotationConfiguration.isCurrentlyLoaded()) {
                return Status.LOADED;
            } else if (!annotationConfiguration.isLoadable()) {
                return Status.DEFINITION_INCOMPLETE;
            }
        } 
        return Status.LOADED;
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageDataSourceConfiguration getRefreshedImageSource(Long id) {
        ImageDataSourceConfiguration imagingSource = new ImageDataSourceConfiguration();
        imagingSource.setId(id);
        imagingSource = getRefreshedEntity(imagingSource);
        HibernateUtil.loadCollection(imagingSource.getStudyConfiguration());
        return imagingSource;
    }
    
    /**
     * {@inheritDoc}
     */
    public StudyConfiguration getRefreshedStudyConfiguration(Long id) {
        StudyConfiguration studyConfiguration = new StudyConfiguration();
        studyConfiguration.setId(id);
        studyConfiguration = getRefreshedEntity(studyConfiguration);
        HibernateUtil.loadCollection(studyConfiguration);
        return studyConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public AnnotationDefinition createDefinition(AnnotationFieldDescriptor descriptor, 
                                                 Study study, 
                                                 EntityTypeEnum entityType,
                                                 AnnotationTypeEnum annotationType) throws ValidationException {
        AnnotationDefinition annotationDefinition = new AnnotationDefinition();
        annotationDefinition.getCommonDataElement().setLongName(descriptor.getName());
        annotationDefinition.getCommonDataElement().getValueDomain().setDataType(annotationType);
        annotationDefinition.setKeywords(annotationDefinition.getDisplayName());
        addDefinitionToStudy(descriptor, study, entityType, annotationDefinition);
        daoSave(annotationDefinition);
        daoSave(descriptor);
        daoSave(study);
        return annotationDefinition;
    }
    
    @SuppressWarnings({ "PMD.ExcessiveMethodLength" }) // Switch Statement and null checks
    private void addDefinitionToStudy(AnnotationFieldDescriptor descriptor, Study study, EntityTypeEnum entityType,
            AnnotationDefinition annotationDefinition) throws ValidationException {
        AnnotationDefinition annotationDefinitionToRemove = null;
        if (descriptor.getDefinition() != null) {
            annotationDefinitionToRemove = descriptor.getDefinition();
            moveValuesToNewDefinition(study, annotationDefinition, annotationDefinitionToRemove);
            if (EntityTypeEnum.SUBJECT.equals(entityType)) {
                moveDefinitionInSurvivalDefinitions(study, annotationDefinitionToRemove, annotationDefinition);
            }
        }
        descriptor.setDefinition(annotationDefinition);
        descriptor.setAnnotationEntityType(entityType);
    }

    /**
     * Moves AbstractAnnotationValues from one AnnotationDefinition to another.
     * @param study - Study that the values belong to.
     * @param annotationDefinition - new AnnotationDefinition where the Values will belong.
     * @param annotationDefinitionToRemove - Old AnnotationDefinition.
     * @throws ValidationException if unable to move old values to new definition.
     */
    private void moveValuesToNewDefinition(Study study, AnnotationDefinition annotationDefinition,
            AnnotationDefinition annotationDefinitionToRemove) throws ValidationException {
        if (annotationDefinitionToRemove.getAnnotationValueCollection() != null 
            && !annotationDefinitionToRemove.getAnnotationValueCollection().isEmpty()) {
            List<AbstractAnnotationValue> valuesToConvert = new ArrayList<AbstractAnnotationValue>();
            for (AbstractAnnotationValue value : annotationDefinitionToRemove.getAnnotationValueCollection()) {
                if (studyContainsAnnotationValue(value, study)) {
                    valuesToConvert.add(value); // To not get a ConcurrentModificationException.
                }
            }
            for (AbstractAnnotationValue valueToConvert : valuesToConvert) {
                valueToConvert.convertAnnotationValue(annotationDefinition);
            }
        }
    }
    
    private boolean studyContainsAnnotationValue(AbstractAnnotationValue value, Study study) {
        if (value.getSubjectAnnotation() != null 
                && study.equals(value.getSubjectAnnotation().getStudySubjectAssignment().getStudy())) {
            return true;
        } else if (value.getImageSeries() != null 
                && study.equals(value.getImageSeries().getImageStudy().
                        getImageDataSource().getStudyConfiguration().getStudy())) {
            return true;
        } else if (value.getSampleAcquisition() != null
                && study.equals(value.getSampleAcquisition().getAssignment().getStudy())) { 
            return true;
        } else if (value.getImage() != null 
                && study.equals(value.getImage().getSeries().getImageStudy().
                        getImageDataSource().getStudyConfiguration().getStudy())) {
            return true;
        }
        return false;
    }
    
    private void moveDefinitionInSurvivalDefinitions(Study study, 
            AnnotationDefinition oldDefinition, AnnotationDefinition newDefinition) {
        for (SurvivalValueDefinition definition : study.getSurvivalValueDefinitionCollection()) {
            if (oldDefinition.equals(definition.getSurvivalStartDate())) {
                definition.setSurvivalStartDate(newDefinition);
            }
            if (oldDefinition.equals(definition.getLastFollowupDate())) {
                definition.setLastFollowupDate(newDefinition);
            }
            if (oldDefinition.equals(definition.getDeathDate())) {
                definition.setDeathDate(newDefinition);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void addExternalLinksToStudy(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList) 
        throws ValidationException, IOException {
        ExternalLinksLoader.loadLinks(externalLinkList);
        studyConfiguration.getExternalLinkLists().add(externalLinkList);
        daoSave(studyConfiguration);
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

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = true)
    public boolean isDuplicateStudyName(Study study, String username) {
        return getDao().isDuplicateStudyName(study, username);
    }
    
    /**
     * {@inheritDoc}
     */
    public void removeSurvivalValueDefinition(Study study, SurvivalValueDefinition survivalValueDefinition) {
       study.getSurvivalValueDefinitionCollection().remove(survivalValueDefinition);
       Collection <SurvivalValueDefinition> objectsToRemove = new HashSet<SurvivalValueDefinition>();
       objectsToRemove.add(survivalValueDefinition);
       getDao().removeObjects(objectsToRemove);
       daoSave(study);
    }

    /**
     * {@inheritDoc}
     */
    public ImageDataSourceConfiguration retrieveImageDataSource(Study study) {
        ImageDataSourceConfiguration dataSource = getDao().retrieveImagingDataSourceForStudy(study);
        if (dataSource != null) {
            Hibernate.initialize(dataSource.getServerProfile());
        }
        return dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {IOException.class, ValidationException.class })
    public void saveDnaAnalysisMappingFile(GenomicDataSourceConfiguration genomicSource,
            File mappingFile, String filename) throws IOException, ValidationException {
        File savedFile = getFileManager().storeStudyFile(mappingFile, filename, genomicSource.getStudyConfiguration());
        if (genomicSource.getDnaAnalysisDataConfiguration() == null) {
            genomicSource.setDnaAnalysisDataConfiguration(new DnaAnalysisDataConfiguration());
        } else {
            unmapSamples(genomicSource);
        }
        if (ArrayDataLoadingTypeEnum.PARSED_DATA.equals(genomicSource.getLoadingType())) {
            mapSamples(genomicSource.getStudyConfiguration(), mappingFile, genomicSource);
        }
        genomicSource.getDnaAnalysisDataConfiguration().setMappingFilePath(savedFile.getAbsolutePath());
        genomicSource.setStatus(Status.READY_FOR_LOAD);
        daoSave(genomicSource);
    }

    /**
     * {@inheritDoc}
     */
    public void saveSampleMappingFile(GenomicDataSourceConfiguration source,
            File mappingFile, String filename) throws IOException {
        File savedFile = getFileManager().storeStudyFile(mappingFile, filename, source.getStudyConfiguration());
        source.setSampleMappingFilePath(savedFile.getAbsolutePath());
        daoSave(source);
    }

    /**
     * @param securityManager the securityManager to set
     */
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

   /**
    * {@inheritDoc}
    */
    public void setStudyLastModifiedByCurrentUser(StudyConfiguration studyConfiguration, UserWorkspace lastModifiedBy,
            TimeStampable timeStampedStudyObject, String systemLogMessage) {
        Date lastModifiedDate = new Date();
        studyConfiguration.setLastModifiedBy(lastModifiedBy);
        studyConfiguration.setLastModifiedDate(lastModifiedDate);
        if (timeStampedStudyObject != null) {
            timeStampedStudyObject.setLastModifiedDate(lastModifiedDate);
            daoSave(timeStampedStudyObject);
        }
        if (!StringUtils.isBlank(systemLogMessage)) {
            LogEntry logEntry = new LogEntry();
            logEntry.setUsername(lastModifiedBy == null ? null : lastModifiedBy.getUsername());
            logEntry.setLogDate(lastModifiedDate);
            logEntry.setTrimSystemLogMessage(systemLogMessage);
            studyConfiguration.getLogEntries().add(logEntry);
        }
        daoSave(studyConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = {ConnectionException.class, ValidationException.class })
    public void saveAnnotationGroup(AnnotationGroup annotationGroup,
            StudyConfiguration studyConfiguration, File annotationGroupFile)
    throws ValidationException, ConnectionException, IOException {
        if (annotationGroup.getStudy() != studyConfiguration.getStudy()) {
            annotationGroup.setStudy(studyConfiguration.getStudy());
            studyConfiguration.getStudy().getAnnotationGroups().add(annotationGroup);
        }
        if (annotationGroupFile != null) {
            uploadAnnotationGroup(studyConfiguration, annotationGroup, annotationGroupFile);
        }
        daoSave(annotationGroup);
        daoSave(studyConfiguration);
    }
    
    private void uploadAnnotationGroup(StudyConfiguration studyConfiguration,
            AnnotationGroup annotationGroup, File uploadFile)
    throws ConnectionException, ValidationException, IOException {
        AnnotationGroupUploadFileHandler uploadFileHandler = new AnnotationGroupUploadFileHandler(
                studyConfiguration, uploadFile);
        List<AnnotationGroupUploadContent> uploadContents = uploadFileHandler.extractUploadData();
        if (uploadContents != null) {
            StringBuffer validationMsg = new StringBuffer();
            for (AnnotationGroupUploadContent uploadContent : uploadContents) {
                try {
                    createAnnotation(annotationGroup, uploadContent);
                } catch (ValidationException e) {
                    validationMsg.append(e.getMessage());
                }
            }
            if (validationMsg.length() > 0) {
                throw new ValidationException(validationMsg.toString());
            }
        }
    }
        
    private void createAnnotation(AnnotationGroup annotationGroup, AnnotationGroupUploadContent uploadContent)
    throws ConnectionException, ValidationException {
        AnnotationFieldDescriptor annotationFieldDescriptor = uploadContent.createAnnotationFieldDescriptor();
        annotationFieldDescriptor.setAnnotationGroup(annotationGroup);
        if (!AnnotationFieldType.IDENTIFIER.equals(uploadContent.getAnnotationType())) {
            AnnotationDefinition annotationDefinition = createAnnotationDefinition(uploadContent);
            annotationFieldDescriptor.setDefinition(annotationDefinition);
        }
        annotationGroup.getAnnotationFieldDescriptors().add(annotationFieldDescriptor);
    }

    private AnnotationDefinition createAnnotationDefinition(AnnotationGroupUploadContent uploadContent)
    throws ConnectionException, ValidationException {
        AnnotationDefinition annotationDefinition = getDao().getAnnotationDefinition(
                uploadContent.getDefinitionName(), uploadContent.getDataType());
        if (annotationDefinition == null) {
            if (uploadContent.getCdeId() != null) {
                annotationDefinition = getCaDsrAnnotationDefinition(uploadContent.getCdeId(),
                    uploadContent.getVersion());
                annotationDefinition.setKeywords(uploadContent.getDefinitionName());
            } else {
                annotationDefinition = uploadContent.createAnnotationDefinition();
            }
        }
        return annotationDefinition;
    }
    
    private AnnotationDefinition getCaDsrAnnotationDefinition(Long cdeId, Float version)
    throws ConnectionException, ValidationException {
        AnnotationDefinition annotationDefinition = getDao().getAnnotationDefinition(
                cdeId, version);
        if (annotationDefinition == null) {
            annotationDefinition = new AnnotationDefinition();
            annotationDefinition.setCommonDataElement(retrieveDataElement(
                    cdeId, version));
            retrieveValueDomain(annotationDefinition.getCommonDataElement());
            
        }
        return annotationDefinition;
    }

    private CommonDataElement retrieveDataElement(Long dataElementId, Float version)
    throws ConnectionException, ValidationException {
        CommonDataElement commonDataElement = caDSRFacade.retrieveDataElement(dataElementId, version);
        if (commonDataElement == null) {
            throw new ValidationException("Error cdeId not found: " + dataElementId.toString());
        }
        return commonDataElement;
    }

    /**
     * {@inheritDoc}
     */
    public void delete(StudyConfiguration studyConfiguration, AnnotationGroup annotationGroup) {
        studyConfiguration.getStudy().getAnnotationGroups().remove(annotationGroup);
        for (AnnotationFieldDescriptor afd : annotationGroup.getAnnotationFieldDescriptors()) {
            List<ResultColumn> columnsUsingFieldDescriptor = getDao().getResultColumnsUsingAnnotation(afd);
            List<AbstractAnnotationCriterion> criterionUsingFieldDescriptor = getDao().getCriteriaUsingAnnotation(afd);
            for (ResultColumn column : columnsUsingFieldDescriptor) {
                column.getQuery().getColumnCollection().remove(column);
                getDao().delete(column);
            }
            for (AbstractAnnotationCriterion criterion : criterionUsingFieldDescriptor) {
                criterion.setAnnotationFieldDescriptor(null);
                getDao().save(criterion);
            }
        }
        getDao().delete(annotationGroup);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = ValidationException.class)
    public AnnotationFieldDescriptor updateFieldDescriptorType(AnnotationFieldDescriptor fieldDescriptor, 
            AnnotationFieldType type) throws ValidationException {
        AnnotationFieldDescriptor returnFieldDescriptor = fieldDescriptor;
        for (FileColumn fileColumn : getDao().getFileColumnsUsingAnnotationFieldDescriptor(fieldDescriptor)) {
            if (AnnotationFieldType.IDENTIFIER.equals(type)) {
                fileColumn.checkValidIdentifierColumn();
                fileColumn.getAnnotationFile().setIdentifierColumn(fileColumn);
                returnFieldDescriptor = fileColumn.getFieldDescriptor();
                daoSave(fileColumn);
            } else if (AnnotationFieldType.TIMEPOINT.equals(type)) {
                fileColumn.getAnnotationFile().setTimepointColumn(fileColumn);
                returnFieldDescriptor = fileColumn.getFieldDescriptor();
                daoSave(fileColumn);
            }
        }
        
        returnFieldDescriptor.setType(type);
        daoSave(returnFieldDescriptor);
        return returnFieldDescriptor;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.EmptyCatchBlock") // See message inside catch block.
    public Set<String> getAvailableValuesForFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor) 
    throws ValidationException {
        Set<String> allAvailableValues = new HashSet<String>();
        allAvailableValues.addAll(AnnotationUtil.getAdditionalValue(fieldDescriptor.getDefinition()
                .getAnnotationValueCollection(), new ArrayList<String>(), PermissibleValueUtil
                .getDisplayPermissibleValue(fieldDescriptor.getDefinition().getPermissibleValueCollection())));
        for (FileColumn fileColumn : getDao().getFileColumnsUsingAnnotationFieldDescriptor(fieldDescriptor)) {
            List<String> fileDataValues = fileColumn.getAnnotationFile() != null ? fileColumn.getDataValues()
                    : new ArrayList<String>();
            if (AnnotationTypeEnum.DATE.equals(fieldDescriptor.getDefinition().getDataType())) {
                try {
                    fileDataValues = DateUtil.toString(fileDataValues);
                } catch (ParseException e) {
                    // noop - if it doesn't fit the date format just let it keep going.
                    // This function is for JSP display so it can't fail.
                }
            }
            allAvailableValues.addAll(AnnotationUtil.getAdditionalValue(fieldDescriptor.getDefinition()
                    .getAnnotationValueCollection(), fileDataValues, PermissibleValueUtil
                    .getDisplayPermissibleValue(fieldDescriptor.getDefinition().getPermissibleValueCollection())));
        }
        return allAvailableValues;
    }

    /**
     * {@inheritDoc}
     */
    public void daoSave(Object persistentObject) {
        getDao().save(persistentObject);
    }

    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }
}
