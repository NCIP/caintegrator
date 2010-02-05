package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.genomic.Sample;

import java.util.Collection;

/**
 * 
 */
public class SampleList extends AbstractList {

    private static final long serialVersionUID = 1L;
    
    private Collection<Sample> sampleCollection;

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

}