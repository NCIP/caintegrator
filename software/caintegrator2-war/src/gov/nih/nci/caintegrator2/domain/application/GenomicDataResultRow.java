package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.AbstractReporter;
import gov.nih.nci.caintegrator2.domain.genomic.SampleSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GenomicDataResultRow extends AbstractCaIntegrator2Object implements GenomicDataResultComparable {

    private static final long serialVersionUID = 1L;
    
    private AbstractReporter reporter;
    private final List<GenomicDataResultValue> values = new ArrayList<GenomicDataResultValue>();
    private boolean hasMatchingValues = false;
    private int nonFilterIndex;
    private Float sortedValue;

    /**
     * {@inheritDoc}
     */
    public Float getSortedValue() {
        return sortedValue;
    }

    /**
     * @param sortedValue the sortedValue to set
     */
    public void setSortedValue(Float sortedValue) {
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
     * @return the nonFilterIndex
     */
    public int getNonFilterIndex() {
        return nonFilterIndex;
    }

    /**
     * @param nonFilterIndex the nonFilterIndex to set
     */
    public void setNonFilterIndex(int nonFilterIndex) {
        this.nonFilterIndex = nonFilterIndex;
    }

}
