package gov.nih.nci.caintegrator2.domain.application;

/**
 * 
 */
public class GeneListCriterion extends AbstractGenomicCriterion {

    private static final long serialVersionUID = 1L;
    
    private GeneList geneList;

    /**
     * @return the geneList
     */
    public GeneList getGeneList() {
        return geneList;
    }

    /**
     * @param geneList the geneList to set
     */
    public void setGeneList(GeneList geneList) {
        this.geneList = geneList;
    }

}