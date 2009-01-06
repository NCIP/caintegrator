package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.genomic.SampleAcquisition;

/**
 * 
 */
public class GenomicDataResultColumn extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Integer columnIndex;
    private SampleAcquisition sampleAcquisition;
    
    /**
     * @return the columnIndex
     */
    public Integer getColumnIndex() {
        return columnIndex;
    }
    
    /**
     * @param columnIndex the columnIndex to set
     */
    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
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

}