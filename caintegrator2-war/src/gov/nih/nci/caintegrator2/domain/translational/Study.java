package gov.nih.nci.caintegrator2.domain.translational;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.util.Collection;

/**
 * 
 */
public class Study extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;

    private String longTitleText;

    private String shortTitleText;

    private Collection<Timepoint> timepointCollection;

    private Collection<SurvivalValueDefinition> survivalValueDefinitionCollection;

    private Collection<Sample> controlSampleCollection;

    private Collection<AnnotationDefinition> imageSeriesAnnotationCollection;

    private Collection<StudySubjectAssignment> assignmentCollection;

    private Collection<AnnotationDefinition> subjectAnnotationCollection;

    private Timepoint defaultTimepoint;

    private Collection<AnnotationDefinition> sampleAnnotationCollection;

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
     * @return the controlSampleCollection
     */
    public Collection<Sample> getControlSampleCollection() {
        return controlSampleCollection;
    }

    /**
     * @param controlSampleCollection
     *            the controlSampleCollection to set
     */
    public void setControlSampleCollection(Collection<Sample> controlSampleCollection) {
        this.controlSampleCollection = controlSampleCollection;
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
     * @return the assignmentCollection
     */
    public Collection<StudySubjectAssignment> getAssignmentCollection() {
        return assignmentCollection;
    }

    /**
     * @param assignmentCollection
     *            the assignmentCollection to set
     */
    public void setAssignmentCollection(Collection<StudySubjectAssignment> assignmentCollection) {
        this.assignmentCollection = assignmentCollection;
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

}