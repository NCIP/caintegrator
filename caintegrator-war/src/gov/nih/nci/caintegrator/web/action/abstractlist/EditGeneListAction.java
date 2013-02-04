/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.abstractlist;

import gov.nih.nci.caintegrator.domain.application.AbstractList;
import gov.nih.nci.caintegrator.domain.application.GeneList;
import gov.nih.nci.caintegrator.domain.genomic.Gene;

/**
 * Provides functionality to list and add array designs.
 */
public class EditGeneListAction extends EditAbstractListAction {

    private static final long serialVersionUID = 1L;

    @Override
    AbstractList getAbstractList(String name, boolean globalList) {
        return (globalList)
            ? getStudySubscription().getStudy().getStudyConfiguration().getGeneList(name)
            : getStudySubscription().getGeneList(name);
    }

    @Override
    void setOpenList(String name) {
        setOpenGeneListName(name);
        setOpenGlobalGeneListName(null);
    }

    @Override
    void setOpenGlobalList(String name) {
        setOpenGlobalGeneListName(name);
        setOpenGeneListName(null);
    }

    /**
     * @return the gene symbol listing
     */
    public String getGeneSymbolListing() {
        StringBuffer listing = new StringBuffer();
        for (Gene gene : ((GeneList) getAbstractList()).getGeneCollection()) {
            if (listing.length() > 0) {
                listing.append('\n');
            }
            listing.append(gene.getSymbol());
        }
        return listing.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getCreationSuccessfulMessage() {
        return getText("struts.messages.gene.list.successfully.created");
    }

}
