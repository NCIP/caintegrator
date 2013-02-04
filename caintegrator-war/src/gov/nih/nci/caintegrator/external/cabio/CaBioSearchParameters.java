/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.cabio;

import gov.nih.nci.cabio.domain.Pathway;
import gov.nih.nci.caintegrator.domain.translational.Study;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class CaBioSearchParameters {
    /**
     * To use all taxons.
     */
    public static final String ALL_TAXONS = "ALL";
    
    /**
     * Human taxon (default).
     */
    public static final String HUMAN_TAXON = "human";
    
    private String keywords;
    private String taxon = HUMAN_TAXON;
    private Study study;
    private boolean filterGenesOnStudy = true;
    private KeywordSearchPreferenceEnum searchPreference = KeywordSearchPreferenceEnum.ANY;
    private CaBioSearchTypeEnum searchType = CaBioSearchTypeEnum.GENE_KEYWORDS;
    private final List<Pathway> pathways = new ArrayList<Pathway>();
    
    /**
     * @return the keywords
     */
    public String getKeywords() {
        return keywords;
    }
    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
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
     * @return the study
     */
    public Study getStudy() {
        return study;
    }
    /**
     * @param study the study to set
     */
    public void setStudy(Study study) {
        this.study = study;
    }
    /**
     * @return the filterGenesOnStudy
     */
    public boolean isFilterGenesOnStudy() {
        return filterGenesOnStudy;
    }
    /**
     * @param filterGenesOnStudy the filterGenesOnStudy to set
     */
    public void setFilterGenesOnStudy(boolean filterGenesOnStudy) {
        this.filterGenesOnStudy = filterGenesOnStudy;
    }
    
    /**
     * @return the searchPreference
     */
    public KeywordSearchPreferenceEnum getSearchPreference() {
        return searchPreference;
    }
    
    /**
     * @param searchPreference the searchPreference to set
     */
    public void setSearchPreference(KeywordSearchPreferenceEnum searchPreference) {
        this.searchPreference = searchPreference;
    }
    
    /**
     * @return the searchPreference
     */
    public String getSearchPreferenceForDisplay() {
        return searchPreference.getValue();
    }
    
    /**
     * @param searchPreferenceForDisplay the searchPreference to set
     */
    public void setSearchPreferenceForDisplay(String searchPreferenceForDisplay) {
        this.searchPreference = KeywordSearchPreferenceEnum.getByValue(searchPreferenceForDisplay);
    }
    
    /**
     * @return the searchType
     */
    public CaBioSearchTypeEnum getSearchType() {
        return searchType;
    }
    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(CaBioSearchTypeEnum searchType) {
        this.searchType = searchType;
    }
    
    /**
     * @return the searchType
     */
    public String getSearchTypeForDisplay() {
        return searchType.getValue();
    }
    /**
     * @param searchTypeForDisplay the searchType to set
     */
    public void setSearchTypeForDisplay(String searchTypeForDisplay) {
        this.searchType = CaBioSearchTypeEnum.getByValue(searchTypeForDisplay);
    }
    
    /**
     * @return the pathways
     */
    public List<Pathway> getPathways() {
        return pathways;
    }

}
