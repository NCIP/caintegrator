package gov.nih.nci.caintegrator2.domain.translational;

import gov.nih.nci.caintegrator2.application.study.StudyConfiguration;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.genomic.ArrayData;
import gov.nih.nci.caintegrator2.domain.genomic.ReporterTypeEnum;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class Study extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;

    private String longTitleText;
    private String shortTitleText;
    private Collection<Timepoint> timepointCollection;
    private Collection<SurvivalValueDefinition> survivalValueDefinitionCollection;
    private Set<Sample> controlSampleCollection = new HashSet<Sample>();
    private Collection<AnnotationDefinition> imageSeriesAnnotationCollection;
    private Set<StudySubjectAssignment> assignmentCollection = new HashSet<StudySubjectAssignment>();
    private Collection<AnnotationDefinition> subjectAnnotationCollection;
    private Timepoint defaultTimepoint;
    private Collection<AnnotationDefinition> sampleAnnotationCollection;
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
    public Collection<Timepoint> getTimepointCollection() {
        return timepointCollection;
    }

    /**
     * @param timepointCollection
     *            the timepointCollection to set
     */
    public void setTimepointCollection(Collection<Timepoint> timepointCollection) {
        this.timepointCollection = timepointCollection;
    }

    /**
     * @return the survivalValueDefinitionCollection
     */
    public Collection<SurvivalValueDefinition> getSurvivalValueDefinitionCollection() {
        return survivalValueDefinitionCollection;
    }

    /**
     * @param survivalValueDefinitionCollection
     *            the survivalValueDefinitionCollection to set
     */
    public void setSurvivalValueDefinitionCollection(
            Collection<SurvivalValueDefinition> survivalValueDefinitionCollection) {
        this.survivalValueDefinitionCollection = survivalValueDefinitionCollection;
    }

    /**
     * @return the imageSeriesAnnotationCollection
     */
    public Collection<AnnotationDefinition> getImageSeriesAnnotationCollection() {
        return imageSeriesAnnotationCollection;
    }

    /**
     * @param imageSeriesAnnotationCollection
     *            the imageSeriesAnnotationCollection to set
     */
    public void setImageSeriesAnnotationCollection(Collection<AnnotationDefinition> imageSeriesAnnotationCollection) {
        this.imageSeriesAnnotationCollection = imageSeriesAnnotationCollection;
    }

    /**
     * @return the subjectAnnotationCollection
     */
    public Collection<AnnotationDefinition> getSubjectAnnotationCollection() {
        return subjectAnnotationCollection;
    }

    /**
     * @param subjectAnnotationCollection
     *            the subjectAnnotationCollection to set
     */
    public void setSubjectAnnotationCollection(Collection<AnnotationDefinition> subjectAnnotationCollection) {
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
    public Collection<AnnotationDefinition> getSampleAnnotationCollection() {
        return sampleAnnotationCollection;
    }

    /**
     * @param sampleAnnotationCollection
     *            the sampleAnnotationCollection to set
     */
    public void setSampleAnnotationCollection(Collection<AnnotationDefinition> sampleAnnotationCollection) {
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
     * @return the controlSampleCollection
     */
    public Set<Sample> getControlSampleCollection() {
        return controlSampleCollection;
    }

    /**
     * @param controlSampleCollection the controlSampleCollection to set
     */
    @SuppressWarnings("unused") // required by Hibernate
    private void setControlSampleCollection(Set<Sample> controlSampleCollection) {
        this.controlSampleCollection = controlSampleCollection;
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

}