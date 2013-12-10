/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator.domain.genomic.SampleSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a genomic data result row.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenomicDataResultRow implements GenomicDataResultComparable {
    private AbstractReporter reporter;
    private SegmentDataResultValue segmentDataResultValue;
    private final List<GenomicDataResultValue> values = new ArrayList<GenomicDataResultValue>();
    private boolean hasMatchingValues = false;
    private float sortedValue;

    /**
     * {@inheritDoc}
     */
    public float getSortedValue() {
        return sortedValue;
    }

    /**
     * @param sortedValue the sortedValue to set
     */
    public void setSortedValue(float sortedValue) {
        this.sortedValue = sortedValue;
    }

    /**
     * @return the reporter
     */
    public AbstractReporter getReporter() {
        return reporter;
    }

    /**
     * @param reporter the reporter to set
     */
    public void setReporter(AbstractReporter reporter) {
        this.reporter = reporter;
    }
    /**
     * @return the values
     */
    public List<GenomicDataResultValue> getValues() {
        return values;
    }

    /**
     * @param sampleSet the set of samples to be excluded from the values.
     */
    void excludeSampleSet(SampleSet sampleSet) {
        List<GenomicDataResultValue> removedValues = new ArrayList<GenomicDataResultValue>();
        for (GenomicDataResultValue value : values) {
            if (sampleSet.contains(value.getColumn().getSampleAcquisition().getSample())) {
                removedValues.add(value);
            }
        }
        values.removeAll(removedValues);
    }

    /**
     * @return the hasMatchingValues
     */
    public boolean isHasMatchingValues() {
        return hasMatchingValues;
    }

    /**
     * @param hasMatchingValues the hasMatchingValues to set
     */
    public void setHasMatchingValues(boolean hasMatchingValues) {
        this.hasMatchingValues = hasMatchingValues;
    }

    /**
     * @return the segmentDataResultValue
     */
    public SegmentDataResultValue getSegmentDataResultValue() {
        return segmentDataResultValue;
    }

    /**
     * @param segmentDataResultValue the segmentDataResultValue to set
     */
    public void setSegmentDataResultValue(SegmentDataResultValue segmentDataResultValue) {
        this.segmentDataResultValue = segmentDataResultValue;
    }

}
