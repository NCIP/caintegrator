package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class ReporterSet extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String reporterType;
    private Collection<ArrayData> arrayDataCollection;
    private Collection<AbstractReporter> reporters;
    private Platform platform;
    
    /**
     * @return the reporterType
     */
    public String getReporterType() {
        return reporterType;
    }
    
    /**
     * @param reporterType the reporterType to set
     */
    public void setReporterType(String reporterType) {
        this.reporterType = reporterType;
    }
    
    /**
     * @return the arrayDataCollection
     */
    public Collection<ArrayData> getArrayDataCollection() {
        return arrayDataCollection;
    }
    
    /**
     * @param arrayDataCollection the arrayDataCollection to set
     */
    public void setArrayDataCollection(Collection<ArrayData> arrayDataCollection) {
        this.arrayDataCollection = arrayDataCollection;
    }
    
    /**
     * @return the reporters
     */
    public Collection<AbstractReporter> getReporters() {
        return reporters;
    }
    
    /**
     * @param reporters the reporters to set
     */
    public void setReporters(Collection<AbstractReporter> reporters) {
        this.reporters = reporters;
    }
    
    /**
     * @return the platform
     */
    public Platform getPlatform() {
        return platform;
    }
    
    /**
     * @param platform the platform to set
     */
    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

}