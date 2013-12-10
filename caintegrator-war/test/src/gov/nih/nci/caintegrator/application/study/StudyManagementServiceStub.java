/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.cadsr.freestylesearch.util.SearchException;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.TimeStampable;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.genomic.Sample;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSSecurityException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudyManagementServiceStub implements StudyManagementService {

    public boolean saveCalled;
    public boolean copyCalled;
    public boolean deleteCalled;
    public boolean addGenomicSourceCalled;
    public boolean addAuthorizedStudyElementsGroupCalled;
    public boolean deleteAuthorizedStudyElementsGroupCalled;
    public boolean addClinicalAnnotationFileCalled;
    public boolean getRefreshedStudyEntityCalled;
    public boolean getMatchingDefinitionsCalled;
    public boolean getMatchingDataElementsCalled;
    public boolean setDataElementCalled;
    public boolean setDefinitionCalled;
    public boolean loadClinicalAnnotationCalled;
    public boolean reLoadClinicalAnnotationCalled;
    public boolean unLoadClinicalAnnotationCalled;
    public boolean loadAimAnnotationsCalled;
    public boolean mapSamplesCalled;
    public boolean addImageSourceCalled;
    public boolean addImageAnnotationFileCalled;
    public boolean loadImageAnnotationCalled;
    public boolean mapImageSeriesCalled;
    public boolean createDefinitionCalled;
    public boolean addControlSampleSetCalled;
    public boolean isDuplicateStudyNameCalled;
    public boolean addStudyLogoCalled;
    public boolean retrieveStudyLogoCalled;
    public boolean createNewSurvivalValueDefinitionCalled;
    public boolean removeSurvivalValueDefinitionCalled;
    public boolean throwSearchError;
    public boolean retrieveImageDataSourceCalled;
    public boolean saveDnaAnalysisMappingFileCalled;
    public boolean saveSampleMappingFileCalled;
    public boolean loadGenomicSourceCalled;
    public boolean throwConnectionException = false;
    public boolean throwValidationException = false;
    public boolean throwIOException = false;
    public boolean getRefreshedImageSourceCalled;
    public boolean addImageSourceToStudyCalled;
    public boolean loadImageSourceCalled;
    public boolean saveFileStoStudyDirectoryCalled;
    public boolean updateImageDataSourceStatusCalled;
    public boolean getRefreshedStudyConfigurationCalled;
    public boolean getRefreshedGenomicSourceCalled;
    public boolean isThrowCSException = false;
    public boolean addExternalLinksToStudyCalled;
    public boolean saveAnnotationGroupCalled = false;
    public boolean daoSaveCalled = false;
    public boolean checkForSampleUpdates = false;

    public ImageDataSourceConfiguration refreshedImageSource = new ImageDataSourceConfiguration();
    public GenomicDataSourceConfiguration refreshedGenomicSource = new GenomicDataSourceConfiguration();
    public DelimitedTextClinicalSourceConfiguration refreshedClinicalSource =
        new DelimitedTextClinicalSourceConfiguration();
    public StudyConfiguration refreshedStudyConfiguration = new StudyConfiguration();


    @Override
    public DelimitedTextClinicalSourceConfiguration loadClinicalAnnotation(Long studyConfigurationId,
            Long clinicalSourceConfigurationId)
        throws ValidationException {
        loadClinicalAnnotationCalled = true;
        if (refreshedStudyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
        refreshedClinicalSource.setStatus(Status.LOADED);
        return refreshedClinicalSource;
    }

    @Override
    public StudyConfiguration reLoadClinicalAnnotation(Long studyConfigurationId)
        throws ValidationException {
        reLoadClinicalAnnotationCalled = true;
        if (refreshedStudyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
        return refreshedStudyConfiguration;
    }

    @Override
    public void save(StudyConfiguration studyConfiguration) {
        saveCalled = true;
    }

    @Override
    public void delete(StudyConfiguration studyConfiguration) {
        deleteCalled = true;
    }

    @Override
    public StudyConfiguration deleteClinicalSource(Long studyConfigurationId,
            Long clinicalSourceId) throws ValidationException {
        deleteCalled = true;
        if (refreshedStudyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
        return refreshedStudyConfiguration;
    }

    public void delete(Collection<PermissibleValue> abstractPermissibleValues) {
        deleteCalled = true;
    }

    public void clear() {
        copyCalled = false;
        loadClinicalAnnotationCalled = false;
        reLoadClinicalAnnotationCalled = false;
        unLoadClinicalAnnotationCalled = false;
        saveCalled = false;
        addClinicalAnnotationFileCalled = false;
        addGenomicSourceCalled = false;
        getRefreshedStudyEntityCalled = false;
        getMatchingDefinitionsCalled = false;
        getMatchingDataElementsCalled = false;
        setDataElementCalled = false;
        setDefinitionCalled = false;
        mapSamplesCalled = false;
        addImageAnnotationFileCalled = false;
        addImageSourceCalled = false;
        loadImageAnnotationCalled = false;
        mapImageSeriesCalled = false;
        createDefinitionCalled = false;
        addControlSampleSetCalled = false;
        isDuplicateStudyNameCalled = false;
        addStudyLogoCalled = false;
        retrieveStudyLogoCalled = false;
        createNewSurvivalValueDefinitionCalled = false;
        removeSurvivalValueDefinitionCalled = false;
        throwSearchError = false;
        saveDnaAnalysisMappingFileCalled = false;
        saveSampleMappingFileCalled = false;
        retrieveImageDataSourceCalled = false;
        loadGenomicSourceCalled = false;
        throwConnectionException = false;
        throwValidationException = false;
        throwIOException = false;
        getRefreshedImageSourceCalled = false;
        addImageSourceToStudyCalled = false;
        loadImageSourceCalled = false;
        loadAimAnnotationsCalled = false;
        saveFileStoStudyDirectoryCalled = false;
        updateImageDataSourceStatusCalled = false;
        getRefreshedStudyConfigurationCalled = false;
        getRefreshedGenomicSourceCalled = false;
        isThrowCSException = false;
        addExternalLinksToStudyCalled = false;
        saveAnnotationGroupCalled = false;
        daoSaveCalled = false;
        checkForSampleUpdates = false;
    }

    @Override
    public void addGenomicSource(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        addGenomicSourceCalled = true;
        studyConfiguration.getGenomicDataSources().add(genomicSource);
    }

    @Override
    public DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration,
            File annotationFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException {
        if (TestDataFiles.INVALID_FILE_MISSING_VALUE.equals(annotationFile)) {
            throw new ValidationException(new ValidationResult());
        } else if (TestDataFiles.INVALID_FILE_DOESNT_EXIST.equals(annotationFile)) {
            throw new IOException();
        }
        addClinicalAnnotationFileCalled = true;
        return new DelimitedTextClinicalSourceConfiguration();
    }

    @Override
    public <T extends AbstractCaIntegrator2Object> T getRefreshedEntity(T entity) {
        getRefreshedStudyEntityCalled = true;
        return entity;
    }

    @Override
    public List<AnnotationDefinition> getMatchingDefinitions(List<String> keywords) {
        getMatchingDefinitionsCalled = true;
        return Collections.emptyList();
    }

    @Override
    public List<CommonDataElement> getMatchingDataElements(List<String> keywords) {
        getMatchingDataElementsCalled = true;
        if (throwSearchError) {
            throw new SearchException("Bad Search");
        }
        return Collections.emptyList();
    }

    @Override
    public void setDataElement(AnnotationFieldDescriptor fieldDescriptor, CommonDataElement dataElement, Study study, EntityTypeEnum entityType, String keywords) throws ValidationException, ConnectionException {
        setDataElementCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    @Override
    public void setDefinition(Study study, AnnotationFieldDescriptor fieldDescriptor, AnnotationDefinition annotationDefinition,
                                EntityTypeEnum entityType) throws ValidationException {
        setDefinitionCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    @Override
    public void mapSamples(StudyConfiguration studyConfiguration, File mappingFile, GenomicDataSourceConfiguration genomicSource)throws ValidationException, IOException {
        mapSamplesCalled = true;
        if (studyConfiguration.getStudy().getShortTitleText().equals("Invalid")) {
            throw new ValidationException("Invalid");
        }
        if (studyConfiguration.getStudy().getShortTitleText().equals("IOException")) {
            throw new IOException("Invalid");
        }
    }

    @Override
    public ImageAnnotationConfiguration addImageAnnotationFile(ImageDataSourceConfiguration imageDataSourceConfiguration,
            File annotationFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException {
        if (TestDataFiles.INVALID_FILE_MISSING_VALUE.equals(annotationFile)) {
            throw new ValidationException(new ValidationResult());
        } else if (TestDataFiles.INVALID_FILE_DOESNT_EXIST.equals(annotationFile)) {
            throw new IOException();
        }
        addImageAnnotationFileCalled = true;
        ImageAnnotationConfiguration imageAnnotationConfiguration = new ImageAnnotationConfiguration();
        imageAnnotationConfiguration.setAnnotationFile(new AnnotationFileStub());
        return imageAnnotationConfiguration;
    }

    @Override
    public void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
            throws ConnectionException {
        addImageSourceCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
    }

    @Override
    public void loadImageAnnotation(ImageDataSourceConfiguration imageSource) throws ValidationException {
        loadImageAnnotationCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    @Override
    public void mapImageSeriesAcquisitions(StudyConfiguration studyConfiguration,
            ImageDataSourceConfiguration imageSource, File mappingFile, ImageDataSourceMappingTypeEnum mappingType)
        throws ValidationException, IOException {
        mapImageSeriesCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
        if (throwIOException) {
            throw new IOException();
        }
    }


    @Override
    public AnnotationDefinition createDefinition(AnnotationFieldDescriptor descriptor, Study study, EntityTypeEnum entityType, AnnotationTypeEnum annotationType) {
        AnnotationDefinition definition = new AnnotationDefinition();
        if (descriptor != null) {
            descriptor.setDefinition(definition);
        }
        if (annotationType != null) {
            definition.getCommonDataElement().getValueDomain().setDataType(annotationType);
        }
        createDefinitionCalled = true;
        return definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addControlSampleSet(GenomicDataSourceConfiguration genomicSource,
            String controlSampleSetName, File controlSampleFile, String controlFileName)
            throws ValidationException {
        SampleSet sampleSet = new SampleSet();
        sampleSet.setName(controlSampleSetName);
        sampleSet.getSamples().add(new Sample());
        genomicSource.getControlSampleSetCollection().add(sampleSet);
        addControlSampleSetCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDuplicateStudyName(Study study, String username) {
        isDuplicateStudyNameCalled = true;
        return study.getShortTitleText().equals("Duplicate");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addStudyLogo(StudyConfiguration studyConfiguration, File imageFile, String fileName, String fileType) throws IOException {
        addStudyLogoCalled = true;

    }

    @Override
    public StudyLogo retrieveStudyLogo(Long id, String name) {
        retrieveStudyLogoCalled = true;
        return new StudyLogo();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.caintegrator.application.study.StudyManagementService#createNewSurvivalValueDefinition(gov.nih.nci.caintegrator.domain.translational.Study)
     */
    public SurvivalValueDefinition createNewSurvivalValueDefinition(Study study) {
        createNewSurvivalValueDefinitionCalled = true;
        return new SurvivalValueDefinition();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.caintegrator.application.study.StudyManagementService#removeSurvivalValueDefinition(gov.nih.nci.caintegrator.domain.translational.Study, gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition)
     */
    @Override
    public void removeSurvivalValueDefinition(Study study, SurvivalValueDefinition survivalValueDefinition) {
        removeSurvivalValueDefinitionCalled = true;
    }

    @Override
    public ImageDataSourceConfiguration retrieveImageDataSource(Study study) {
        retrieveImageDataSourceCalled = true;
        return new ImageDataSourceConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveDnaAnalysisMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) {
        saveDnaAnalysisMappingFileCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSampleMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) throws IOException {
        saveSampleMappingFileCalled = true;
    }

    @Override
    public void delete(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        deleteCalled = true;
    }

    @Override
    public void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException {

    }

    @Override
    public void delete(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
            throws ValidationException {
        deleteCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    @Override
    public void addGenomicSourceToStudy(StudyConfiguration studyConfiguration,
            GenomicDataSourceConfiguration genomicSource) {
    }

    @Override
    public void loadGenomicSource(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
            ExperimentNotFoundException {
        loadGenomicSourceCalled = true;
    }

    @Override
    public void addImageSourceToStudy(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource) {
        addImageSourceToStudyCalled = true;
    }

    @Override
    public void loadImageSource(ImageDataSourceConfiguration imageSource) throws ConnectionException {
        loadImageSourceCalled = true;
    }

    @Override
    public File saveFileToStudyDirectory(StudyConfiguration studyConfiguration, File file) throws IOException {
        saveFileStoStudyDirectoryCalled = true;
        return null;
    }

    @Override
    public void updateImageDataSourceStatus(StudyConfiguration studyConfiguration) {
        updateImageDataSourceStatusCalled = true;
    }

    @Override
    public ImageDataSourceConfiguration getRefreshedImageSource(Long id) {
        getRefreshedImageSourceCalled = true;
        return refreshedImageSource;
    }

    @Override
    public GenomicDataSourceConfiguration getRefreshedGenomicSource(Long id) {
        getRefreshedGenomicSourceCalled = true;
        return refreshedGenomicSource;
    }

    @Override
    public StudyConfiguration getRefreshedSecureStudyConfiguration(String username, Long id)
    throws CSSecurityException {
        getRefreshedStudyConfigurationCalled = true;
        if (isThrowCSException) {
            throw new CSSecurityException("invalid");
        }
        return refreshedStudyConfiguration;
    }

    @Override
    public void save(AnnotationDefinition definition) throws ValidationException {

    }

    @Override
    public void setStudyLastModifiedByCurrentUser(StudyConfiguration studyConfiguration, UserWorkspace lastModifiedBy, TimeStampable object, String systemLogMessage) {

    }

    @Override
    public void addExternalLinksToStudy(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList)
            throws ValidationException, IOException {
        if (throwValidationException) {
            throw new ValidationException("");
        }
        if (throwIOException) {
            throw new IOException();
        }
        addExternalLinksToStudyCalled = true;
    }

    @Override
    public void delete(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList) {
        deleteCalled = true;

    }

    @Override
    public void saveAnnotationGroup(AnnotationGroup annotationGroup, StudyConfiguration studyConfiguration,
            File annotationGroupFile) throws ValidationException {
        if (throwValidationException) {
            throw new ValidationException("");
        }
        saveCalled = true;

    }

    @Override
    public void delete(StudyConfiguration studyConfiguration, AnnotationGroup annotationGroup) {
        deleteCalled = true;
    }

    public void saveAnnotationGroup(AnnotationGroup annotationGroup, StudyConfiguration studyConfiguration,
            List<AnnotationGroupUploadContent> uploadContents) throws ValidationException, ConnectionException {
        saveAnnotationGroupCalled = true;
    }

    @Override
    public AnnotationFieldDescriptor updateFieldDescriptorType(AnnotationFieldDescriptor fieldDescriptor, AnnotationFieldType type)
            throws ValidationException {
        fieldDescriptor.setType(type);
        return fieldDescriptor;
    }

    @Override
    public Set<String> getAvailableValuesForFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor)
            throws ValidationException {
        return new HashSet<String>();
    }

    @Override
    public void makeFieldDescriptorValid(AnnotationFieldDescriptor descriptor) {

    }

    @Override
    public void daoSave(Object persistentObject) {
        daoSaveCalled = true;
    }

    public AnnotationDefinition getAnnotationDefinition(String name) {
        return null;
    }

    public AnnotationFieldDescriptor getExistingFieldDescriptorInStudy(String name,
            StudyConfiguration studyConfiguration) {
        return null;
    }

    @Override
    public void unloadAllClinicalAnnotation(StudyConfiguration studyConfiguration) {
        unLoadClinicalAnnotationCalled = true;
    }

    @Override
    public ImageAnnotationConfiguration addAimAnnotationSource(ServerConnectionProfile aimConnection,
            ImageDataSourceConfiguration imageSource) {
        return null;
    }

    @Override
    public void loadAimAnnotations(Long imageSourceId)
    throws ConnectionException, ValidationException {
        loadAimAnnotationsCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    @Override
    public StudyConfiguration getRefreshedStudyConfiguration(Long id) {
        getRefreshedStudyConfigurationCalled = true;
        return refreshedStudyConfiguration;
    }

    @Override
    public void saveSubjectSourceStatus(AbstractClinicalSourceConfiguration source) {

    }

    @Override
    public StudyConfiguration copy(StudyConfiguration copyFrom, StudyConfiguration copyTo)
        throws ValidationException, IOException,
            ConnectionException {
        copyCalled = true;
        return copyTo;
    }

    @Override
    public void addAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
                                                AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        addAuthorizedStudyElementsGroupCalled = true;
        studyConfiguration.getAuthorizedStudyElementsGroups().add(authorizedStudyElementsGroup);
    }

    @Override
    public void deleteAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
                                        AuthorizedStudyElementsGroup authorizedStudyElementsGroup) {
        deleteAuthorizedStudyElementsGroupCalled = true;
    }


    @Override
    public void createProtectionElement(StudyConfiguration studyConfiguration,
                                        AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException {
    }

    @Override
    public void checkForSampleUpdates(GenomicDataSourceConfiguration sc) throws ConnectionException,
    ExperimentNotFoundException {
        checkForSampleUpdates = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AnnotationFieldDescriptor> getVisibleAnnotationFieldDescriptorsForUser(AnnotationGroup annotationGroup,
            String username) {
        return annotationGroup != null ? annotationGroup.getVisibleAnnotationFieldDescriptors()
                : new HashSet<AnnotationFieldDescriptor>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AuthorizedStudyElementsGroup> getAuthorizedStudyElementsGroups(String username, Long studyConfigId) {
        return new ArrayList<AuthorizedStudyElementsGroup>();
    }
}
