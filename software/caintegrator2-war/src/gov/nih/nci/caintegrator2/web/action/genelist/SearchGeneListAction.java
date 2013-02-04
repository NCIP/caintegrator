/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.genelist;

import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Action to search for gene list.
 */
public class SearchGeneListAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;

    // JSP Form Hidden Variables
    private String geneSymbolElementId;
    private boolean geneListSearchTopicPublished = false;
    private String geneListName = null;
    private List<Gene> genes;

    /**
     * {@inheritDoc}
     */
    public String execute() {
        setDefaultGeneList();
        return SUCCESS;
    }
    
    private void setDefaultGeneList() {
        if (!getStudySubscription().getGeneLists().isEmpty()) {
            geneListName = getStudySubscription().getGeneLists().get(0).getName();
            retrieveGenes();
        }
    }
    
    /**
     * Searches gene list for genes.
     * @return struts result.
     */
    public String searchForGenes() {
        retrieveGenes();
        return SUCCESS;
    }
    
    private void retrieveGenes() {
        genes = new ArrayList<Gene>();
        GeneList list = getStudySubscription().getGeneList(getGeneListName());
        if (list != null) {
            genes.addAll(list.getGeneCollection());
        }
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
        return geneListSearchTopicPublished;
    }

    /**
     * @param caBioGeneSearchTopicPublished the caBioGeneSearchTopicPublished to set
     */
    public void setCaBioGeneSearchTopicPublished(boolean caBioGeneSearchTopicPublished) {
        this.geneListSearchTopicPublished = caBioGeneSearchTopicPublished;
    }
    
    /**
     * @return the geneListName
     */
    public String getGeneListName() {
        return geneListName;
    }

    /**
     * @param geneListName the geneListName to set
     */
    public void setGeneListName(String geneListName) {
        this.geneListName = geneListName;
    }

    /**
     * @return the geneListSearchTopicPublished
     */
    public boolean isGeneListSearchTopicPublished() {
        return geneListSearchTopicPublished;
    }

    /**
     * @param geneListSearchTopicPublished the geneListSearchTopicPublished to set
     */
    public void setGeneListSearchTopicPublished(boolean geneListSearchTopicPublished) {
        this.geneListSearchTopicPublished = geneListSearchTopicPublished;
    }

    /**
     * @return the geneSymbols
     */
    public List<Gene> getGenes() {
        return genes;
    }
}
