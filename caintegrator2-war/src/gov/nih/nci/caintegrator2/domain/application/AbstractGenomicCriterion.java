package gov.nih.nci.caintegrator2.domain.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

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
    @Override
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
        this.geneSymbol = geneSymbol.toUpperCase(Locale.getDefault());
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
    @Override
    protected List<String> getGeneSymbolsInCriterion() {
        return new ArrayList<String>(getGeneSymbols());
    }

}