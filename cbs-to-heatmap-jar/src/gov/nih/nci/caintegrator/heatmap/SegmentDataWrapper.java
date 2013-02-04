/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.heatmap;

/**
 * Wrapper for the SegmentData intended to bridge the gap between caIntegrator and CBS2Heatmap.
 */
public class SegmentDataWrapper {
    private String sampleIdentifier;
    private String chromosome;   
    private Integer startPosition;
    private Integer stopPosition;
    private Integer numberOfMarkers;
    private Float segmentValue;
    
    
    /**
     * @return the sampleIdentifier
     */
    public String getSampleIdentifier() {
        return sampleIdentifier;
    }
    /**
     * @param sampleIdentifier the sampleIdentifier to set
     */
    public void setSampleIdentifier(String sampleIdentifier) {
        this.sampleIdentifier = sampleIdentifier;
    }
    /**
     * @return the chromosome
     */
    public String getChromosome() {
        return chromosome;
    }
    /**
     * @param chromosome the chromosome to set
     */
    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }
    /**
     * @return the startPosition
     */
    public Integer getStartPosition() {
        return startPosition;
    }
    /**
     * @param startPosition the startPosition to set
     */
    public void setStartPosition(Integer startPosition) {
        this.startPosition = startPosition;
    }
    /**
     * @return the stopPosition
     */
    public Integer getStopPosition() {
        return stopPosition;
    }
    /**
     * @param stopPosition the stopPosition to set
     */
    public void setStopPosition(Integer stopPosition) {
        this.stopPosition = stopPosition;
    }
    /**
     * @return the numberOfMarkers
     */
    public Integer getNumberOfMarkers() {
        return numberOfMarkers;
    }
    /**
     * @param numberOfMarkers the numberOfMarkers to set
     */
    public void setNumberOfMarkers(Integer numberOfMarkers) {
        this.numberOfMarkers = numberOfMarkers;
    }
    /**
     * @return the segmentValue
     */
    public Float getSegmentValue() {
        return segmentValue;
    }
    /**
     * @param segmentValue the segmentValue to set
     */
    public void setSegmentValue(Float segmentValue) {
        this.segmentValue = segmentValue;
    }

    
}
