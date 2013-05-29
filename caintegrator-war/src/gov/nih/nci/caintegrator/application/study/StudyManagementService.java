/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import gov.nih.nci.caintegrator.application.CaIntegrator2EntityRefresher;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.TimeStampable;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.InvalidImagingCollectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSSecurityException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Service used to create, define, deploy and update studies.
 */
public interface StudyManagementService extends CaIntegrator2EntityRefresher {

    /**
     * Saves a study.
     *
     * @param studyConfiguration study to save
     */
    void save(StudyConfiguration studyConfiguration);

    /**
     * Copy a study.
     * @param copyFrom original
     * @param copyTo new
     * @return study configuration
     * @throws IOException on error
     * @throws ValidationException on error
     * @throws ConnectionException on error
     */
    StudyConfiguration copy(StudyConfiguration copyFrom, StudyConfiguration copyTo) throws ValidationException,
        IOException, ConnectionException;

    /**
     * Saves the annotation definition.
     * @param definition to persist.
     * @throws ValidationException if values are invalid for the given type.
     */
    void save(AnnotationDefinition definition) throws ValidationException;

    /**
     * Saves the status/status description only for the given source.
     * @param source to save status for.
     */
    void saveSubjectSourceStatus(AbstractClinicalSourceConfiguration source);

    /**
     * Creates a protection element for the Study Configuration.
     * @param studyConfiguration to create protection element for.
     * @throws CSException if there's a problem creating the protection element.
     */
    void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException;

    /**
     * Creates a protection element for the AuthorizedStudyElementsGroup.
     * @param studyConfiguration to create protection element for.
     * @param authorizedStudyElementsGroup to create protection element for.
     * @throws CSException if there's a problem creating the protection element.
     */
    void createProtectionElement(StudyConfiguration studyConfiguration,
                                    AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException;


    /**
     * Deletes a study.
     *
     * @param studyConfiguration study to delete
     * @throws CSException if there's a problem deleting the protection element from CSM api.
     */
    void delete(StudyConfiguration studyConfiguration) throws CSException;

    /**
     * Deletes a clinical source.
     *
     * @param studyConfigurationId study configuration of the clinical source
     * @param clinicalSourceId clinical source to delete
     * @return the study configuration.
     * @throws ValidationException fail to reload
     */
    StudyConfiguration deleteClinicalSource(Long studyConfigurationId,
            Long clinicalSourceId) throws ValidationException;

    /**
     * Deletes a genomic source.
     * @param studyConfiguration of the clinical source.
     * @param genomicSource to delete.
     */
    void delete(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource);

    /**
     * Delete an imaging source.
     * @param studyConfiguration of the imaging source.
     * @param imageSource to delete.
     * @throws ValidationException fail to reload.
     */
    void delete(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
    throws ValidationException;

    /**
     * Deletes an externalLinkList.
     * @param studyConfiguration of the external links.
     * @param externalLinkList to delete.
     */
    void delete(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList);

    /**
     * Adds a clinical annotation file for use. The file given will be copied to permanent storage allowing the
     * file provided as an argument to be removed after completion of this method.
     *
     * @param studyConfiguration add the annotation file to this study
     * @param annotationFile annotation file to add.
     * @param filename the name with which the annotation file should be stored
     *        (allows for the use of files with temp names as input)
     * @param createNewAnnotationDefinition create new annotation definition when one is not found
     * @return the clinical source configuration created.
     * @throws ValidationException if the file was not a valid annotation file.
     * @throws IOException if the annotation file couldn't be copied to permanent storage.
     */
    DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration,
            File annotationFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException;

    /**
     * Adds a logo to the study.
     * @param studyConfiguration add the logo to this study.
     * @param imageFile object to add.
     * @param fileName name of the file for the logo.
     * @param fileType - type of file (such as image\jpeg).
     * @throws IOException if the image file couldn't be loaded.
     */
    void addStudyLogo(StudyConfiguration studyConfiguration,
                        File imageFile,
                        String fileName,
                        String fileType) throws IOException;

    /**
     * Retrieves study logo from the database given a study id and name.
     * @param studyId - ID of the Study object.
     * @param studyShortTitleText - Short Title Text of Study Object.
     * @return Object retrieved from database.
     */
    StudyLogo retrieveStudyLogo(Long studyId, String studyShortTitleText);

    /**
     * Adds an AuthorizedElementsGroup to the study configuration.
     * @param studyConfiguration study configuration to which the authoriziedStudyElementGroup will be added
     * @param authorizedStudyElementsGroup authoriziedStudyElementGroup that is being added
     * @throws CSException on error
     */
    void addAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
            AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException;

    /**
     * Deletes an AuthorizedElementsGroup from the study configuration.
     * @param studyConfiguration study configuration from which the authoriziedStudyElementGroup will be deleted
     * @param authorizedStudyElementsGroup authoriziedStudyElementGroup that is being deleted
     * @throws CSException on error
     */
    void deleteAuthorizedStudyElementsGroup(StudyConfiguration studyConfiguration,
            AuthorizedStudyElementsGroup authorizedStudyElementsGroup) throws CSException;

    /**
     * Loads a specific clinical annotation from study configuration.
     *
     * @param studyConfigurationId study configuration to load
     * @param clinicalSourceId clinical source configuration to load
     * @return the clinical source that is loaded.
     * @throws ValidationException fail to load
     * @throws InvalidFieldDescriptorException  if field descriptors are invalid.
     */
    DelimitedTextClinicalSourceConfiguration loadClinicalAnnotation(Long studyConfigurationId,
            Long clinicalSourceId)
        throws ValidationException, InvalidFieldDescriptorException;

    /**
     * ReLoads clinical annotations for study configuration.
     *
     * @param studyConfigurationId study configuration to load
     * @return studyConfiguration the study configuration.
     * @throws ValidationException fail to load
     */
    StudyConfiguration reLoadClinicalAnnotation(Long studyConfigurationId)
        throws ValidationException;

    /**
     * Unloads all clinical annotation from study configuration.
     *
     * @param studyConfiguration study configuration to unload
     */
    void unloadAllClinicalAnnotation(StudyConfiguration studyConfiguration);

    /**
     * Adds a new, initialized genomic data source to the study. Samples related to this data source are
     * retrieved from the source and added to the study.
     *
     * @param studyConfiguration study configuration to add genomic data source to
     * @param genomicSource genomic source to add
     * @throws ConnectionException if the configured server couldn't be reached.
     * @throws ExperimentNotFoundException if the experiment cannot be found.
     */
    void addGenomicSource(StudyConfiguration studyConfiguration, GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, ExperimentNotFoundException;


    /**
     * Adds a new genomic data source to the study.
     * @param studyConfiguration to add source to.
     * @param genomicSource to add.
     */
    void addGenomicSourceToStudy(StudyConfiguration studyConfiguration,
            GenomicDataSourceConfiguration genomicSource);

    /**
     * Samples related to this data source are retrieved from the source and added to the study.
     *
     * @param genomicSource genomic source to add
     * @throws ConnectionException if the configured server couldn't be reached.
     * @throws ExperimentNotFoundException if the experiment cannot be found.
     */
    void loadGenomicSource(GenomicDataSourceConfiguration genomicSource)
    throws ConnectionException, ExperimentNotFoundException;

    /**
     * Retrieves refreshed genomic source.
     * @param id of the genomic source.
     * @return refreshed genomic source.
     */
    GenomicDataSourceConfiguration getRefreshedGenomicSource(Long id);

    /**
     * Retrieves refreshed clinical source.
     * @param id of the clinical source.
     * @return refreshed clinical source.
     */
    StudyConfiguration getRefreshedStudyConfiguration(Long id);

    /**
     * Retrieves refreshed study configuration.
     * @param username of the user trying to access study.
     * @param id of the study configuration.
     * @return refreshed study configuration.
     * @throws CSSecurityException if user doesn't have access to this study.
     */
    StudyConfiguration getRefreshedSecureStudyConfiguration(String username, Long id)
    throws CSSecurityException;

    /**
     * Saves a file to the study directory.
     * @param studyConfiguration to get the directory.
     * @param file to save.
     * @return saved file.
     * @throws IOException if unable to read/save file.
     */
    File saveFileToStudyDirectory(StudyConfiguration studyConfiguration, File file) throws IOException;

    /**
     * Adds a new, initialized image data source to the study. The <code>ImageSeriesAcquisition</code> related to this
     * data source is retrieved from the source and added to the study.
     *
     * @param studyConfiguration study configuration to add image data source to
     * @param imageSource image source to add
     * @throws ConnectionException if the configured server couldn't be reached.
     * @throws InvalidImagingCollectionException if there are no images associated with collection.
     */
    void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource)
    throws ConnectionException, InvalidImagingCollectionException;

    /**
     * Adds a new image source to study.
     * @param studyConfiguration to add image source to.
     * @param imageSource to add to study.
     */
    void addImageSourceToStudy(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource);

    /**
     * Loads the image source.
     * @param imageSource to load.
     * @throws ConnectionException if can't connect to imaging server.
     * @throws InvalidImagingCollectionException if there are no images associated with collection.
     */
    void loadImageSource(ImageDataSourceConfiguration imageSource)
    throws ConnectionException, InvalidImagingCollectionException;


    /**
     * Adds an image series annotation file to the study. The file given will be copied to permanent storage
     * allowing the file provided as an argument to be removed after completion of this method.
     *
     * @param imageDataSourceConfiguration add the annotation file to this image data source
     * @param annotationFile annotation file to add.
     * @param filename the name with which the annotation file should be stored
     *        (allows for the use of files with temp names as input)
     * @param createNewAnnotationDefinition create new annotation definition when one is not found
     * @return the clinical source configuration created.
     * @throws ValidationException if the file was not a valid annotation file.
     * @throws IOException if the annotation file couldn't be copied to permanent storage.
     */
    ImageAnnotationConfiguration addImageAnnotationFile(ImageDataSourceConfiguration imageDataSourceConfiguration,
            File annotationFile, String filename, boolean createNewAnnotationDefinition)
    throws ValidationException, IOException;

    /**
     * Adds aim annotation source to the image source configuration.
     * @param aimConnection connection to aim.
     * @param imageSource imaging source.
     * @return annotation configuration.
     */
    ImageAnnotationConfiguration addAimAnnotationSource(ServerConnectionProfile aimConnection,
            ImageDataSourceConfiguration imageSource);

    /**
     * Loads image annotations given an image data source configuration.
     *
     * @param imageDataSource to load
     * @throws ValidationException fail to load
     */
    void loadImageAnnotation(ImageDataSourceConfiguration imageDataSource) throws ValidationException;

    /**
     * Loads the aim annotations.
     * @param imageSourceId image source to load annotations for.
     * @throws ConnectionException unable to connect to AIM service.
     * @throws ValidationException if unable to create annotation field descriptor.
     */
    void loadAimAnnotations(Long imageSourceId) throws ConnectionException, ValidationException;

    /**
     * Updates the status of the imaging sources.
     * @param studyConfiguration which contains the imaging sources.
     */
    void updateImageDataSourceStatus(StudyConfiguration studyConfiguration);

    /**
     * Returns the refreshed ImageDataSourceConfiguration attached to the current Hibernate session.
     * @param id of the image data source configuration.
     * @return refreshed entity.
     */
    ImageDataSourceConfiguration getRefreshedImageSource(Long id);

    /**
     * Returns an ordered list of existing definitions that match the keywords contained
     * in the given column.
     *
     * @param keywords match definitions for these keywords.
     * @return the list of matching candidate definitions.
     */
    List<AnnotationDefinition> getMatchingDefinitions(List<String> keywords);

    /**
     * Returns an ordered list of existing CaDSR data elements that match the keywords contained
     * in the given column.
     *
     * @param keywords match data elements for these keywords.
     * @return the list of matching candidate data elements.
     */
    List<CommonDataElement> getMatchingDataElements(List<String> keywords);

    /**
     * Selects an existing CaDSR data element as the definition for a column.

     * @param fieldDescriptor fieldDescriptor receiving definition.
     * @param dataElement the selected data element.
     * @param study the study that the FileColumn belongs to.
     * @param entityType the entityType for the data element.
     * @param keywords the keywords that were used to find this data element.
     * @throws ConnectionException if underlying data sources couldn't be reached.
     * @throws ValidationException if the data element selected is invalid for this definition.
     */
    void setDataElement(AnnotationFieldDescriptor fieldDescriptor, CommonDataElement dataElement,
            Study study, EntityTypeEnum entityType, String keywords)
    throws ConnectionException, ValidationException;

    /**
     * Selects an existing annotation definition for a column.
     *
     * @param study is the study that the definition is getting set for.
     * @param fieldDescriptor fieldDescriptor receiving definition.
     * @param annotationDefinition the selected definition.
     * @param entityType entity type for the annotation definition.
     * @throws ValidationException if invalid definition for the given values.
     */
    void setDefinition(Study study, AnnotationFieldDescriptor fieldDescriptor,
            AnnotationDefinition annotationDefinition, EntityTypeEnum entityType) throws ValidationException;

    /**
     * Create the associations between subjects in the study and samples.
     *
     * @param studyConfiguration study containing the subjects and samples
     * @param mappingFile comma-separated value file that maps subject identifiers to sample names
     * @param genomicSource to map samples for.
     * @throws ValidationException if the file was not a valid mapping file.
     * @throws IOException unexpected IO exception
     *
     */
    void mapSamples(StudyConfiguration studyConfiguration, File mappingFile,
            GenomicDataSourceConfiguration genomicSource)
        throws ValidationException, IOException;

    /**
     * Create the associations between subjects in the study and image series.
     *
     * @param studyConfiguration study containing the subjects and image series
     * @param mappingFile comma-separated value file that maps subject identifiers to image series identifiers
     * @param mappingType the type of mapping file it is.
     * @param imageSource the imageSource to map images.
     * @throws IOException unexpected IO exception
     * @throws ValidationException validation exception
     */
    void mapImageSeriesAcquisitions(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource,
            File mappingFile, ImageDataSourceMappingTypeEnum mappingType)
        throws ValidationException, IOException;

    /**
     * Creates a new AnnotationDefinition based on an AnnotationFieldDescriptor.
     * @param descriptor annotation descriptor to use.
     * @param study object to correlate the newly created definition to.
     * @param entityType entity type for the annotation definition.
     * @param annotationType data type for the annotation definition.
     * @return The annotation definition that was created.
     * @throws ValidationException if there's a problem creating definition.
     */
    AnnotationDefinition createDefinition(AnnotationFieldDescriptor descriptor, Study study, EntityTypeEnum entityType,
            AnnotationTypeEnum annotationType) throws ValidationException;

    /**
     * Adds the samples specified by identifier in the file to a new set of control samples in the
     * study.
     *
     * @param genomicSource add controls for this genomicSource
     * @param controlSampleSetName the controlSampleSet name
     * @param controlSampleFile file containing the sample identifiers, one per line
     * @param controlSampleFileName the controlSampleFile file name
     * @throws ValidationException if the file is invalid.
     * @throws IOException unexpected IO exception
     */
    void addControlSampleSet(GenomicDataSourceConfiguration genomicSource, String controlSampleSetName,
            File controlSampleFile, String controlSampleFileName)
        throws ValidationException, IOException;

    /**
     * Check for duplicate study name in the database.
     *
     * @param study
     *            the study object
     * @param username the user checking to see if it's a duplicate, because it is based
     * on that users UserGroup privileges.
     * @return true or false
     */
    boolean isDuplicateStudyName(Study study, String username);

    /**
     * Removes a survivalValueDefinition from a study and deletes the object.
     * @param study - Study to remove from.
     * @param survivalValueDefinition - Object to remove from study.
     */
    void removeSurvivalValueDefinition(Study study, SurvivalValueDefinition survivalValueDefinition);

    /**
     * Retrieves the image data source for a given study configuration. (If there's more than one,
     * it takes the first one found).
     * @param study to use for data source.
     * @return image data source configuration for the study.
     */
    ImageDataSourceConfiguration retrieveImageDataSource(Study study);

    /**
     * Associates a copy number mapping file to the given genomic data source.
     *
     * @param genomicDataSourceConfiguration  copy number data is associated to this source.
     * @param mappingFile the file containing the mapping of subjects to samples to copy number files.
     * @param filename the filename to save the file as.
     * @throws IOException if the file couldn't be saved.
     * @throws ValidationException if unable to map the samples.
     */
    void saveDnaAnalysisMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) throws IOException, ValidationException;

    /**
     * Associates a sample mapping file to the given genomic data source.
     *
     * @param genomicDataSourceConfiguration  copy number data is associated to this source.
     * @param mappingFile the file containing the mapping of subjects to samples to level2 data files.
     * @param filename the filename to save the file as.
     * @throws IOException if the file couldn't be saved.
     */
    void saveSampleMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration,
            File mappingFile, String filename) throws IOException;

    /**
     * Sets the lastModifiedBy attribute of a StudyConfiguration to the current user's workspace.
     * @param studyConfiguration that is being modified by the user.
     * @param lastModifiedBy the user to last modify this study.
     * @param timeStampedStudyObject the specific study object that is to be timestamped, if left null it
     * @param systemLogMessage to add a log to the study configuration with the given message.
     * it will only timestamp the given studyConfiguration.
     */
    void setStudyLastModifiedByCurrentUser(StudyConfiguration studyConfiguration, UserWorkspace lastModifiedBy,
            TimeStampable timeStampedStudyObject, String systemLogMessage);

    /**
     * Adds the given ExternalLinkList to the StudyConfiguration.
     * @param studyConfiguration to add externalLinkList to.
     * @param externalLinkList to load and add to study.
     * @throws ValidationException if invalid file format.
     * @throws IOException if there's a problem with opening the file.
     */
    void addExternalLinksToStudy(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList)
    throws ValidationException, IOException;

    /**
     * @param annotationGroup to create and add to study.
     * @param studyConfiguration that the group belongs to.
     * @param annotationGroupFile optional file to use to load into the group.
     * @throws ValidationException if invalid.
     * @throws ConnectionException caDSR connection problem
     * @throws IOException I/O exception
     */
    void saveAnnotationGroup(AnnotationGroup annotationGroup, StudyConfiguration studyConfiguration,
            File annotationGroupFile)
    throws ValidationException, ConnectionException, IOException;

    /**
     * Deletes AnnotationGroup from study.
     * @param studyConfiguration to delete annotationGroup from.
     * @param annotationGroup to delete from study.
     */
    void delete(StudyConfiguration studyConfiguration, AnnotationGroup annotationGroup);

    /**
     * Updates field descriptor type.
     * @param fieldDescriptor to update the type for.
     * @param type new type.
     * @return the newly saved AFD.
     * @throws ValidationException if invalid type for the existing data.
     */
    AnnotationFieldDescriptor updateFieldDescriptorType(AnnotationFieldDescriptor fieldDescriptor,
            AnnotationFieldType type)
    throws ValidationException;

    /**
     * Gets all values (from all files as well as currently loaded values) in string format for a fieldDescriptor.
     * @param fieldDescriptor to get string values for.
     * @return set of string values.
     * @throws ValidationException if validation problem.
     */
    Set<String> getAvailableValuesForFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor)
    throws ValidationException;

    /**
     * Makes a fieldDescriptor valid, and then saves it.
     * @param descriptor to save.
     */
    void makeFieldDescriptorValid(AnnotationFieldDescriptor descriptor);

    /**
     * @param persistentObject to save
     */
    void daoSave(Object persistentObject);

    /**
     * Check samples for updates based on a particular genomic data source.
     * @param config the genomic data source configuration
     * @throws ConnectionException connection error
     * @throws ExperimentNotFoundException experiment not found.
     */
    void checkForSampleUpdates(GenomicDataSourceConfiguration config)
            throws ConnectionException, ExperimentNotFoundException;

    /**
     * Gets all visible AnnotationFieldDescriptors from group that the user is authorized to see.
     * @param annotationGroup annotation group from which to retrieve authorized field descriptors
     * @param username user for whom to retrieve authorized field descriptors
     * @return authorized, visible field descriptors
     */
    Set<AnnotationFieldDescriptor> getVisibleAnnotationFieldDescriptorsForUser(AnnotationGroup annotationGroup,
            String username);

    /**
     * Gets all authorized study element groups that the given user is restricted by.
     * @param username the user
     * @param studyConfigId the id of the study configuration
     * @return the authorized study element groups
     */
    List<AuthorizedStudyElementsGroup> getAuthorizedStudyElementsGroups(String username, Long studyConfigId);
}
