/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.cadsr.freestylesearch.util.SearchException;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSSecurityException;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("PMD")
public class StudyManagementServiceStub implements StudyManagementService {

    public boolean saveCalled;
    public boolean deleteCalled;
    public boolean addGenomicSourceCalled;
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
    
    public ImageDataSourceConfiguration refreshedImageSource = new ImageDataSourceConfiguration();
    public GenomicDataSourceConfiguration refreshedGenomicSource = new GenomicDataSourceConfiguration();
    public StudyConfiguration refreshedStudyConfiguration = new StudyConfiguration();


    public void loadClinicalAnnotation(StudyConfiguration studyConfiguration,
            AbstractClinicalSourceConfiguration clinicalSourceConfiguration)
        throws ValidationException {
        loadClinicalAnnotationCalled = true;
        if (studyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
    }

    public void reLoadClinicalAnnotation(StudyConfiguration studyConfiguration)
        throws ValidationException {
        reLoadClinicalAnnotationCalled = true;
        if (studyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
    }

    public void save(StudyConfiguration studyConfiguration) {
        saveCalled = true;
    }

    public void delete(StudyConfiguration studyConfiguration) {
        deleteCalled = true;
    }

    public void delete(StudyConfiguration studyConfiguration,
            AbstractClinicalSourceConfiguration clinicalSource) throws ValidationException {
        deleteCalled = true;
        if (studyConfiguration.getStudy().getShortTitleText().equalsIgnoreCase("Invalid")) {
            throw new ValidationException(new ValidationResult());
        }
    }

    public void delete(Collection<PermissibleValue> abstractPermissibleValues) {
        deleteCalled = true;
    }

    public void clear() {
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
    }

    public void addGenomicSource(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        addGenomicSourceCalled = true;
        studyConfiguration.getGenomicDataSources().add(genomicSource);
    }

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

    public <T> T getRefreshedEntity(T entity) {
        getRefreshedStudyEntityCalled = true;
        return entity;
    }

    public List<AnnotationDefinition> getMatchingDefinitions(List<String> keywords) {
        getMatchingDefinitionsCalled = true;
        return Collections.emptyList();
    }

    public List<CommonDataElement> getMatchingDataElements(List<String> keywords) {
        getMatchingDataElementsCalled = true;
        if (throwSearchError) {
            throw new SearchException("Bad Search");
        }
        return Collections.emptyList();
    }

    public void setDataElement(AnnotationFieldDescriptor fieldDescriptor, CommonDataElement dataElement, Study study, EntityTypeEnum entityType, String keywords) throws ValidationException, ConnectionException {
        setDataElementCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public void setDefinition(Study study, AnnotationFieldDescriptor fieldDescriptor, AnnotationDefinition annotationDefinition, 
                                EntityTypeEnum entityType) throws ValidationException {
        setDefinitionCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public void mapSamples(StudyConfiguration studyConfiguration, File mappingFile, GenomicDataSourceConfiguration genomicSource)throws ValidationException, IOException {
        mapSamplesCalled = true;
        if (studyConfiguration.getStudy().getShortTitleText().equals("Invalid")) {
            throw new ValidationException("Invalid");
        }
        if (studyConfiguration.getStudy().getShortTitleText().equals("IOException")) {
            throw new IOException("Invalid");
        }
    }
    
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

    public void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
            throws ConnectionException {
        addImageSourceCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
    }

    public void loadImageAnnotation(ImageDataSourceConfiguration imageSource) throws ValidationException {
        loadImageAnnotationCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

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
    public boolean isDuplicateStudyName(Study study, String username) {
        isDuplicateStudyNameCalled = true;
        return study.getShortTitleText().equals("Duplicate");
    }

    /**
     * {@inheritDoc}
     */
    public void addStudyLogo(StudyConfiguration studyConfiguration, File imageFile, String fileName, String fileType) throws IOException {
        addStudyLogoCalled = true;
        
    }
    
    public StudyLogo retrieveStudyLogo(Long id, String name) {
        retrieveStudyLogoCalled = true;
        return new StudyLogo();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.caintegrator2.application.study.StudyManagementService#createNewSurvivalValueDefinition(gov.nih.nci.caintegrator2.domain.translational.Study)
     */
    public SurvivalValueDefinition createNewSurvivalValueDefinition(Study study) {
        createNewSurvivalValueDefinitionCalled = true;
        return new SurvivalValueDefinition();
    }

    /* (non-Javadoc)
     * @see gov.nih.nci.caintegrator2.application.study.StudyManagementService#removeSurvivalValueDefinition(gov.nih.nci.caintegrator2.domain.translational.Study, gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition)
     */
    public void removeSurvivalValueDefinition(Study study, SurvivalValueDefinition survivalValueDefinition) {
        removeSurvivalValueDefinitionCalled = true;
    }

    public ImageDataSourceConfiguration retrieveImageDataSource(Study study) {
        retrieveImageDataSourceCalled = true;
        return new ImageDataSourceConfiguration();
    }

    /**
     * {@inheritDoc}
     */
    public void saveDnaAnalysisMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) {
        saveDnaAnalysisMappingFileCalled = true;
    }

    /**
     * {@inheritDoc}
     */
    public void saveSampleMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) throws IOException {
        saveSampleMappingFileCalled = true;   
    }

    public void delete(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource) {
        deleteCalled = true;
    }

    public void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException {
        
    }

    public void delete(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
            throws ValidationException {
        deleteCalled = true;
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

    public void addGenomicSourceToStudy(StudyConfiguration studyConfiguration,
            GenomicDataSourceConfiguration genomicSource) {
    }

    public void loadGenomicSource(GenomicDataSourceConfiguration genomicSource) throws ConnectionException,
            ExperimentNotFoundException {
        loadGenomicSourceCalled = true;
    }

    public void addImageSourceToStudy(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource) {
        addImageSourceToStudyCalled = true;
    }

    public void loadImageSource(ImageDataSourceConfiguration imageSource) throws ConnectionException {
        loadImageSourceCalled = true;
    }

    public File saveFileToStudyDirectory(StudyConfiguration studyConfiguration, File file) throws IOException {
        saveFileStoStudyDirectoryCalled = true;
        return null;
    }

    public void updateImageDataSourceStatus(StudyConfiguration studyConfiguration) {
        updateImageDataSourceStatusCalled = true;
    }

    public ImageDataSourceConfiguration getRefreshedImageSource(Long id) {
        getRefreshedImageSourceCalled = true;
        return refreshedImageSource;
    }

    public GenomicDataSourceConfiguration getRefreshedGenomicSource(Long id) {
        getRefreshedGenomicSourceCalled = true;
        return refreshedGenomicSource;
    }

    public StudyConfiguration getRefreshedSecureStudyConfiguration(String username, Long id) 
    throws CSSecurityException {
        getRefreshedStudyConfigurationCalled = true;
        if (isThrowCSException) {
            throw new CSSecurityException("invalid");
        }
        return refreshedStudyConfiguration;
    }

    public void save(AnnotationDefinition definition) throws ValidationException {
        
    }

    public void setStudyLastModifiedByCurrentUser(StudyConfiguration studyConfiguration, UserWorkspace lastModifiedBy, TimeStampable object, String systemLogMessage) {
        
    }

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

    public void delete(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList) {
        deleteCalled = true;
        
    }

    public void saveAnnotationGroup(AnnotationGroup annotationGroup, StudyConfiguration studyConfiguration,
            File annotationGroupFile) throws ValidationException {
        if (throwValidationException) {
            throw new ValidationException("");
        }
        saveCalled = true;

    }

    public void delete(StudyConfiguration studyConfiguration, AnnotationGroup annotationGroup) {
        deleteCalled = true;
    }

    public void saveAnnotationGroup(AnnotationGroup annotationGroup, StudyConfiguration studyConfiguration,
            List<AnnotationGroupUploadContent> uploadContents) throws ValidationException, ConnectionException {
        saveAnnotationGroupCalled = true;
    }

    public AnnotationFieldDescriptor updateFieldDescriptorType(AnnotationFieldDescriptor fieldDescriptor, AnnotationFieldType type)
            throws ValidationException {
        fieldDescriptor.setType(type);
        return fieldDescriptor;
    }

    public Set<String> getAvailableValuesForFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor)
            throws ValidationException {
        return new HashSet<String>();
    }

    public void makeFieldDescriptorValid(AnnotationFieldDescriptor descriptor) {
        
    }

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

    public void unloadAllClinicalAnnotation(StudyConfiguration studyConfiguration) {
        unLoadClinicalAnnotationCalled = true;
    }

    public ImageAnnotationConfiguration addAimAnnotationSource(ServerConnectionProfile aimConnection,
            ImageDataSourceConfiguration imageSource) {
        return null;
    }

    public void loadAimAnnotations(ImageDataSourceConfiguration imageSource)
    throws ConnectionException, ValidationException {
        loadAimAnnotationsCalled = true;
        if (throwConnectionException) {
            throw new ConnectionException("");
        }
        if (throwValidationException) {
            throw new ValidationException("");
        }
    }

}
