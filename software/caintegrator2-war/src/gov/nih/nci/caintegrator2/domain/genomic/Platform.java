package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.application.arraydata.PlatformVendorEnum;
import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class Platform extends AbstractCaIntegrator2Object {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private PlatformVendorEnum vendor;
    private Set<ReporterList> reporterLists = new HashSet<ReporterList>();
    
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
    public PlatformVendorEnum getVendor() {
        return vendor;
    }
    
    /**
     * @param vendor the vendor to set
     */
    public void setVendor(PlatformVendorEnum vendor) {
        this.vendor = vendor;
    }
    
    /**
     * @return the reporterLists
     */
    public Set<ReporterList> getReporterLists() {
        return reporterLists;
    }
    
    /**
     * @param reporterLists the reporterLists to set
     */
    @SuppressWarnings("unused")     // required by Hibernate
    private void setReporterLists(Set<ReporterList> reporterLists) {
        this.reporterLists = reporterLists;
    }

}