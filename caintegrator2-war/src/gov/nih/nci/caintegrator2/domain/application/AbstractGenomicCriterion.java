package gov.nih.nci.caintegrator2.domain.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.xwork.StringUtils;

/**
 * 
 */
public abstract class AbstractGenomicCriterion extends AbstractCriterion implements Cloneable {

    private static final long serialVersionUID = 1L;
    private String geneSymbol = "";
    private String platformName = "";
    
    /**
     * {@inheritDoc}
     */
    protected AbstractGenomicCriterion clone() throws CloneNotSupportedException {
        return (AbstractGenomicCriterion) super.clone();
    }
    
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
        if (StringUtils.isBlank(geneSymbol)) {
            return geneSymbols;
        }
        geneSymbols.addAll(Arrays.asList(getGeneSymbol().replaceAll("\\s*", "").split(",")));
        return geneSymbols;
    }
    
    /**
     * @param platformName the platformName to set
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * {@inheritDoc}
     */
    public String getPlatformName() {
        return platformName;
    }

    /**
     * {@inheritDoc}
     */
    protected List<String> getGeneSymbolsInCriterion() {
        return new ArrayList<String>(getGeneSymbols());
    }

}