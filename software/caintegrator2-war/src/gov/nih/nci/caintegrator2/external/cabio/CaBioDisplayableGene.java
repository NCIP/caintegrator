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
    
    private String id;
    private String symbol;
    private String fullName;
    private String taxonCommonName;
    private String hugoSymbol;
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
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
