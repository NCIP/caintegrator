/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator2.application.study.AbstractClinicalSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.AuthorizedStudyElementsGroup;
import gov.nih.nci.caintegrator2.application.study.FileColumn;
import gov.nih.nci.caintegrator2.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.ImageDataSourceConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.application.study.StudyLogo;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.CopyNumberAlterationCriterion;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.application.ResultColumn;
import gov.nih.nci.caintegrator2.domain.application.SubjectListCriterion;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.domain.genomic.GeneLocationConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.GenomeBuildVersionEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterList;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SegmentData;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Main DAO interface for storage and retrieval of persistent entities.
 */
/**
 * 
 */
public interface CaIntegrator2Dao {


    /**
     * Refreshes the object given.
     *
     * @param persistentObject the object to refresh.
     */
    void refresh(Object persistentObject);

    /**
     * Clears the current session.
     */
    void clearSession();

    /**
     * Saves the object given.
     *
     * @param persistentObject the object to save.
     */
    void save(Object persistentObject);

    /**
     * Deletes the object given.
     *
     * @param persistentObject the object to delete.
     */
    void delete(Object persistentObject);

    /**
     * Returns the persistent object with the id given.
     *
     * @param <T> type of object being returned.
     * @param id id of the object to retrieve
     * @param objectClass the class of the object to retrieve
     * @return the requested object.
     */
    <T> T get(Long id, Class<T> objectClass);

    /**
     * Saves the status/status description only for the given source.
     * @param source to save status for.
     */
    void saveSubjectSourceStatus(AbstractClinicalSourceConfiguration source);

    /**
     * Deletes all given objects from the database.
     * @param objects - persistent objects to be removed.
     */
    @SuppressWarnings("unchecked")
    void removeObjects(Collection objects);

    /**
     * Returns the workspace belonging to the specified user.
     *
     * @param username retrieve workspace for this user.
     * @return the user's workspace
     */
    UserWorkspace getWorkspace(String username);

    /**
     * Returns a list of AnnotationDefinitions that match the keywords.
     * @param keywords - keywords to search on.
     * @return - list of annotation definitions that match.
     */
    List<AnnotationDefinition> findMatches(Collection<String> keywords);

    /**
     * Retrieves all workspaces subscribed to the given study.
     * @param study to find workspace subscriptions for.
     * @return the list of workspaces.
     */
    List<UserWorkspace> retrieveAllSubscribedWorkspaces(Study study);

    /**
     * Returns the subjects (via their linked <code>StudySubjectAssignments</code> that match
     * the corresponding criterion.
     *
     * @param criterion find subjects that match the given criterion.
     * @param study restrict the search to the given study.
     * @return the list of matches.
     */
    List<StudySubjectAssignment> findMatchingSubjects(AbstractAnnotationCriterion criterion, Study study);

    /**
     * Returns the subjects (via their linked <code>StudySubjectAssignments</code> that match
     * the corresponding subject list criterion.
     *
     * @param subjectListCriterion find subjects that exist in the given subjectListCriterion.
     * @param study restrict the search to the given study.
     * @return the list of matches.
     */
    List<StudySubjectAssignment> findMatchingSubjects(SubjectListCriterion subjectListCriterion, Study study);

    /**
     * Returns the subjects (via their linked <code>ImageSeries</code> that match
     * the corresponding criterion.
     *
     * @param criterion find subjects that match the given criterion.
     * @param study restrict the search to the given study.
     * @return the list of matches.
     */
    List<ImageSeries> findMatchingImageSeries(AbstractAnnotationCriterion criterion, Study study);

    /**
     * Returns the subjects (via their linked <code>SampleAcquisitions</code> that match
     * the corresponding criterion.
     *
     * @param criterion find subjects that match the given criterion.
     * @param study restrict the search to the given study.
     * @return the list of matches.
     */
    List<SampleAcquisition> findMatchingSamples(AbstractAnnotationCriterion criterion, Study study);

    /**
     * Returns the gene expression reporters that match the parameters given.
     *
     * @param geneSymbols finds expression reporters for genes that match the symbol.
     * @param reporterType return only reporters of this type
     * @param study restrict the search to the given study.
     * @param platform restricts the search to the given platform.
     * @return the list of matches.
     */
    Set<AbstractReporter> findReportersForGenes(Set<String> geneSymbols, ReporterTypeEnum reporterType,
            Study study, Platform platform);

    /**
     * Returns the SegmentDatas that match the copy number criterion.
     * @param copyNumberCriterion to find segment datas for.
     * @param study restrict search to given study.
     * @param platform restricts search to the given platform.
     * @return the list of matches.
     * @throws InvalidCriterionException if a criterion is invalid.
     */
    List<SegmentData> findMatchingSegmentDatas(CopyNumberAlterationCriterion copyNumberCriterion,
            Study study, Platform platform) throws InvalidCriterionException;

    /**
     * Returns the SegmentDatas that match the location of the given segment datas.
     * @param segmentDatasToMatch uses the location of these segment datas to find all others.
     * @param study restrict search to given study.
     * @param platform restricts search to the given platform.
     * @return the list of matches.
     */
    List<SegmentData> findMatchingSegmentDatasByLocation(List<SegmentData> segmentDatasToMatch,
            Study study, Platform platform);

    /**
     * Returns the genes associated with Dna reporters for the given chromosomal start and end positions.
     * @param chromosome the chromosome to search for genes.
     * @param startPosition on the chromosome to search for genes.
     * @param endPosition on the chromosome to search for genes.
     * @param genomeBuildVersion retrieves the genes in the locations for this build version.
     * @return list of genes in given location.
     *
     */
    List<Gene> findGenesByLocation(String chromosome, Integer startPosition, Integer endPosition,
            GenomeBuildVersionEnum genomeBuildVersion);

    /**
     * Looks to see if a Genome Location version has been mapped to chromosomal locations.
     * @param genomeVersion to check if it is mapped.
     * @return T/F value determining if it was mapped or not.
     */
    boolean isGenomeVersionMapped(GenomeBuildVersionEnum genomeVersion);

    /**
     * Gets geneLocationConfiguration from the build version.
     * @param genomeVersion to get gene location configuration for.
     * @return gene location configuration for the version.
     */
    GeneLocationConfiguration getGeneLocationConfiguration(GenomeBuildVersionEnum genomeVersion);

    /**
     * Returns the definitions that matches the name given (if one exists).
     *
     * @param name find definitions for this name
     * @param dataType find definitions for this dataType
     * @return the matching definition or null.
     */
    AnnotationDefinition getAnnotationDefinition(String name, AnnotationTypeEnum dataType);

    /**
     * Returns the definitions that matches the publicId given (if one exists).
     *
     * @param cdeId find definitions with this publicId
     * @param version find definitions with this version
     * @return the matching definition or null.
     */
    AnnotationDefinition getAnnotationDefinition(Long cdeId, Float version);

    /**
     * Returns the map of symbol and gene.
     *
     * @return the mapping of symbol and gene.
     */
    Map<String, Gene> getGeneSymbolMap();

    /**
     * Returns the gene that matches the given symbol or null if no match is found.
     *
     * @param symbol the gene symbol
     * @return the matching gene or null.
     */
    Gene getGene(String symbol);


    /**
     * Returns the gene that matches, if not found then create one.
     * @param symbol the gene symbol
     * @return gene
     */
    Gene lookupOrCreateGene(String symbol);

    /**
     * Given a collection of gene symbols, it will return back all the ones that exist for given study.
     * @param symbols the gene symbols.
     * @param study the study to check gene symbols existance for.
     * @return a subset of the given symbols that exist in the study (in UPPERCASE).
     */
    Set<String> retrieveGeneSymbolsInStudy(Collection<String> symbols, Study study);

    /**
     * Returns the array design platform that matches the given name.
     *
     * @param name the platform name.
     * @return the matching platform.
     */
    Platform getPlatform(String name);

    /**
     * Returns the platformConfiguration that matches the given name.
     *
     * @param name the platformConfiguration name.
     * @return the matching platformConfiguration.
     */
    PlatformConfiguration getPlatformConfiguration(String name);

    /**
     * Returns the array design reporter list that matches the given name.
     *
     * @param name the reporter list name.
     * @return the matching platform.
     */
    ReporterList getReporterList(String name);

    /**
     * Check for duplicate study name.
     *
     * @param study
     *            the study object
     * @param username the user checking to see if it's a duplicate, because it is based
     * on that users UserGroup privileges.
     * @return true or false.
     */
    boolean isDuplicateStudyName(Study study, String username);

    /**
     * To retrieve studyLogo based on ID and Name.
     * @param studyId - id for study.
     * @param studyShortTitleText - title of study (to make it more difficult to retrieve logo by guessing ID).
     * @return - StudyLogo object.
     */
    StudyLogo retrieveStudyLogo(Long studyId, String studyShortTitleText);

    /**
     * Retrieves the unique values for a study's annotation definition.
     * @param <T> class type, which is based on the definition.getType() (String, Double, Date).
     * @param study object of interest that the values and annotation definition belong to.
     * @param definition is the annotation whose unique values are returned based on the study given.
     * @param entityType is the type of annotation the definition is.
     * @param objectClass is the object type being returned.
     * @return List of unique values.
     */
    <T> List<T> retrieveUniqueValuesForStudyAnnotation(Study study,
            AnnotationDefinition definition,
            EntityTypeEnum entityType,
            Class<T> objectClass);

    /**
     * Ensures the given object is attached.
     *
     * @param <T> type of the object
     * @param persistentObject the object to attach if detached.
     * @return the merged object.
     */
    <T> T merge(T persistentObject);

    /**
     * Based on a given study, retrieves the ImageDataSourceConfiguration, it only returns the first one it finds
     * so if there's multiple sources defined for a study it will not return all.
     * @param study to find imaging source.
     * @return imaging source for study.
     */
    ImageDataSourceConfiguration retrieveImagingDataSourceForStudy(Study study);

    /**
     * For a collection of image acquisition, finds the number of images associated with it.
     * @param imageSeries to retrieve number of images.
     * @return number of images for source.
     */
    int retrieveNumberImages(Collection<ImageSeries> imageSeries);

    /**
     * For a given genomic source, retrieves all distinct platforms.
     * @param genomicSource - source of interest.
     * @return Platforms for that source.
     */
    List<Platform> retrievePlatformsForGenomicSource(GenomicDataSourceConfiguration genomicSource);

    /**
     * Returns all platforms ordered by name.
     *
     * @return the platforms.
     */
    List<Platform> getPlatforms();

    /**
     * Returns all platform configurations.
     * @return the platformConfigurations.
     */
    List<PlatformConfiguration> getPlatformConfigurations();

    /**
     * Check if the platform is in used.
     *
     * @param platform the platform
     * @return boolean
     */
    boolean isPlatformInUsed(Platform platform);

    /**
     * Returns all studies ordered by name that are publicly accessible.
     * @return publicly accessible studies.
     */
    List<Study> getPublicStudies();

    /**
     * Returns all studies ordered by name.
     * @param username for adding instance level security.
     * @return the studies.
     */
    List<Study> getStudies(String username);

    /**
     * Sets the Hibernate flush mode.
     *
     * @param mode the flush mode.
     */
    void setFlushMode(int mode);

    /**
     * Retrieves all FileColumns for the given field descriptor.
     * @param fieldDescriptor to search columns for.
     * @return list of file columns matching.
     */
    List<FileColumn> getFileColumnsUsingAnnotationFieldDescriptor(AnnotationFieldDescriptor fieldDescriptor);

    /**
     * Retrieves all ResultColumns for the given field descriptor.
     * @param fieldDescriptor to search columns for.
     * @return list of result columns matching.
     */
    List<ResultColumn> getResultColumnsUsingAnnotation(AnnotationFieldDescriptor fieldDescriptor);

    /**
     * Retrieves all AbstractAnnotationCriterion for the given field descriptor.
     * @param fieldDescriptor to search criterion for.
     * @return list of criterion matching.
     */
    List<AbstractAnnotationCriterion> getCriteriaUsingAnnotation(AnnotationFieldDescriptor fieldDescriptor);

    /**
     * Retrieves gistic analysis associated with the given samples.
     * @param genomicSource genomic source for gistic analysis.
     * @return matching gistic analysis.
     */
    Set<GisticAnalysis> getGisticAnalysisUsingGenomicSource(
            GenomicDataSourceConfiguration genomicSource);

    /**
     * This is used for long running transactions with no database activity, will send a small query to the database
     * so the database doesn't think it is an idle thread and kill it.
     */
    void runSessionKeepAlive();

    /**
     * Retrieves studyConfigurations which used this specified platform.
     * @param platform the platform to be used to look up studyConfigurations.
     * @return List of StudyConfigurations or null if no studies use it.
     */
    List<StudyConfiguration> getStudyConfigurationsWhichNeedThisPlatform(Platform platform);

    /**
     * Retrieves a list of all genomic data sources in the db.
     * @return all the stored genomic data sources
     */
    List<GenomicDataSourceConfiguration> getAllGenomicDataSources();

    /**
     * Marks studies that have at least one genomic data source needing data refresh as having refreshed data.
     */
    void markStudiesAsNeedingRefresh();

    /**
     * Retrieves AuthorizedStudyElementGroups that are accessible for this CSM user.
     * @param username the username to be used to check authorization.
     * @param studyConfigurationId the StudyConfigurationId
     * @return AuthorizedStudyElementGroups that are accessible for this CSM user.
     */

    List<AuthorizedStudyElementsGroup> getAuthorizedStudyElementGroups(String username, Long studyConfigurationId);
    
    /**
     * retrieves the authorized AnnotationFieldDescriptors that are accessible for this CSM user.
     * @param username the username to be used to check authorization.
     * @param studyConfiguration the StudyConfiguration
     * @return AnnotationFieldDescriptors that are accessible for this CSM user.
     */
    List<AnnotationFieldDescriptor> getAuthorizedAnnotationFieldDescriptors(String username,
            StudyConfiguration studyConfiguration);

}
