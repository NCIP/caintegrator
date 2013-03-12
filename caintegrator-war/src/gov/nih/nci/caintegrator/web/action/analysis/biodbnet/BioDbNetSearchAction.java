/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.biodbnet;

import gov.nih.nci.caintegrator.external.biodbnet.BioDbNetService;
import gov.nih.nci.caintegrator.external.biodbnet.enums.SearchType;
import gov.nih.nci.caintegrator.external.biodbnet.search.GeneResults;
import gov.nih.nci.caintegrator.external.biodbnet.search.SearchParameters;
import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;

/**
 * Actions for dealing with bioDbNet searches.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class BioDbNetSearchAction extends AbstractCaIntegrator2Action {
    private static final long serialVersionUID = 1L;
    private BioDbNetService bioDbNetService;
    private SearchParameters searchParameters;
    private Set<GeneResults> geneResults;

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        searchParameters = new SearchParameters();
        geneResults = Sets.newHashSet();
    }

    /**
     * Default input method.
     * @return the struts result
     */
    @Override
    public String input() {
        return SUCCESS;
    }

    /**
     * Performs the bioDbNet search.
     * @return the struts results
     */
    public String search() {
        try {
            getSearchParameters().setStudy(getCurrentStudy());
            setGeneResults(handleSearch(getSearchParameters().getSearchType()));
        } catch (Exception e) {
            LOG.error("An error has occurred.", e);
        }
        if (CollectionUtils.isEmpty(getGeneResults())) {
            addActionError(getText("bioDbNet.noResultsFound"));
            return INPUT;
        }
        return SUCCESS;
    }

    private Set<GeneResults> handleSearch(SearchType searchType) {
        Set<GeneResults> results = Sets.newHashSet();
        if (searchType == SearchType.GENE_ID) {
            results = bioDbNetService.retrieveGenesById(getSearchParameters());
        } else if (searchType == SearchType.GENE_ALIAS) {
            Set<String> geneIds = bioDbNetService.retrieveGeneIdsByAlias(getSearchParameters());
            results = bioDbNetService.retrieveGenesById(generateNewParams(geneIds));
        } else if (searchType == SearchType.GENE_SYMBOL) {
            Set<String> geneIds = bioDbNetService.retrieveGeneIds(getSearchParameters());
            results = bioDbNetService.retrieveGenesById(generateNewParams(geneIds));
        } else if (searchType == SearchType.PATHWAY) {
            results = bioDbNetService.retrieveGenesByPathway(getSearchParameters());
        }
        return results;
    }

    private SearchParameters generateNewParams(Set<String> input) {
        SearchParameters params = new SearchParameters();
        params.setFilterGenesOnStudy(getSearchParameters().isFilterGenesOnStudy());
        params.setTaxon(getSearchParameters().getTaxon());
        params.setInputValues(StringUtils.join(input, ','));
        return params;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        super.validate();
        if (StringUtils.isEmpty(getSearchParameters().getInputValues())) {
            addActionError(getText("struts.messages.error.must.enter.keywords"));
        }
    }

    /**
     * @return the searchParameters
     */
    public SearchParameters getSearchParameters() {
        return searchParameters;
    }

    /**
     * @param searchParameters the searchParameters to set
     */
    public void setSearchParameters(SearchParameters searchParameters) {
        this.searchParameters = searchParameters;
    }

    /**
     * @return the geneResults
     */
    public Set<GeneResults> getGeneResults() {
        return geneResults;
    }

    /**
     * @param geneResults the geneResults to set
     */
    public void setGeneResults(Set<GeneResults> geneResults) {
        this.geneResults = geneResults;
    }

    /**
     * Sets the bioDbNet service.
     * @param svc the service to set
     */
    public void setBioDbNetService(BioDbNetService svc) {
        this.bioDbNetService = svc;
    }
}