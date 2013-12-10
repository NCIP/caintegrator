/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.domain.genomic.SampleAcquisition;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a genomic data result column.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GenomicDataResultColumn implements GenomicDataResultComparable {
    private SampleAcquisition sampleAcquisition;
    private List<GenomicDataResultValue> values;
    private GenomicDataQueryResult result;
    private int columnIndex;
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
     * @return the sampleAcquisition
     */
    public SampleAcquisition getSampleAcquisition() {
        return sampleAcquisition;
    }

    /**
     * @param sampleAcquisition the sampleAcquisition to set
     */
    public void setSampleAcquisition(SampleAcquisition sampleAcquisition) {
        this.sampleAcquisition = sampleAcquisition;
    }

    /**
     * Returns the values for this column.
     *
     * @return the values.
     */
    public List<GenomicDataResultValue> getValues() {
        if (values == null) {
            loadValues();
        }
        return values;
    }

    private void loadValues() {
        values = new ArrayList<GenomicDataResultValue>(getResult().getFilteredRowCollection().size());
        for (GenomicDataResultRow row : getResult().getFilteredRowCollection()) {
            values.add(row.getValues().get(getColumnIndex()));
        }
    }

    /**
     * @return the result
     */
    public GenomicDataQueryResult getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(GenomicDataQueryResult result) {
        this.result = result;
    }

    int getColumnIndex() {
        return columnIndex;
    }

    void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

}
