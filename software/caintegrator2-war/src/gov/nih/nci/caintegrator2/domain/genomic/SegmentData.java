/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * Represents data for a single chromosomal segment copy number variation.
 */
public class SegmentData extends AbstractCaIntegrator2Object implements Comparable<SegmentData> {

    private static final long serialVersionUID = 1L;
    
    private Integer numberOfMarkers;
    private Float segmentValue;
    private ChromosomalLocation location;
    private ArrayData arrayData;

    /**
     * {@inheritDoc}
     */
    public int compareTo(SegmentData segmentData) {
        if (getLocation() == null) {
            return segmentData.getLocation() == null ? 0 : 1;
        } else if (segmentData.getLocation() == null) {
            return -1;
        } else {
            return location.compareTo(segmentData.getLocation());
        }
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

    /**
     * @return the location
     */
    public ChromosomalLocation getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(ChromosomalLocation location) {
        this.location = location;
    }

    /**
     * @return the arrayData
     */
    public ArrayData getArrayData() {
        return arrayData;
    }

    /**
     * @param arrayData the arrayData to set
     */
    public void setArrayData(ArrayData arrayData) {
        this.arrayData = arrayData;
    }

}
