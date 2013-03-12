/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.biodbnet.search;

import gov.nih.nci.caintegrator.domain.translational.Study;
import gov.nih.nci.caintegrator.external.biodbnet.enums.SearchType;
import gov.nih.nci.caintegrator.external.biodbnet.enums.Taxon;

/**
 * Wrapper housing the necessary information to perform a biodbnet search.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class SearchParameters {

    private Taxon taxon = Taxon.HUMAN;
    private SearchType searchType = SearchType.GENE_SYMBOL;
    private String inputValues;
    private boolean filterGenesOnStudy = false;
    private Study study;

    /**
     * @return the taxon
     */
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     * @return the searchType
     */
    public SearchType getSearchType() {
        return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    /**
     * @return the input values
     */
    public String getInputValues() {
        return inputValues;
    }

    /**
     * @param values the input values to set
     */
    public void setInputValues(String values) {
        this.inputValues = values;
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
}
