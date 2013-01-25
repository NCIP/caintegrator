/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.caarray.ExperimentNotFoundException;
import gov.nih.nci.security.exceptions.CSException;
import gov.nih.nci.security.exceptions.CSSecurityException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Service used to create, define, deploy and update studies.
 */
public interface StudyManagementService {
    
    /**
     * Saves a study.
     * 
     * @param studyConfiguration study to save
     */
    void save(StudyConfiguration studyConfiguration);
    
    /**
     * Saves the annotation definition.
     * @param definition to persist.
     * @throws ValidationException if values are invalid for the given type.
     */
    void save(AnnotationDefinition definition) throws ValidationException;
    
    /**
     * Creates a protection element for the Study Configuration.
     * @param studyConfiguration to create protection element for.
     * @throws CSException if there's a problem creating the protection element.
     */
    void createProtectionElement(StudyConfiguration studyConfiguration) throws CSException;
    
    /**
     * Saves a genomic source.
     * @param genomicSource to save.
     */
    void saveGenomicDataSource(GenomicDataSourceConfiguration genomicSource);
    
    /**
     * Saves an imaging source.
     * @param imagingSource to save.
     */
    void saveImagingDataSource(ImageDataSourceConfiguration imagingSource);
    
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
     * @param studyConfiguration study configuration of the clinical source
     * @param clinicalSource clinical source to delete
     * @throws ValidationException fail to reload
     */
    void delete(StudyConfiguration studyConfiguration,
            AbstractClinicalSourceConfiguration clinicalSource)
        throws ValidationException;
    
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
     * @return the clinical source configuration created.
     * @throws ValidationException if the file was not a valid annotation file.
     * @throws IOException if the annotation file couldn't be copied to permanent storage.
     */
    DelimitedTextClinicalSourceConfiguration addClinicalAnnotationFile(StudyConfiguration studyConfiguration, 
            File annotationFile, String filename) throws ValidationException, IOException;
    
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
     * Loads a specific clinical annotation from study configuration.
     * 
     * @param studyConfiguration study configuration to load
     * @param clinicalSourceConfiguration clinical source configuration to load
     * @throws ValidationException fail to load
     */
    void loadClinicalAnnotation(StudyConfiguration studyConfiguration,
            AbstractClinicalSourceConfiguration clinicalSourceConfiguration)
        throws ValidationException;

    /**
     * ReLoads clinical annotations for study configuration.
     * 
     * @param studyConfiguration study configuration to load
     * @throws ValidationException fail to load
     */
    void reLoadClinicalAnnotation(StudyConfiguration studyConfiguration)
        throws ValidationException;
    
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
     */
    void addImageSource(StudyConfiguration studyConfiguration, ImageDataSourceConfiguration imageSource) 
    throws ConnectionException;
    
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
     */
    void loadImageSource(ImageDataSourceConfiguration imageSource) throws ConnectionException;
    
    
    /**
     * Adds an image series annotation file to the study. The file given will be copied to permanent storage 
     * allowing the file provided as an argument to be removed after completion of this method.
     * 
     * @param imageDataSourceConfiguration add the annotation file to this image data source
     * @param annotationFile annotation file to add.
     * @param filename the name with which the annotation file should be stored 
     *        (allows for the use of files with temp names as input)
     * @return the clinical source configuration created.
     * @throws ValidationException if the file was not a valid annotation file.
     * @throws IOException if the annotation file couldn't be copied to permanent storage.
     */
    ImageAnnotationConfiguration addImageAnnotationFile(ImageDataSourceConfiguration imageDataSourceConfiguration, 
            File annotationFile, String filename) throws ValidationException, IOException;

    /**
     * Loads image annotations given an image data source configuration.
     * 
     * @param imageDataSource to load
     * @throws ValidationException fail to load
     */
    void loadImageAnnotation(ImageDataSourceConfiguration imageDataSource) throws ValidationException;

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
     * Returns the refreshed entity attached to the current Hibernate session.
     * 
     * @param <T> type of object being returned.
     * @param entity a persistent entity with the id set.
     * @return the refreshed entity.
     */
    <T> T getRefreshedStudyEntity(T entity);

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

     * @param fileColumn column receiving definition.
     * @param dataElement the selected data element.
     * @param study the study that the FileColumn belongs to.
     * @param entityType the entityType for the data element.
     * @param keywords the keywords that were used to find this data element.
     * @throws ConnectionException if underlying data sources couldn't be reached.
     * @throws ValidationException if the data element selected is invalid for this definition.
     */
    void setDataElement(FileColumn fileColumn, CommonDataElement dataElement, Study study, EntityTypeEnum entityType, 
                        String keywords)
    throws ConnectionException, ValidationException;

    /**
     * Selects an existing annotation definition for a column.
     * 
     * @param study is the study that the definition is getting set for.
     * @param fileColumn column receiving definition.
     * @param annotationDefinition the selected definition.
     * @param entityType entity type for the annotation definition.
     * @throws ValidationException if invalid definition for the given values.
     */
    void setDefinition(Study study, FileColumn fileColumn, AnnotationDefinition annotationDefinition, 
            EntityTypeEnum entityType) throws ValidationException;

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
     * Creates a new SurvivalValueDefinition for the study and returns.
     * @param study - Study to create SurvivalValueDefinition for.
     * @return newly created object.
     */
    SurvivalValueDefinition createNewSurvivalValueDefinition(Study study);

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
     */
    void saveCopyNumberMappingFile(GenomicDataSourceConfiguration genomicDataSourceConfiguration, 
            File mappingFile, String filename) throws IOException;

    /**
     * Sets the lastModifiedBy attribute of a StudyConfiguration to the current user's workspace.
     * @param studyConfiguration that is being modified by the user.
     * @param lastModifiedBy the user to last modify this study.
     */
    void setLastModifiedByCurrentUser(StudyConfiguration studyConfiguration, UserWorkspace lastModifiedBy);
    
    /**
     * Adds the given ExternalLinkList to the StudyConfiguration.
     * @param studyConfiguration to add externalLinkList to.
     * @param externalLinkList to load and add to study.
     * @throws ValidationException if invalid file format.
     * @throws IOException if there's a problem with opening the file.
     */
    void addExternalLinksToStudy(StudyConfiguration studyConfiguration, ExternalLinkList externalLinkList) 
    throws ValidationException, IOException;
}
