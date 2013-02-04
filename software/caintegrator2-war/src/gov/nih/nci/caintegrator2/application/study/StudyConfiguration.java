/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.application.AbstractCaIntegrator2StudyObject;
import gov.nih.nci.caintegrator2.domain.application.TimeStampable;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeries;
import gov.nih.nci.caintegrator2.domain.imaging.ImageSeriesAcquisition;
import gov.nih.nci.caintegrator2.domain.translational.Study;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Subject;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * Holds data about the sources of study data and authorization for access to data.
 */
@SuppressWarnings({"PMD.ExcessiveClassLength", "PMD.TooManyFields", "PMD.AvoidDuplicateLiterals" })
public class StudyConfiguration extends AbstractCaIntegrator2StudyObject implements TimeStampable {
    
    private static final long serialVersionUID = 1L;
    private Visibility visibility;
    private Status status = Status.NOT_DEPLOYED;
    private String statusDescription;
    private Study study = new Study();
    private List<AbstractClinicalSourceConfiguration> clinicalConfigurationCollection =
        new ArrayList<AbstractClinicalSourceConfiguration>();   
    private List<GenomicDataSourceConfiguration> genomicDataSources = new ArrayList<GenomicDataSourceConfiguration>();
    private List<ImageDataSourceConfiguration> imageDataSources = new ArrayList<ImageDataSourceConfiguration>();
    private List<ExternalLinkList> externalLinkLists = new ArrayList<ExternalLinkList>();
    private List<LogEntry> logEntries = new ArrayList<LogEntry>();
    
    private StudyLogo studyLogo;
    private UserWorkspace userWorkspace;
    private UserWorkspace lastModifiedBy;
    private Date lastModifiedDate;
    private Date deploymentStartDate;
    private Date deploymentFinishDate;
    private transient Map<String, StudySubjectAssignment> identifierToSubjectAssignmentMap;
    private transient Map<String, ImageSeries> identifierToImageSeriesMap;
    private transient Map<String, Timepoint> nameToTimepointMap;

    /**
     * Creates a new <code>StudyConfiguration</code>.
     */
    public StudyConfiguration() {
        super();
    }

    /**
     * @return the clinicalConfigurationCollection
     */
    public List<AbstractClinicalSourceConfiguration> getClinicalConfigurationCollection() {
        return clinicalConfigurationCollection;
    }

    /**
     * @param clinicalConfigurationCollection the clinicalConfigurationCollection to set
     */
    public void setClinicalConfigurationCollection(
            List<AbstractClinicalSourceConfiguration> clinicalConfigurationCollection) {
        this.clinicalConfigurationCollection = clinicalConfigurationCollection;
    }
    
    /**
     * Adds a configuration to the collection.
     * @param configuration - Configuration file to add to the list.
     */
    public void addClinicalConfiguration(AbstractClinicalSourceConfiguration configuration) {
        this.clinicalConfigurationCollection.add(configuration);
    }
    
    /**
     * Deletes a clinical source configuration from the collection.
     * @param clinicalSourceConfiguration - Configuration file to remove from the list.
     */
    public void deleteClinicalSourceConfiguration(AbstractClinicalSourceConfiguration clinicalSourceConfiguration) {
        identifierToSubjectAssignmentMap = null;
        this.clinicalConfigurationCollection.remove(clinicalSourceConfiguration);
    }
    
    /**
     * Updates the mapping of subjectAssignment (remove empty subjectAssignment from the collection).
     * @return a list of obsolete subject assignments
     */
    public List<StudySubjectAssignment> removeObsoleteSubjectAssignment() {
        List<StudySubjectAssignment> removeList = new ArrayList<StudySubjectAssignment>();
        identifierToSubjectAssignmentMap = null;
        for (StudySubjectAssignment studySubjectAssignment : getStudy().getAssignmentCollection()) {
            if (studySubjectAssignment.isObsolete()) {
                removeList.add(studySubjectAssignment);
            }
        }
        getStudy().getAssignmentCollection().removeAll(removeList);
        return removeList;
    }

    /**
     * @return the visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }

    void setStudy(Study study) {
        this.study = study;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the genomicDataSources
     */
    public List<GenomicDataSourceConfiguration> getGenomicDataSources() {
        return genomicDataSources;
    }

    @SuppressWarnings("unused")
    private void setGenomicDataSources(List<GenomicDataSourceConfiguration> genomicDataSources) {
        this.genomicDataSources = genomicDataSources;
    }

    Set<AnnotationFieldDescriptor> getAllExistingDescriptors() {
        Set<AnnotationFieldDescriptor> existingDescriptors = new HashSet<AnnotationFieldDescriptor>();
        for (AnnotationGroup group : study.getAnnotationGroups()) {
            existingDescriptors.addAll(group.getAnnotationFieldDescriptors());
        }
        return existingDescriptors;
    }

    /**
     * Returns an assigment with the given identifier, creating one if necessary.
     * 
     * @param identifier the identifier.
     * @return the new or existing assignment.
     */
    public StudySubjectAssignment getOrCreateSubjectAssignment(String identifier) {
        if (getIdentifierToSubjectAssignmentMap().containsKey(identifier)) {
            return getSubjectAssignment(identifier);
        } else {
            return createSubjectAssignment(identifier);
        }
    }

    /**
     * Returns the matching study subject assignment.
     * 
     * @param identifier the identifier to search for.
     * @return the matching assignment.
     */
    public StudySubjectAssignment getSubjectAssignment(String identifier) {
        return getIdentifierToSubjectAssignmentMap().get(identifier);
    }

    private StudySubjectAssignment createSubjectAssignment(String identifier) {
        StudySubjectAssignment studySubjectAssignment = new StudySubjectAssignment();
        studySubjectAssignment.setSubject(new Subject());
        studySubjectAssignment.setStudy(getStudy());
        studySubjectAssignment.setIdentifier(identifier);
        getStudy().getAssignmentCollection().add(studySubjectAssignment);
        getIdentifierToSubjectAssignmentMap().put(identifier, studySubjectAssignment);
        return studySubjectAssignment;
    }

    private Map<String, StudySubjectAssignment> getIdentifierToSubjectAssignmentMap() {
        if (identifierToSubjectAssignmentMap == null) {
            loadIdentifierToSubjectAssignmentMap();
        }
        return identifierToSubjectAssignmentMap;
    }
    
    private void loadIdentifierToSubjectAssignmentMap() {
        identifierToSubjectAssignmentMap = new HashMap<String, StudySubjectAssignment>();
        for (StudySubjectAssignment assignment : getStudy().getAssignmentCollection()) {
            identifierToSubjectAssignmentMap.put(assignment.getIdentifier(), assignment);
        }
    }

    Timepoint getOrCreateTimepoint(String name) {
        if (getNameToTimepointMap().containsKey(name)) {
            return getNameToTimepointMap().get(name);
        } else {
            return createTimepoint(name);
        }
    }

    private Timepoint createTimepoint(String name) {
        Timepoint timepoint = new Timepoint();
        timepoint.setName(name);
        getNameToTimepointMap().put(name, timepoint);
        return timepoint;
    }

    private Map<String, Timepoint> getNameToTimepointMap() {
        if (nameToTimepointMap == null) {
            loadNameToTimepointMap();
        }
        return nameToTimepointMap;
    }
    
    private void loadNameToTimepointMap() {
        nameToTimepointMap = new HashMap<String, Timepoint>();
        for (Timepoint timepoint : getStudy().getTimepointCollection()) {
            nameToTimepointMap.put(timepoint.getName(), timepoint);
        }
    }

    /**
     * @return all samples in the study.
     */
    public List<Sample> getSamples() {
        List<Sample> samples = new ArrayList<Sample>();
        for (StudySubjectAssignment studySubjectAssignment : getStudy().getAssignmentCollection()) {
            for (SampleAcquisition sampleAcquisition : studySubjectAssignment.getSampleAcquisitionCollection()) {
                samples.add(sampleAcquisition.getSample());
            }
        }
        return samples;
    }

    /**
     * @return the imageDataSources
     */
    public List<ImageDataSourceConfiguration> getImageDataSources() {
        return imageDataSources;
    }

    /**
     * @param imageDataSources the imageDataSources to set
     */
    @SuppressWarnings("unused")
    private void setImageDataSources(List<ImageDataSourceConfiguration> imageDataSources) {
        this.imageDataSources = imageDataSources;
    }

    /**
     * @return the externalLinkLists
     */
    public List<ExternalLinkList> getExternalLinkLists() {
        return externalLinkLists;
    }

    /**
     * @param externalLinkLists the externalLinkLists to set
     */
    @SuppressWarnings("unused")
    private void setExternalLinkLists(List<ExternalLinkList> externalLinkLists) {
        this.externalLinkLists = externalLinkLists;
    }

    ImageSeries getImageSeries(String identifier) {
        return getIdentifierToImageSeriesMap().get(identifier);
    }

    private Map<String, ImageSeries> getIdentifierToImageSeriesMap() {
        if (identifierToImageSeriesMap == null) {
            loadIdentifierToImageSeriesMap();
        }
        return identifierToImageSeriesMap;
    }

    private void loadIdentifierToImageSeriesMap() {
        identifierToImageSeriesMap = new HashMap<String, ImageSeries>();
        for (ImageDataSourceConfiguration imageSource : imageDataSources) {
            loadIdentifierToImageSeriesMap(imageSource.getImageSeriesAcquisitions());
        }
    }

    private void loadIdentifierToImageSeriesMap(List<ImageSeriesAcquisition> imageSeriesAcquisitions) {
        for (ImageSeriesAcquisition imageSeriesAcquisition : imageSeriesAcquisitions) {
            loadIdentifierToImageSeriesMap(imageSeriesAcquisition.getSeriesCollection());
        }
    }

    private void loadIdentifierToImageSeriesMap(Collection<ImageSeries> seriesCollection) {
        for (ImageSeries imageSeries : seriesCollection) {
           identifierToImageSeriesMap.put(imageSeries.getIdentifier(), imageSeries);
        }
    }


    Sample getSample(String sampleName) {
        for (GenomicDataSourceConfiguration sourceConfiguration : getGenomicDataSources()) {
            Sample sample = sourceConfiguration.getSample(sampleName);
            if (sample != null) {
                return sample;
            }
        }
        return null;
    }

    /**
     * @return the studyLogo
     */
    public StudyLogo getStudyLogo() {
        return studyLogo;
    }

    /**
     * @param studyLogo the studyLogo to set
     */
    public void setStudyLogo(StudyLogo studyLogo) {
        this.studyLogo = studyLogo;
    }

    /**
     * @return the boolean of whether the study has Genomic data 
     */
    public boolean hasGenomicDataSources() {
        return !genomicDataSources.isEmpty();
    }

    /**
     * @return the boolean of whether the study has Image data 
     */
    public boolean hasImageDataSources() {
        return !imageDataSources.isEmpty();
    }
    
    /**
     * @return the boolean of whether the study has clinical data.
     */
    public boolean hasClinicalDataSources() {
        return !clinicalConfigurationCollection.isEmpty();
    }

    /**
     * @return the boolean of whether the study is deployed
     */
    public boolean isDeployed() {
        return Status.DEPLOYED.equals(status);
    }

    /**
     * @return the boolean of whether the study is deployable
     */
    public boolean isDeployable() {
        return ((Status.NOT_DEPLOYED.equals(status) || Status.ERROR.equals(status))
                && allLoadedClinical() && genomicSourcesDeployed() && allImageSourcesLoaded());
    }
    
    private boolean allImageSourcesLoaded() {
        for (ImageDataSourceConfiguration imageSource : imageDataSources) {
            if (!imageSource.getStatus().isLoadable()) {
                return false;
            }
        }
        return true;
    }

    private boolean genomicSourcesDeployed() {
        for (GenomicDataSourceConfiguration genomicSource : genomicDataSources) {
            if (!genomicSource.getStatus().isLoadable()) {
                return false;
            }
        }
        return true;
    }

    private boolean allLoadedClinical() {
        for (AbstractClinicalSourceConfiguration clinicalConfiguration : clinicalConfigurationCollection) {
            if (!clinicalConfiguration.isCurrentlyLoaded()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the userWorkspace
     */
    public UserWorkspace getUserWorkspace() {
        return userWorkspace;
    }

    /**
     * @param userWorkspace the userWorkspace to set
     */
    public void setUserWorkspace(UserWorkspace userWorkspace) {
        this.userWorkspace = userWorkspace;
    }

    /**
     * @return the lastModifiedBy
     */
    public UserWorkspace getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    public void setLastModifiedBy(UserWorkspace lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    /**
     * {@inheritDoc}
     */
    public String getDisplayableLastModifiedDate() {
        return DateUtil.getDisplayableTimeStamp(lastModifiedDate); 
    }

    /**
     * @return the deploymentStartDate
     */
    public Date getDeploymentStartDate() {
        return deploymentStartDate;
    }

    /**
     * @param deploymentStartDate the deploymentStartDate to set
     */
    public void setDeploymentStartDate(Date deploymentStartDate) {
        this.deploymentStartDate = deploymentStartDate;
    }

    /**
     * @return the deploymentFinishDate
     */
    public Date getDeploymentFinishDate() {
        return deploymentFinishDate;
    }

    /**
     * @param deploymentFinishDate the deploymentFinishDate to set
     */
    public void setDeploymentFinishDate(Date deploymentFinishDate) {
        this.deploymentFinishDate = deploymentFinishDate;
    }

    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    /**
     * @param name the controlSampleSet name
     * @return the requested controlSampleSet
     */
    public SampleSet getControlSampleSet(String name) {
        SampleSet controlSampleSet = null;
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration : genomicDataSources) {
            controlSampleSet = genomicDataSourceConfiguration.getControlSampleSet(name);
            if (controlSampleSet != null) {
                return controlSampleSet;
            }
        }
        return null;
    }
    
    /**
     * Get all control samples.
     * @return set of all control samples
     */
    public Set<Sample> getAllControlSamples() {
        Set<Sample> controlSamples = new HashSet<Sample>();
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration : genomicDataSources) {
            controlSamples.addAll(genomicDataSourceConfiguration.getAllControlSamples());
        }
        return controlSamples;
    }

    /**
     * @return the boolean of whether the study has Control Samples
     */
    public boolean hasControlSamples() {
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration : genomicDataSources) {
            if (!genomicDataSourceConfiguration.getAllControlSamples().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all control sample set names.
     * @param platformName to narrow the samples based on platform name (make it null if to get all control samples).
     * @return list of control sample set names.
     */
    public List<String> getControlSampleSetNames(String platformName) {
        List<String> controlSampleSetNames = new ArrayList<String>();
            for (GenomicDataSourceConfiguration genomicSource : genomicDataSources) {
                if (StringUtils.isBlank(platformName) || (platformName.equals(genomicSource.getPlatformName()))) {
                    controlSampleSetNames.addAll(genomicSource.getControlSampleSetNames());
                }
            }
        Collections.sort(controlSampleSetNames);
        return controlSampleSetNames;
    }

    /**
     * @return the boolean of whether the study has Expression Data
     */
    public boolean hasExpressionData() {
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration : genomicDataSources) {
            if (genomicDataSourceConfiguration.isExpressionData()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the boolean of whether the study has Copy Number Data
     */
    public boolean hasCopyNumberData() {
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration : genomicDataSources) {
            if (genomicDataSourceConfiguration.isCopyNumberData()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the boolean of whether the study has SNP Data
     */
    public boolean hasSnpData() {
        for (GenomicDataSourceConfiguration genomicDataSourceConfiguration : genomicDataSources) {
            if (genomicDataSourceConfiguration.isSnpData()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retrieves any existing field descriptors with the given name for the given study.
     * @param name to search existing annotation field descriptors for.
     * @return any matching AnnotationFieldDescriptor in the study with the given name.
     */
    public AnnotationFieldDescriptor getExistingFieldDescriptorInStudy(String name) {
        return getMatchingDescriptor(name, getAllExistingDescriptors());
    }
    
    private AnnotationFieldDescriptor getMatchingDescriptor(String name, 
            Collection<AnnotationFieldDescriptor> descriptors) {
        for (AnnotationFieldDescriptor descriptor : descriptors) {
            if (descriptor.getName().toUpperCase(Locale.ENGLISH).equals(name.toUpperCase(Locale.ENGLISH))) {
                return descriptor;
            }
        }
        return null;
    }

    /**
     * @return the logEntries
     */
    public List<LogEntry> getLogEntries() {
        return logEntries;
    }

    /**
     * @param logEntries the logEntries to set
     */
    @SuppressWarnings("unused")
    private void setLogEntries(List<LogEntry> logEntries) {
        this.logEntries = logEntries;
    }
}
