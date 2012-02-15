package gov.nih.nci.caintegrator2.domain.translational;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.Platform;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals") // for the word "unused"
public class Study extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;

    private String longTitleText;
    private String shortTitleText;
    private Set<Timepoint> timepointCollection = new HashSet<Timepoint>();
    private Set<SurvivalValueDefinition> survivalValueDefinitionCollection = new HashSet<SurvivalValueDefinition>();
    private Set<StudySubjectAssignment> assignmentCollection = new HashSet<StudySubjectAssignment>();
    private Timepoint defaultTimepoint;
    private Set<AnnotationGroup> annotationGroups = new HashSet<AnnotationGroup>();
    private StudyConfiguration studyConfiguration;
    private Boolean publiclyAccessible = false;
    private Boolean enabled = true;

    /**
     * The default annotation group name.
     */
    public static final String DEFAULT_ANNOTATION_GROUP = "Annotations - Default";

    /**
     * @return the studyConfiguration
     */
    public StudyConfiguration getStudyConfiguration() {
        return studyConfiguration;
    }

    /**
     * @param studyConfiguration the studyConfiguration to set
     */
    public void setStudyConfiguration(StudyConfiguration studyConfiguration) {
        this.studyConfiguration = studyConfiguration;
    }

    /**
     * @return the longTitleText
     */
    public String getLongTitleText() {
        return longTitleText;
    }

    /**
     * @param longTitleText
     *            the longTitleText to set
     */
    public void setLongTitleText(String longTitleText) {
        this.longTitleText = longTitleText;
    }

    /**
     * @return the shortTitleText
     */
    public String getShortTitleText() {
        return shortTitleText;
    }

    /**
     * @param shortTitleText
     *            the shortTitleText to set
     */
    public void setShortTitleText(String shortTitleText) {
        this.shortTitleText = shortTitleText;
    }

    /**
     * @return the timepointCollection
     */
    public Set<Timepoint> getTimepointCollection() {
        return timepointCollection;
    }

    /**
     * @param timepointCollection
     *            the timepointCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setTimepointCollection(Set<Timepoint> timepointCollection) {
        this.timepointCollection = timepointCollection;
    }

    /**
     * @return the survivalValueDefinitionCollection
     */
    public Set<SurvivalValueDefinition> getSurvivalValueDefinitionCollection() {
        return survivalValueDefinitionCollection;
    }

    /**
     * @param survivalValueDefinitionCollection
     *            the survivalValueDefinitionCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setSurvivalValueDefinitionCollection(
            Set<SurvivalValueDefinition> survivalValueDefinitionCollection) {
        this.survivalValueDefinitionCollection = survivalValueDefinitionCollection;
    }

    /**
     * @return the defaultTimepoint
     */
    public Timepoint getDefaultTimepoint() {
        return defaultTimepoint;
    }

    /**
     * @param defaultTimepoint
     *            the defaultTimepoint to set
     */
    public void setDefaultTimepoint(Timepoint defaultTimepoint) {
        this.defaultTimepoint = defaultTimepoint;
    }

    /**
     * Returns all samples associated with the study.
     *
     * @return the samples.
     */
    public Set<Sample> getSamples() {
        Set<Sample> samples = new HashSet<Sample>();
        for (StudySubjectAssignment subjectAssignment : getAssignmentCollection()) {
            for (SampleAcquisition sampleAcquisition : subjectAssignment.getSampleAcquisitionCollection()) {
                samples.add(sampleAcquisition.getSample());
            }
        }
        return Collections.unmodifiableSet(samples);
    }

    /**
     * Returns all array datas of the given type in the project.
     *
     * @param reporterType get array datas of this type.
     * @param platform to retrieve Array Datas for (null if you want to use all platforms).
     * @return an immutable set of the matching array datas.
     */
    public Set<ArrayData> getArrayDatas(ReporterTypeEnum reporterType, Platform platform) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (Sample sample : getSamples()) {
            arrayDatas.addAll(sample.getArrayDatas(reporterType, platform));
        }
        return Collections.unmodifiableSet(arrayDatas);
    }

    /**
     * @return the assignmentCollection
     */
    public Set<StudySubjectAssignment> getAssignmentCollection() {
        return assignmentCollection;
    }

    /**
     * @param assignmentCollection the assignmentCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setAssignmentCollection(Set<StudySubjectAssignment> assignmentCollection) {
        this.assignmentCollection = assignmentCollection;
    }

    /**
     * @param name the controlSampleSet name
     * @return the requested controlSampleSet
     */
    public SampleSet getControlSampleSet(String name) {
        return studyConfiguration.getControlSampleSet(name);
    }

    /**
     * @return the boolean of whether the study has Genomic data
     */
    public boolean hasGenomicDataSources() {
        return studyConfiguration.hasGenomicDataSources();
    }

    /**
     * @return the boolean of whether the study has Image data
     */
    public boolean hasImageDataSources() {
        return studyConfiguration.hasImageDataSources();
    }

    /**
     * @return the boolean of whether the study has clinical data.
     */
    public boolean hasClinicalDataSources() {
        return studyConfiguration.hasClinicalDataSources();
    }

    /**
     * @return the boolean of whether the study has ImageSeries data
     */
    public boolean hasVisibleImageSeriesData() {
        for (AnnotationFieldDescriptor afd : getAllVisibleAnnotationFieldDescriptors()) {
            if (EntityTypeEnum.IMAGESERIES.equals(afd.getAnnotationEntityType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the boolean of whether the study is deployed
     */
    public boolean isDeployed() {
        return studyConfiguration != null
            && studyConfiguration.isDeployed();
    }

    /**
     * @return the boolean of whether the study has Expression data
     */
    public boolean hasExpressionData() {
        return studyConfiguration != null
            && studyConfiguration.hasExpressionData();
    }

    /**
     * @return the boolean of whether the study has Copy Number data
     */
    public boolean hasCopyNumberData() {
        return studyConfiguration != null
            && studyConfiguration.hasCopyNumberData();
    }

    /**
     * @return the boolean of whether the study has Copy Number data
     */
    public boolean hasCghCalls() {
        return studyConfiguration != null
            && studyConfiguration.hasCghCalls();
    }

    /**
     * @return the boolean of whether the study has SNP data
     */
    public boolean hasSnpData() {
        return studyConfiguration != null
            && studyConfiguration.hasSnpData();
    }

    /**
     * @return the subjectAnnotationGroup
     */
    public Set<AnnotationGroup> getAnnotationGroups() {
        return annotationGroups;
    }

    /**
     * @param subjectAnnotationGroup the subjectAnnotationGroup to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setAnnotationGroups(Set<AnnotationGroup> annotationGroups) {
        this.annotationGroups = annotationGroups;
    }

    /**
     * Get AnnotationGroup by name.
     * @param name of the annotation group to retrieve
     * @return AnnotationGroup
     */
    public AnnotationGroup getAnnotationGroup(String name) {
        for (AnnotationGroup annotationGroup : annotationGroups) {
            if (annotationGroup.getName().equals(name)) {
                return annotationGroup;
            }
        }
        return null;
    }

    /**
     *
     * @return all annotation groups, sorted by name.
     */
    public List<AnnotationGroup> getSortedAnnotationGroups() {
        List<AnnotationGroup> annotationGroupList = new ArrayList<AnnotationGroup>(annotationGroups);
        Collections.sort(annotationGroupList);
        return annotationGroupList;
    }

    /**
     * @param groupName name of the group.
     * @return the default SubjectAnnotationGroup
     */
    public AnnotationGroup getOrCreateAnnotationGroup(String groupName) {
        if (getAnnotationGroup(groupName) == null) {
            return createAnnotationGroup(groupName);
        }
        return getAnnotationGroup(groupName);
    }

    /**
     * @param groupName name of the group.
     * @return the created annotation group
     */
    public AnnotationGroup createAnnotationGroup(String groupName) {
        AnnotationGroup defaultGroup = new AnnotationGroup();
        defaultGroup.setName(groupName);
        defaultGroup.setStudy(this);
        getAnnotationGroups().add(defaultGroup);
        return defaultGroup;
    }

    /**
     * Gets all visible AnnotationFieldDescriptors that have AnnotationDefinitions that aren't null.
     * @return visible AFDs.
     */
    public Set<AnnotationFieldDescriptor> getAllVisibleAnnotationFieldDescriptors() {
        Set<AnnotationFieldDescriptor> visibleSet = new HashSet<AnnotationFieldDescriptor>();
        for (AnnotationGroup group : getAnnotationGroups()) {
            visibleSet.addAll(getVisibleAnnotationCollection(group.getName()));
        }
        return visibleSet;
    }

    /**
     * Gets all visible AnnotationFieldDescriptors that have AnnotationDefinitions and belong to the given group.
     * @param groupName containing the AFDs.
     * @return visible AFDs in the group.
     */
    public Set<AnnotationFieldDescriptor> getVisibleAnnotationCollection(String groupName) {
        Set<AnnotationFieldDescriptor> visibleSet = new HashSet<AnnotationFieldDescriptor>();
        AnnotationGroup group = getAnnotationGroup(groupName);
        if (group != null) {
            visibleSet.addAll(group.getVisibleAnnotationFieldDescriptors());
        }
        return visibleSet;
    }

    /**
     * Gets all visible AnnotationFieldDescriptors that have AnnotationDefinitions that aren't null, and belong
     * to the given entityType and annotationType.  If entityType or annotationType is null, it will ignore them.
     * @param entityType of the annotations.
     * @param annotationType of the annotations.
     * @return visible AFDs.
     */
    public Set<AnnotationFieldDescriptor> getAllVisibleAnnotationFieldDescriptors(EntityTypeEnum entityType,
            AnnotationTypeEnum annotationType) {
        Set<AnnotationFieldDescriptor> validSet = new HashSet<AnnotationFieldDescriptor>();

        for (AnnotationFieldDescriptor fieldDescriptor : getAllVisibleAnnotationFieldDescriptors()) {
            boolean matchesAnnotationType = (annotationType == null || annotationType.equals(fieldDescriptor
                    .getDefinition().getDataType()));
            boolean matchesEntityType = (entityType == null || entityType.equals(fieldDescriptor
                    .getAnnotationEntityType()));
            if (matchesAnnotationType && matchesEntityType) {
                validSet.add(fieldDescriptor);
            }
        }
        return validSet;
    }

    /**
     * @return the publiclyAccessible
     */
    public Boolean isPubliclyAccessible() {
        return publiclyAccessible;
    }

    /**
     * @param publiclyAccessible the publiclyAccessible to set
     */
    public void setPubliclyAccessible(Boolean publiclyAccessible) {
        this.publiclyAccessible = publiclyAccessible;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the enabled
     */
    public Boolean isEnabled() {
        return enabled;
    }
}