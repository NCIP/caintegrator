package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class Platform extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String vendor;
    private Collection<ReporterSet> reporterSets;
    
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
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }
    
    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    /**
     * @return the reporterSets
     */
    public Collection<ReporterSet> getReporterSets() {
        return reporterSets;
    }
    
    /**
     * @param reporterSets the reporterSets to set
     */
    public void setReporterSets(Collection<ReporterSet> reporterSets) {
        this.reporterSets = reporterSets;
    }

}