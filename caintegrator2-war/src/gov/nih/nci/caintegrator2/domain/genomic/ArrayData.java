package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

/**
 * 
 */
public class ArrayData extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private ReporterList reporterList;
    private Sample sample;
    private ArrayDataMatrix matrix;
    private Study study;
    private Array array;
    
    /**
     * @return the reporterList
     */
    public ReporterList getReporterList() {
        return reporterList;
    }
    
    /**
     * @param reporterList the reporterList to set
     */
    public void setReporterList(ReporterList reporterList) {
        this.reporterList = reporterList;
    }
    
    /**
     * @return the sample
     */
    public Sample getSample() {
        return sample;
    }
    
    /**
     * @param sample the sample to set
     */
    public void setSample(Sample sample) {
        this.sample = sample;
    }
    
    /**
     * @return the matrix
     */
    public ArrayDataMatrix getMatrix() {
        return matrix;
    }
    
    /**
     * @param matrix the matrix to set
     */
    public void setMatrix(ArrayDataMatrix matrix) {
        this.matrix = matrix;
    }
    
    /**
     * @return the study
     */
    public Study getStudy() {
        return study;
    }
    
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    
    /**
     * @return the array
     */
    public Array getArray() {
        return array;
    }
    
    /**
     * @param array the array to set
     */
    public void setArray(Array array) {
        this.array = array;
    }
    
    /**
     * The reporter type for this data.
     *  
     * @return the reporter type.
     */
    public ReporterTypeEnum getReporterType() {
        return getReporterList().getReporterType();
    }

}