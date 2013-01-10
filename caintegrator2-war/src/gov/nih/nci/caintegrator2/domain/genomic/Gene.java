/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.domain.genomic;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class Gene extends AbstractCaIntegrator2Object implements Comparable<Gene> {

    private static final long serialVersionUID = 1L;
    
    private String ensemblgeneID;
    private String entrezgeneID;
    private String fullName;
    private String genbankAccession;
    private String genbankAccessionVersion;
    private String symbol;
    private String unigeneclusterID;
    
    /**
     * @return the ensemblgeneID
     */
    public String getEnsemblgeneID() {
        return ensemblgeneID;
    }
    
    /**
     * @param ensemblgeneID the ensemblgeneID to set
     */
    public void setEnsemblgeneID(String ensemblgeneID) {
        this.ensemblgeneID = ensemblgeneID;
    }
    
    /**
     * @return the entrezgeneID
     */
    public String getEntrezgeneID() {
        return entrezgeneID;
    }
    
    /**
     * @param entrezgeneID the entrezgeneID to set
     */
    public void setEntrezgeneID(String entrezgeneID) {
        this.entrezgeneID = entrezgeneID;
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
     * @return the genbankAccession
     */
    public String getGenbankAccession() {
        return genbankAccession;
    }
    
    /**
     * @param genbankAccession the genbankAccession to set
     */
    public void setGenbankAccession(String genbankAccession) {
        this.genbankAccession = genbankAccession;
    }
    
    /**
     * @return the genbankAccessionVersion
     */
    public String getGenbankAccessionVersion() {
        return genbankAccessionVersion;
    }
    
    /**
     * @param genbankAccessionVersion the genbankAccessionVersion to set
     */
    public void setGenbankAccessionVersion(String genbankAccessionVersion) {
        this.genbankAccessionVersion = genbankAccessionVersion;
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
     * @return the unigeneclusterID
     */
    public String getUnigeneclusterID() {
        return unigeneclusterID;
    }
    
    /**
     * @param unigeneclusterID the unigeneclusterID to set
     */
    public void setUnigeneclusterID(String unigeneclusterID) {
        this.unigeneclusterID = unigeneclusterID;
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(Gene gene) {
        return symbol.compareTo(gene.getSymbol());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getSymbol();
    }

}
