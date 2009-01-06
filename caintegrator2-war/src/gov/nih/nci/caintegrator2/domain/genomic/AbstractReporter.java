package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class AbstractReporter extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private ReporterSet reporterSet;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the reporterSet
     */
    public ReporterSet getReporterSet() {
        return reporterSet;
    }
    
    /**
     * @param reporterSet the reporterSet to set
     */
    public void setReporterSet(ReporterSet reporterSet) {
        this.reporterSet = reporterSet;
    }

}