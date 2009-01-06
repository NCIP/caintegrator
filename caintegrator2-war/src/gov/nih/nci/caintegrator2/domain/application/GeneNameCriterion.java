package gov.nih.nci.caintegrator2.domain.application;

/**
 * 
 */
public class GeneNameCriterion extends AbstractGenomicCriterion {

    private static final long serialVersionUID = 1L;
    
    private String geneSymbol;

    /**
     * @return the geneSymbol
     */
    public String getGeneSymbol() {
        return geneSymbol;
    }

    /**
     * @param geneSymbol the geneSymbol to set
     */
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

}