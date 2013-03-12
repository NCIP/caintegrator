/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.search;

/**
 * Wrapper that contains the results of a biodbnet gene search.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GeneResults implements Comparable<GeneResults> {

    private Long geneId;
    private String symbol;
    private String taxon;
    private String description;
    private String aliases;

    /**
     * @return the geneId
     */
    public Long getGeneId() {
        return geneId;
    }

    /**
     * @param geneId the geneId to set
     */
    public void setGeneId(Long geneId) {
        this.geneId = geneId;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    /**
     * @return the taxon
     */
    public String getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    public void setTaxon(String taxon) {
        this.taxon = taxon;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the aliases
     */
    public String getAliases() {
        return aliases;
    }

    /**
     * @param aliases the aliases to set
     */
    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(GeneResults other) {
        return this.getGeneId().compareTo(other.getGeneId());
    }
}
