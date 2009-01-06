package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.genomic.Gene;

/**
 * 
 */
public class GeneCriterion extends AbstractGenomicCriterion {

    private static final long serialVersionUID = 1L;
    
    private Gene gene;

    /**
     * @return the gene
     */
    public Gene getGene() {
        return gene;
    }

    /**
     * @param gene the gene to set
     */
    public void setGene(Gene gene) {
        this.gene = gene;
    }

}