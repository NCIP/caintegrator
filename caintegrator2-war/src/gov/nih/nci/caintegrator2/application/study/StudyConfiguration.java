/**
 * The software subject to this notice and license includes both human readable
 * source code form and machine readable, binary, object code form. The caIntegrator2
 * Software was developed in conjunction with the National Cancer Institute 
 * (NCI) by NCI employees, 5AM Solutions, Inc. (5AM), ScenPro, Inc. (ScenPro)
 * and Science Applications International Corporation (SAIC). To the extent 
 * government employees are authors, any rights in such works shall be subject 
 * to Title 17 of the United States Code, section 105. 
 *
 * This caIntegrator2 Software License (the License) is between NCI and You. You (or 
 * Your) shall mean a person or an entity, and all other entities that control, 
 * are controlled by, or are under common control with the entity. Control for 
 * purposes of this definition means (i) the direct or indirect power to cause 
 * the direction or management of such entity, whether by contract or otherwise,
 * or (ii) ownership of fifty percent (50%) or more of the outstanding shares, 
 * or (iii) beneficial ownership of such entity. 
 *
 * This License is granted provided that You agree to the conditions described 
 * below. NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, 
 * no-charge, irrevocable, transferable and royalty-free right and license in 
 * its rights in the caIntegrator2 Software to (i) use, install, access, operate, 
 * execute, copy, modify, translate, market, publicly display, publicly perform,
 * and prepare derivative works of the caIntegrator2 Software; (ii) distribute and 
 * have distributed to and by third parties the caIntegrator2 Software and any 
 * modifications and derivative works thereof; and (iii) sublicense the 
 * foregoing rights set out in (i) and (ii) to third parties, including the 
 * right to license such rights to further third parties. For sake of clarity, 
 * and not by way of limitation, NCI shall have no right of accounting or right 
 * of payment from You or Your sub-licensees for the rights granted under this 
 * License. This License is granted at no charge to You.
 *
 * Your redistributions of the source code for the Software must retain the 
 * above copyright notice, this list of conditions and the disclaimer and 
 * limitation of liability of Article 6, below. Your redistributions in object 
 * code form must reproduce the above copyright notice, this list of conditions 
 * and the disclaimer of Article 6 in the documentation and/or other materials 
 * provided with the distribution, if any. 
 *
 * Your end-user documentation included with the redistribution, if any, must 
 * include the following acknowledgment: This product includes software 
 * developed by 5AM, ScenPro, SAIC and the National Cancer Institute. If You do 
 * not include such end-user documentation, You shall include this acknowledgment 
 * in the Software itself, wherever such third-party acknowledgments normally 
 * appear.
 *
 * You may not use the names "The National Cancer Institute", "NCI", "ScenPro",
 * "SAIC" or "5AM" to endorse or promote products derived from this Software. 
 * This License does not authorize You to use any trademarks, service marks, 
 * trade names, logos or product names of either NCI, ScenPro, SAID or 5AM, 
 * except as required to comply with the terms of this License. 
 *
 * For sake of clarity, and not by way of limitation, You may incorporate this 
 * Software into Your proprietary programs and into any third party proprietary 
 * programs. However, if You incorporate the Software into third party 
 * proprietary programs, You agree that You are solely responsible for obtaining
 * any permission from such third parties required to incorporate the Software 
 * into such third party proprietary programs and for informing Your a
 * sub-licensees, including without limitation Your end-users, of their 
 * obligation to secure any required permissions from such third parties before 
 * incorporating the Software into such third party proprietary software 
 * programs. In the event that You fail to obtain such permissions, You agree 
 * to indemnify NCI for any claims against NCI by such third parties, except to 
 * the extent prohibited by law, resulting from Your failure to obtain such 
 * permissions. 
 *
 * For sake of clarity, and not by way of limitation, You may add Your own 
 * copyright statement to Your modifications and to the derivative works, and 
 * You may provide additional or different license terms and conditions in Your 
 * sublicenses of modifications of the Software, or any derivative works of the 
 * Software as a whole, provided Your use, reproduction, and distribution of the
 * Work otherwise complies with the conditions stated in this License.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
 * (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO 
 * EVENT SHALL THE NATIONAL CANCER INSTITUTE, 5AM SOLUTIONS, INC., SCENPRO, INC.,
 * SCIENCE APPLICATIONS INTERNATIONAL CORPORATION OR THEIR 
 * AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF 
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.nih.nci.caintegrator2.application.study;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Holds data about the sources of study data and authorization for access to data.
 */
@SuppressWarnings({"PMD.ExcessiveClassLength", "PMD.TooManyFields" })
public class StudyConfiguration extends AbstractCaIntegrator2Object {
    
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
        for (AbstractClinicalSourceConfiguration clinicalSourceConfiguration : clinicalConfigurationCollection) {
            existingDescriptors.addAll(clinicalSourceConfiguration.getAnnotationDescriptors());
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
                && hasOneLoadedClinical() && genomicSourcesDeployed() && imageSourcesLoaded());
    }
    
    private boolean imageSourcesLoaded() {
        for (ImageDataSourceConfiguration imageSource : imageDataSources) {
            if (Status.PROCESSING.equals(imageSource.getStatus())) {
                return false;
            }
        }
        return true;
    }

    private boolean genomicSourcesDeployed() {
        for (GenomicDataSourceConfiguration genomicSource : genomicDataSources) {
            if (!Status.LOADED.equals(genomicSource.getStatus())) {
                return false;
            }
        }
        return true;
    }

    private boolean hasOneLoadedClinical() {
        for (AbstractClinicalSourceConfiguration clinicalConfiguration : clinicalConfigurationCollection) {
            if (clinicalConfiguration.isCurrentlyLoaded()) {
                return true;
            }
        }
        return false;
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
     * @return the lastModifiedDate to display to user.
     */
    public String getDisplayableLastModifiedDate() {
        return lastModifiedDate == null ? "" 
                : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).format(lastModifiedDate); 
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
     * @return list of control sample set names.
     */
    public List<String> getControlSampleSetNames() {
        List<String> controlSampleSetNames = new ArrayList<String>();
            for (GenomicDataSourceConfiguration genomicSource : genomicDataSources) {
                controlSampleSetNames.addAll(genomicSource.getControlSampleSetNames());
            }
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
     * @return a set of visible annotation definition
     */
    public Set<AnnotationDefinition> getVisibleSubjectAnnotationCollection() {
        Set<AnnotationDefinition> visibleSet = new HashSet<AnnotationDefinition>();
        for (AbstractClinicalSourceConfiguration source : clinicalConfigurationCollection) {
            visibleSet.addAll(getVisibleClinicalAnnotationDefinition(source.getAnnotationDescriptors()));
        }
        return visibleSet;
    }
    
    private List<AnnotationDefinition> getVisibleClinicalAnnotationDefinition(
            List<AnnotationFieldDescriptor> descriptors) {
        List<AnnotationDefinition> visibleList = new ArrayList<AnnotationDefinition>();
        for (AnnotationFieldDescriptor descriptor : descriptors) {
            if (descriptor.isShownInBrowse()) {
                visibleList.add(descriptor.getDefinition());
            }
        }
        return visibleList;
    }
    
    /**
     * @return a set of visible annotation definition
     */
    public Set<AnnotationDefinition> getVisibleImageSeriesAnnotationCollection() {
        Set<AnnotationDefinition> visibleSet = new HashSet<AnnotationDefinition>();
        for (ImageDataSourceConfiguration source : imageDataSources) {
            visibleSet.addAll(source.getImageAnnotationConfiguration()
                    .getAnnotationFile().getVisibleAnnotationDefinition());
        }
        return visibleSet;
    }
}
