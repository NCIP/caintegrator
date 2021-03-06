/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.abstractlist;

import gov.nih.nci.caintegrator.domain.genomic.Gene;
import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action to search for gene list.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class SearchGeneListAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;
    private static final int MAX_NUMBER_GENES_TO_DISPLAY = 1000;

    // JSP Form Hidden Variables
    private String geneSymbolElementId;
    private boolean geneListSearchTopicPublished = false;
    private String geneListName = null;
    private List<Gene> genes;

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        setDefaultGeneList();
        return SUCCESS;
    }

    private void setDefaultGeneList() {
        if (!getStudySubscription().getAllGeneLists().isEmpty()) {
            geneListName = getStudySubscription().getAllGeneListNames().get(0);
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
        genes.addAll(getStudySubscription().getSelectedGeneList(getGeneListName()));
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

    /**
     *
     * @return genes as a comma separated string.
     */
    public String getGenesAsString() {
        List<String> geneSymbolString = new ArrayList<String>();
        for (Gene gene : genes) {
            geneSymbolString.add(gene.getSymbol());
        }
        return StringUtils.join(geneSymbolString, ",");
    }

    /**
     * @return the maxNumberGenesToDisplay
     */
    public static int getMaxNumberGenesToDisplay() {
        return MAX_NUMBER_GENES_TO_DISPLAY;
    }

    /**
     *
     * @return if genes found is more than the max number.
     */
    public boolean isGenesHigherThanMax() {
        return genes.size() >= MAX_NUMBER_GENES_TO_DISPLAY;
    }
}
