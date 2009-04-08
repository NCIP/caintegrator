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
    private Study study;
    private Array array;
    private ArrayDataType type;
    
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

    /**
     * Validity check to ensure that this <code>ArrayData</code> object is associated with a <code>Study</code>.
     */
    public void checkHasStudy() {
        if (getStudy() == null) {
            throw new IllegalArgumentException("Null Study in ArrayData with id: " + getId());
        }
    }

    /**
     * @return the type
     */
    public ArrayDataType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ArrayDataType type) {
        this.type = type;
    }

}