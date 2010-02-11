package gov.nih.nci.caintegrator2.domain.translational;

import gov.nih.nci.caintegrator2.application.study.AnnotationGroup;
import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
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
    // This will be @Deprecated.
    private Set<AnnotationDefinition> imageSeriesAnnotationCollection = new HashSet<AnnotationDefinition>(); 
    private Set<StudySubjectAssignment> assignmentCollection = new HashSet<StudySubjectAssignment>();
    // This will be @Deprecated.
    private Set<AnnotationDefinition> subjectAnnotationCollection = new HashSet<AnnotationDefinition>();
    private Timepoint defaultTimepoint;
    // This will be @Deprecated.
    private Set<AnnotationDefinition> sampleAnnotationCollection = new HashSet<AnnotationDefinition>();
    private Set<AnnotationGroup> annotationGroups = new HashSet<AnnotationGroup>();
    private StudyConfiguration studyConfiguration;

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
     * @return the imageSeriesAnnotationCollection
     */
    public Set<AnnotationDefinition> getImageSeriesAnnotationCollection() {
        return imageSeriesAnnotationCollection;
    }

    /**
     * @param imageSeriesAnnotationCollection
     *            the imageSeriesAnnotationCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setImageSeriesAnnotationCollection(Set<AnnotationDefinition> imageSeriesAnnotationCollection) {
        this.imageSeriesAnnotationCollection = imageSeriesAnnotationCollection;
    }

    /**
     * @return the subjectAnnotationCollection
     */
    public Set<AnnotationDefinition> getSubjectAnnotationCollection() {
        return subjectAnnotationCollection;
    }

    /**
     * @param subjectAnnotationCollection
     *            the subjectAnnotationCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setSubjectAnnotationCollection(Set<AnnotationDefinition> subjectAnnotationCollection) {
        this.subjectAnnotationCollection = subjectAnnotationCollection;
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
     * @return the sampleAnnotationCollection
     */
    public Set<AnnotationDefinition> getSampleAnnotationCollection() {
        return sampleAnnotationCollection;
    }

    /**
     * @param sampleAnnotationCollection
     *            the sampleAnnotationCollection to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setSampleAnnotationCollection(Set<AnnotationDefinition> sampleAnnotationCollection) {
        this.sampleAnnotationCollection = sampleAnnotationCollection;
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
     * @return an immutable set of the matching array datas.
     */
    public Set<ArrayData> getArrayDatas(ReporterTypeEnum reporterType) {
        Set<ArrayData> arrayDatas = new HashSet<ArrayData>();
        for (Sample sample : getSamples()) {
            arrayDatas.addAll(sample.getArrayDatas(reporterType));
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
     * @return the boolean of whether the study has clinical data 
     */
    public boolean hasClinicalData() {
        return subjectAnnotationCollection != null
            && !subjectAnnotationCollection.isEmpty();
    }

    /**
     * @return the boolean of whether the study has Genomic data 
     */
    public boolean hasGenomicDataSources() {
        return studyConfiguration != null
            && studyConfiguration.hasGenomicDataSources();
    }

    /**
     * @return the boolean of whether the study has Image data 
     */
    public boolean hasImageDataSources() {
        return studyConfiguration != null
            && studyConfiguration.hasImageDataSources();
    }

    /**
     * @return the boolean of whether the study has ImageSeries data 
     */
    public boolean hasImageSeriesData() {
        return imageSeriesAnnotationCollection != null 
            && !imageSeriesAnnotationCollection.isEmpty();
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
     * 
     * @return all annotation groups, sorted by name.
     */
    public List<AnnotationGroup> getSortedAnnotationGroups() {
        List<AnnotationGroup> annotationGroupList = new ArrayList<AnnotationGroup>(annotationGroups);
        Collections.sort(annotationGroupList);
        return annotationGroupList;
    }
    
    /**
     * 
     * @param entityType for the groups to retrieve.
     * @return sorted annotation groups, of the given entity type.
     */
    public List<AnnotationGroup> getSortedAnnotationGroupsForEntityType(EntityTypeEnum entityType) {
        List<AnnotationGroup> sortedAnnotationGroups = new ArrayList<AnnotationGroup>();
        for (AnnotationGroup annotationGroup : getSortedAnnotationGroups()) {
            if (entityType.equals(annotationGroup.getAnnotationEntityType())) {
                sortedAnnotationGroups.add(annotationGroup);
            }
        }
        return sortedAnnotationGroups;
    }

}