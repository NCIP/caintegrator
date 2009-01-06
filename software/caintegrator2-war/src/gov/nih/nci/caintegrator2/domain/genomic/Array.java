package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class Array extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private Platform platform;
    private Collection<Sample> sampleCollection;
    private Collection<ArrayData> arrayDataCollection;
    
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
     * @return the sampleCollection
     */
    public Collection<Sample> getSampleCollection() {
        return sampleCollection;
    }
    
    /**
     * @param sampleCollection the sampleCollection to set
     */
    public void setSampleCollection(Collection<Sample> sampleCollection) {
        this.sampleCollection = sampleCollection;
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

}