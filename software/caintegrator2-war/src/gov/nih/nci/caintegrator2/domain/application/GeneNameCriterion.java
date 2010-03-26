package gov.nih.nci.caintegrator2.domain.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    /**
     * @return the gene symbols.
     */
    public Set<String> getGeneSymbols() {
        Set<String> geneSymbols = new HashSet<String>();
        geneSymbols.addAll(Arrays.asList(getGeneSymbol().replaceAll("\\s*", "").split(",")));
        return geneSymbols;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> getGeneSymbolsInCriterion() {
        return new ArrayList<String>(getGeneSymbols());
    }

}