/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.genomic;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

/**
 * Represents data for a single chromosomal segment copy number variation.
 */
public class SegmentData extends AbstractCaIntegrator2Object implements Comparable<SegmentData> {

    private static final long serialVersionUID = 1L;

    private int numberOfMarkers;
    private float segmentValue;
    private int callsValue;
    private float probabilityAmplification;
    private float probabilityGain;
    private float probabilityLoss;
    private float probabilityNormal;
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
    public int getNumberOfMarkers() {
        return numberOfMarkers;
    }

    /**
     * @param numberOfMarkers the numberOfMarkers to set
     */
    public void setNumberOfMarkers(int numberOfMarkers) {
        this.numberOfMarkers = numberOfMarkers;
    }

    /**
     * @return the segmentValue
     */
    public float getSegmentValue() {
        return segmentValue;
    }

    /**
     * @param segmentValue the segmentValue to set
     */
    public void setSegmentValue(float segmentValue) {
        this.segmentValue = segmentValue;
    }

    /**
     * @return the callsValue
     */
    public int getCallsValue() {
        return callsValue;
    }

    /**
     * @param callsValue the callsValue to set
     */
    public void setCallsValue(int callsValue) {
        this.callsValue = callsValue;
    }

    /**
     * @return the probabilityAmplification
     */
    public float getProbabilityAmplification() {
        return probabilityAmplification;
    }

    /**
     * @param probabilityAmplification the probabilityAmplification to set
     */
    public void setProbabilityAmplification(float probabilityAmplification) {
        this.probabilityAmplification = probabilityAmplification;
    }

    /**
     * @return the probabilityGain
     */
    public float getProbabilityGain() {
        return probabilityGain;
    }

    /**
     * @param probabilityGain the probabilityGain to set
     */
    public void setProbabilityGain(float probabilityGain) {
        this.probabilityGain = probabilityGain;
    }

    /**
     * @return the probabilityLoss
     */
    public float getProbabilityLoss() {
        return probabilityLoss;
    }

    /**
     * @param probabilityLoss the probabilityLoss to set
     */
    public void setProbabilityLoss(float probabilityLoss) {
        this.probabilityLoss = probabilityLoss;
    }

    /**
     * @return the probabilityNormal
     */
    public float getProbabilityNormal() {
        return probabilityNormal;
    }

    /**
     * @param probabilityNormal the probabilityNormal to set
     */
    public void setProbabilityNormal(float probabilityNormal) {
        this.probabilityNormal = probabilityNormal;
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
