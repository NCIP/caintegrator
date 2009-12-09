package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class GenomicDataResultColumn extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;

    private SampleAcquisition sampleAcquisition;
    private List<GenomicDataResultValue> values;
    private GenomicDataQueryResult result;
    private int columnIndex;
    
    
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
        values = new ArrayList<GenomicDataResultValue>(getResult().getRowCollection().size());
        for (GenomicDataResultRow row : getResult().getRowCollection()) {
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