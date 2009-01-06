package gov.nih.nci.caintegrator2.domain.imaging;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.StudySubjectAssignment;
import gov.nih.nci.caintegrator2.domain.translational.Timepoint;

import java.util.Collection;

/**
 * 
 */
public class ImageSeriesAcquisition extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String identifier;
    private String nciaTrialIdentifier;
    private Collection<ImageSeries> seriesCollection;
    private StudySubjectAssignment assignment;
    private Timepoint timepoint;
    
    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }
    
    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    /**
     * @return the nciaTrialIdentifier
     */
    public String getNciaTrialIdentifier() {
        return nciaTrialIdentifier;
    }
    
    /**
     * @param nciaTrialIdentifier the nciaTrialIdentifier to set
     */
    public void setNciaTrialIdentifier(String nciaTrialIdentifier) {
        this.nciaTrialIdentifier = nciaTrialIdentifier;
    }
    
    /**
     * @return the seriesCollection
     */
    public Collection<ImageSeries> getSeriesCollection() {
        return seriesCollection;
    }
    
    /**
     * @param seriesCollection the seriesCollection to set
     */
    public void setSeriesCollection(Collection<ImageSeries> seriesCollection) {
        this.seriesCollection = seriesCollection;
    }
    
    /**
     * @return the assignment
     */
    public StudySubjectAssignment getAssignment() {
        return assignment;
    }
    
    /**
     * @param assignment the assignment to set
     */
    public void setAssignment(StudySubjectAssignment assignment) {
        this.assignment = assignment;
    }
    
    /**
     * @return the timepoint
     */
    public Timepoint getTimepoint() {
        return timepoint;
    }
    
    /**
     * @param timepoint the timepoint to set
     */
    public void setTimepoint(Timepoint timepoint) {
        this.timepoint = timepoint;
    }

}