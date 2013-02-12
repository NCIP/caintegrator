/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.cabio;

import gov.nih.nci.cabio.domain.Pathway;
import gov.nih.nci.cabio.domain.Taxon;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.cabio.CaBioDisplayableGene;
import gov.nih.nci.caintegrator.external.cabio.CaBioDisplayablePathway;
import gov.nih.nci.caintegrator.external.cabio.CaBioFacade;
import gov.nih.nci.caintegrator.external.cabio.CaBioSearchParameters;
import gov.nih.nci.caintegrator.external.cabio.CaBioSearchTypeEnum;
import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * CaBio search action.
 */
public class CaBioSearchAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;

    private CaBioFacade caBioFacade;
    private CaBioSearchParameters searchParams = new CaBioSearchParameters();
    private List<CaBioDisplayableGene> caBioGenes = new ArrayList<CaBioDisplayableGene>();
    private final List<String> taxonList = new ArrayList<String>();

    // JSP Form Hidden Variables
    private String geneSymbolElementId;
    private boolean caBioGeneSearchTopicPublished = false;
    private boolean runSearchSelected = false;
    private boolean runCaBioGeneSearchFromPathways = false;
    // This variable is a space delimited string which contains the indeces of the checked pathway checkboxes.
    private String checkedPathwayBoxes = "";
    private boolean ableToConnect = false;

    /**
     * {@inheritDoc}
     */
    @Override
    public String input() {
        try {
            reset();
            ableToConnect = true;
        } catch (Exception e) {
            addActionError(getText("struts.messages.error.cabio.unable.to.connect", getArgs(e.getMessage())));
            ableToConnect = false;
        }
        return SUCCESS;
    }

    private void reset() throws ConnectionException {
        getCaBioPathways().clear();
        taxonList.clear();
        taxonList.add(CaBioSearchParameters.ALL_TAXONS);
        taxonList.addAll(caBioFacade.retrieveAllTaxons());
        searchParams = new CaBioSearchParameters();
    }

    /**
     * Searches caBio for genes.
     * @return struts result.
     */
    public String searchCaBio() {
        if (runSearchSelected) {
            runSearchSelected = false;
            if (StringUtils.isBlank(searchParams.getKeywords())) {
                addActionError(getText("struts.messages.error.must.enter.keywords"));
                return INPUT;
            }
            if (StringUtils.length(searchParams.getKeywords()) < 3) {
                addActionError(getText("struts.messages.error.keywords.size.under.three.chars"));
                return INPUT;
            }
            return retrieveDisplayableValues();
        } else if (runCaBioGeneSearchFromPathways) {
            return runGeneSearchFromPathways();
        }
        return SUCCESS;
    }

    private String retrieveDisplayableValues() {
        if (CaBioSearchTypeEnum.PATHWAYS.equals(searchParams.getSearchType())) {
            return retrievePathwaysFromCaBio();
        }
        return retrieveGenesFromCaBio(false);
    }

    private String runGeneSearchFromPathways() {
        runCaBioGeneSearchFromPathways = false;
        searchParams.getPathways().clear();
        retrievePathwaysFromGuiCheckboxes();
        if (searchParams.getPathways().isEmpty()) {
            addActionError(getText("struts.messages.error.cabio.must.select.pathway"));
            return INPUT;
        }
        return retrieveGenesFromCaBio(true);
    }

    /**
     * Because the GUI doesn't set the checkboxes from the results page, it is artificially set in the
     * "checkedPathwayBoxes" string variable which is a space delimited string where the values are the
     * indexes of the checked boxes.  From that we can retrieve the actual pathways the user checked.
     */
    private void retrievePathwaysFromGuiCheckboxes() {
        Set<String> checkedPathwayIndeces = new HashSet<String>();
        checkedPathwayIndeces.addAll(Arrays.asList(checkedPathwayBoxes.split(" ")));
        Integer index = 0;
        for (CaBioDisplayablePathway displayablePathway : getCaBioPathways()) {
            if (checkedPathwayIndeces.contains(index.toString())) {
                displayablePathway.setChecked(true);
                Pathway pathway = new Pathway();
                pathway.setId(Long.valueOf(displayablePathway.getId()));
                if (!CaBioSearchParameters.ALL_TAXONS.equals(searchParams.getTaxon())) {
                    Taxon taxon = new Taxon();
                    taxon.setCommonName(searchParams.getTaxon());
                    pathway.setTaxon(taxon);
                }
                searchParams.getPathways().add(pathway);
            } else {
                displayablePathway.setChecked(false);
            }
            index++;
        }
    }

    private String retrieveGenesFromCaBio(boolean isSearchFromPathwaysSelected) {
        try {
            searchParams.setStudy(getCurrentStudy());
            getCaBioPathways().clear();
            if (isSearchFromPathwaysSelected) {
                caBioGenes = caBioFacade.retrieveGenesFromPathways(searchParams);
            } else {
                caBioGenes = caBioFacade.retrieveGenes(searchParams);
            }
            if (caBioGenes.isEmpty()) {
                addActionError(getText("struts.messages.error.no.results.returned"));
                return INPUT;
            }
        } catch (ConnectionException e) {
            addActionError(getText("struts.messages.error.unable.to.connect"));
            return ERROR;
        }
        return SUCCESS;
    }

    private String retrievePathwaysFromCaBio() {
        try {
            searchParams.setStudy(getCurrentStudy());
            caBioGenes.clear();
            getCaBioPathways().clear();
            getCaBioPathways().addAll(caBioFacade.retrievePathways(searchParams));
            if (getCaBioPathways().isEmpty()) {
                addActionError(getText("struts.messages.error.no.results.returned"));
                return INPUT;
            }
        } catch (ConnectionException e) {
            addActionError(getText("struts.messages.error.unable.to.connect"));
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * @return the caBioFacade
     */
    public CaBioFacade getCaBioFacade() {
        return caBioFacade;
    }

    /**
     * @param caBioFacade the caBioFacade to set
     */
    public void setCaBioFacade(CaBioFacade caBioFacade) {
        this.caBioFacade = caBioFacade;
    }

    /**
     * @return the caBioGenes
     */
    public List<CaBioDisplayableGene> getCaBioGenes() {
        return caBioGenes;
    }

    /**
     * @param caBioGenes the caBioGenes to set
     */
    public void setCaBioGenes(List<CaBioDisplayableGene> caBioGenes) {
        this.caBioGenes = caBioGenes;
    }

    /**
     * @return the caBioPathways
     */
    public List<CaBioDisplayablePathway> getCaBioPathways() {
        return getDisplayableWorkspace().getCaBioPathways();
    }

    /**
     * @return the runSearchSelected
     */
    public boolean isRunSearchSelected() {
        return runSearchSelected;
    }

    /**
     * @param runSearchSelected the runSearchSelected to set
     */
    public void setRunSearchSelected(boolean runSearchSelected) {
        this.runSearchSelected = runSearchSelected;
    }

    /**
     * @return the runCaBioGeneSearchFromPathways
     */
    public boolean isRunCaBioGeneSearchFromPathways() {
        return runCaBioGeneSearchFromPathways;
    }

    /**
     * @param runCaBioGeneSearchFromPathways the runCaBioGeneSearchFromPathways to set
     */
    public void setRunCaBioGeneSearchFromPathways(boolean runCaBioGeneSearchFromPathways) {
        this.runCaBioGeneSearchFromPathways = runCaBioGeneSearchFromPathways;
    }

    /**
     * @return the geneSymbolElementId
     */
    public String getGeneSymbolElementId() {
        return geneSymbolElementId;
    }

    /**
     * @param geneSymbolElementId the geneSymbolElementId to set
     */
    public void setGeneSymbolElementId(String geneSymbolElementId) {
        this.geneSymbolElementId = geneSymbolElementId;
    }

    /**
     * @return the caBioGeneSearchTopicPublished
     */
    public boolean isCaBioGeneSearchTopicPublished() {
        return caBioGeneSearchTopicPublished;
    }

    /**
     * @param caBioGeneSearchTopicPublished the caBioGeneSearchTopicPublished to set
     */
    public void setCaBioGeneSearchTopicPublished(boolean caBioGeneSearchTopicPublished) {
        this.caBioGeneSearchTopicPublished = caBioGeneSearchTopicPublished;
    }

    /**
     * @return the searchParams
     */
    public CaBioSearchParameters getSearchParams() {
        return searchParams;
    }

    /**
     * @param searchParams the searchParams to set
     */
    public void setSearchParams(CaBioSearchParameters searchParams) {
        this.searchParams = searchParams;
    }

    /**
     * @return the taxonList
     */
    public List<String> getTaxonList() {
        return taxonList;
    }

    /**
     * @return the checkedPathwayBoxes
     */
    public String getCheckedPathwayBoxes() {
        return checkedPathwayBoxes;
    }

    /**
     * @param checkedPathwayBoxes the checkedPathwayBoxes to set
     */
    public void setCheckedPathwayBoxes(String checkedPathwayBoxes) {
        this.checkedPathwayBoxes = checkedPathwayBoxes;
    }

    /**
     * @return the ableToConnect
     */
    public boolean isAbleToConnect() {
        return ableToConnect;
    }

}