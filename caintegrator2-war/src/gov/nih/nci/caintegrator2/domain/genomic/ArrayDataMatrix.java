package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.translational.Study;

import java.util.Collection;

/**
 * 
 */
public class ArrayDataMatrix extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    
    private ReporterList reporterList;
    private Collection<ArrayData> sampleDataCollection;
    private Study study;
    
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
     * @return the sampleDataCollection
     */
    public Collection<ArrayData> getSampleDataCollection() {
        return sampleDataCollection;
    }
    
    /**
     * @param sampleDataCollection the sampleDataCollection to set
     */
    public void setSampleDataCollection(Collection<ArrayData> sampleDataCollection) {
        this.sampleDataCollection = sampleDataCollection;
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

}