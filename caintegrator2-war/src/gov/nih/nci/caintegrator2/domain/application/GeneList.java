package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.genomic.Gene;

import java.util.Collection;
import java.util.HashSet;

/**
 * 
 */
public class GeneList extends AbstractList {

    private static final long serialVersionUID = 1L;
    
    private Collection<Gene> geneCollection = new HashSet<Gene>();

    /**
     * @return the geneCollection
     */
    public Collection<Gene> getGeneCollection() {
        return geneCollection;
    }

    /**
     * @param geneCollection the geneCollection to set
     */
    @SuppressWarnings("unused") // For use by Hibernate
    private void setGeneCollection(Collection<Gene> geneCollection) {
        this.geneCollection = geneCollection;
    }

}