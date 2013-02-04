/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

/**
 * 
 */
public class CaBioDisplayableGene implements Comparable<CaBioDisplayableGene> {
    
    private Long id;
    private String symbol;
    private String fullName;
    private String taxonCommonName;
    private String hugoSymbol;
    private String geneAliases;
    private String databaseCrossReferences;
    
    /**
     * @return the geneAliases
     */
    public String getGeneAliases() {
        return geneAliases;
    }
    /**
     * @param geneAliases the geneAliases to set
     */
    public void setGeneAliases(String geneAliases) {
        this.geneAliases = geneAliases;
    }
    /**
     * @return the databaseCrossReferences
     */
    public String getDatabaseCrossReferences() {
        return databaseCrossReferences;
    }
    /**
     * @param databaseCrossReferences the databaseCrossReferences to set
     */
    public void setDatabaseCrossReferences(String databaseCrossReferences) {
        this.databaseCrossReferences = databaseCrossReferences;
    }
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
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
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }
    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    /**
     * @return the taxonCommonName
     */
    public String getTaxonCommonName() {
        return taxonCommonName;
    }
    /**
     * @param taxonCommonName the taxonCommonName to set
     */
    public void setTaxonCommonName(String taxonCommonName) {
        this.taxonCommonName = taxonCommonName;
    }

    /**
     * @return the hugoSymbol
     */
    public String getHugoSymbol() {
        return hugoSymbol;
    }
    /**
     * @param hugoSymbol the hugoSymbol to set
     */
    public void setHugoSymbol(String hugoSymbol) {
        this.hugoSymbol = hugoSymbol;
    }
    /**
     * {@inheritDoc}
     */
    public int compareTo(CaBioDisplayableGene o) {
        return this.getSymbol().compareTo(o.getSymbol());
    }

}
