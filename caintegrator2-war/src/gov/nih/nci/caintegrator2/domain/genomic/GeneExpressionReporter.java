package gov.nih.nci.caintegrator2.domain.genomic;

/**
 * 
 */
public class GeneExpressionReporter extends AbstractReporter {

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

    /**
     * {@inheritDoc}
     */
    public int compareTo(AbstractReporter reporter) {
        int geneComparison = getGeneComparison((GeneExpressionReporter) reporter);
        if (geneComparison == 0) {
            return getName().compareTo(reporter.getName());
        } else {
            return geneComparison;
        }
    }

    private int getGeneComparison(GeneExpressionReporter reporter) {
        if (getGene() == null) {
            return reporter.getGene() == null ? 0 : 1;
        } else if (reporter.getGene() == null) {
            return -1;
        } else {
            return getGene().compareTo(reporter.getGene());
        }
    }

}