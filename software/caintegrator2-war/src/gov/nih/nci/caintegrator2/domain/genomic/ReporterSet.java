package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 */
public class ReporterSet extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String reporterType;
    private Set<ArrayData> arrayDataCollection = new HashSet<ArrayData>();
    private Set<AbstractReporter> reporters = new HashSet<AbstractReporter>();
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

    /**
     * @return the arrayDataCollection
     */
    public Set<ArrayData> getArrayDataCollection() {
        return arrayDataCollection;
    }

    /**
     * @param arrayDataCollection the arrayDataCollection to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setArrayDataCollection(Set<ArrayData> arrayDataCollection) {
        this.arrayDataCollection = arrayDataCollection;
    }

    /**
     * @return the reporters
     */
    public Set<AbstractReporter> getReporters() {
        return reporters;
    }

    /**
     * @param reporters the reporters to set
     */
    @SuppressWarnings("unused") // Required by Hibernate
    private void setReporters(Set<AbstractReporter> reporters) {
        this.reporters = reporters;
    }

}