package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

import java.util.Collection;

/**
 * 
 */
public class Sample extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private SampleAcquisition sampleAcquisition;
    private Collection<ArrayData> arrayDataCollection;
    private Collection<Array> arrayCollection;
    
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
     * @return the arrayCollection
     */
    public Collection<Array> getArrayCollection() {
        return arrayCollection;
    }
    
    /**
     * @param arrayCollection the arrayCollection to set
     */
    public void setArrayCollection(Collection<Array> arrayCollection) {
        this.arrayCollection = arrayCollection;
    }

}