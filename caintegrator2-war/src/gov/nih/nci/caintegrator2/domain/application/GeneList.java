package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.genomic.Gene;

import java.util.Collection;

/**
 * 
 */
public class GeneList extends AbstractList {

    private static final long serialVersionUID = 1L;
    
    private Collection<Gene> geneCollection;

    /**
     * @return the geneCollection
     */
    public Collection<Gene> getGeneCollection() {
        return geneCollection;
    }

    /**
     * @param geneCollection the geneCollection to set
     */
    public void setGeneCollection(Collection<Gene> geneCollection) {
        this.geneCollection = geneCollection;
    }

}