/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.genelist;

import gov.nih.nci.caintegrator2.domain.application.GeneList;
import gov.nih.nci.caintegrator2.domain.application.StudySubscription;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

import org.apache.commons.lang.StringUtils;

/**
 * Provides functionality to list and add array designs.
 */
@SuppressWarnings("PMD") // See createPlatform method
public class EditGeneListAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;
    private String geneListOldName;
    private String geneListName;
    private String selectedAction;
    private boolean editOn = true;

    private static final String EDIT_ACTION = "editGeneList";
    private static final String DELETE_ACTION = "deleteGeneList";
    private static final String RENAME_ACTION = "renameGeneList";
    private static final String GENE_LIST_NAME = "geneListName";
    private static final String HOME_PAGE = "homePage";
    private static final String EDIT_PAGE = "editPage";
    
    /**
     * @return the Struts result.
     */
    public String execute() {
        if (EDIT_ACTION.equals(selectedAction)) {
            return editGeneList();
        } else if (DELETE_ACTION.equals(selectedAction)) {
            return deleteGeneList();
        } else if (RENAME_ACTION.equals(selectedAction)) {
            return renameGeneList();
        }
        return SUCCESS;
    }
    
    /**
     * Go to the edit page.
     * @return the Struts result.
     */
    public String editGeneList() {
        setGeneListOldName(getGeneListName());
        return SUCCESS;
    }
    
    /**
     * Delete the gene list.
     * @return the Struts result.
     */
    public String renameGeneList() {
        GeneList geneList = getStudySubscription().getGeneList(geneListOldName);
        if (geneList != null) {
            geneList.setName(geneListName);
            getWorkspaceService().saveUserWorkspace(getWorkspace());
            geneListOldName = geneListName;
            return EDIT_PAGE;
        } else {
            geneListNoLongerAvailable();
            return SUCCESS;
        }
    }
    
    /**
     * Delete the gene list.
     * @return the Struts result.
     */
    public String deleteGeneList() {
        GeneList geneList = getStudySubscription().getGeneList(getGeneListName());
        if (geneList != null) {
            getStudySubscription().getListCollection().remove(geneList);
            getWorkspaceService().saveUserWorkspace(getWorkspace());
            return HOME_PAGE;
        } else {
            geneListNoLongerAvailable();
            return SUCCESS;
        }
    }
    
    private void geneListNoLongerAvailable() {
        addActionError("The gene list '" + geneListOldName + "' is no longer available.");
        editOn = false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        if (EDIT_ACTION.equalsIgnoreCase(selectedAction)) {
            prepareValueStack();
            validateGeneList();
        } else if (RENAME_ACTION.equalsIgnoreCase(selectedAction)) {
            prepareValueStack();
            validateGeneListName();
        }
    }
    
    private void validateGeneList() {
        if (getStudySubscription().getGeneList(getGeneListName()) == null) {
            addActionError("The requested gene list '" + getGeneListName() + "' doesn't exist.");
            editOn = false;
        }
    }
    
    private void validateGeneListName() {
        if (StringUtils.isEmpty(getGeneListName())) {
            addFieldError(GENE_LIST_NAME, "Gene List name is required");
        } else if (getStudySubscription().getGeneList(getGeneListName()) != null) {
            addFieldError(GENE_LIST_NAME, "Gene List name is duplicate: " + getGeneListName());
            setGeneListName(geneListOldName);
        }
    }
    
    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
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
     * @return the study subscription
     */
    public StudySubscription getSubscription() {
        return getStudySubscription();
    }
    
    /**
     * @return the gene symbol listing
     */
    public String getGeneSymbolListing() {
        StringBuffer listing = new StringBuffer();
        for (Gene gene : getStudySubscription().getGeneList(getGeneListName()).getGeneCollection()) {
            listing.append(gene.getSymbol());
            listing.append('\n');
        }
        return listing.toString();
    }

    /**
     * @return the geneListOldName
     */
    public String getGeneListOldName() {
        return geneListOldName;
    }

    /**
     * @param geneListOldName the geneListOldName to set
     */
    public void setGeneListOldName(String geneListOldName) {
        this.geneListOldName = geneListOldName;
    }

    /**
     * @return the editOn
     */
    public boolean isEditOn() {
        return editOn;
    }
}
