/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.copynumber;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.common.Cai2Util;
import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.analysis.GisticAnalysis;
import gov.nih.nci.caintegrator2.domain.genomic.Gene;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to view / edit the gistic analysis job.
 */
@SuppressWarnings("PMD") // See createPlatform method
public class EditGisticAnalysisAction extends AbstractCaIntegrator2Action {

    private static final long serialVersionUID = 1L;
    private AnalysisService analysisService;
    private final List<Gene> amplifiedGenes = new ArrayList<Gene>();
    private final List<Gene> deletedGenes = new ArrayList<Gene>();
    private GisticAnalysis gisticAnalysis;
    private String selectedAction;
    
    static final String EDIT_ACTION = "edit";
    static final String DELETE_ACTION = "delete";
    static final String CANCEL_ACTION = "cancel";
    static final String SAVE_ACTION = "save";
    static final String HOME_PAGE = "homePage";

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepare() {
        super.prepare();
        if (gisticAnalysis.getId() != null) {
            gisticAnalysis = getWorkspaceService().getRefreshedEntity(gisticAnalysis);
            amplifiedGenes.clear();
            deletedGenes.clear();
            Cai2Util.retrieveGisticAmplifiedDeletedGenes(gisticAnalysis, amplifiedGenes, deletedGenes);
        }
    }
    
    /**
     * @return the Struts result.
     */
    public String execute() {
        if (EDIT_ACTION.equals(selectedAction)) {
            return edit();
        } else if (DELETE_ACTION.equals(selectedAction)) {
            clearAnalysisCache();
            return delete();
        } else if (SAVE_ACTION.equals(selectedAction)) {
            clearAnalysisCache();
            return save();
        } else if (CANCEL_ACTION.equals(selectedAction)) {
            return HOME_PAGE;
        }
        return SUCCESS;
    }
    
    
    /**
     * Go to the edit page.
     * @return the Struts result.
     */
    public String edit() {
        return SUCCESS;
    }

    /**
     * Rename the gistic analysis.
     * @return the Struts result.
     */
    public String save() {
        getWorkspaceService().saveUserWorkspace(getWorkspace());
        addActionMessage(getText("struts.messages.gistic.updated.successfully"));
        return SUCCESS;
    }

    /**
     * Delete the gistic analysis.
     * @return the Struts result.
     */
    public String delete() {
        analysisService.deleteGisticAnalysis(gisticAnalysis);
        return HOME_PAGE;
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
     * @return the gisticAnalysis
     */
    public GisticAnalysis getGisticAnalysis() {
        return gisticAnalysis;
    }

    /**
     * @param gisticAnalysis the gisticAnalysis to set
     */
    public void setGisticAnalysis(GisticAnalysis gisticAnalysis) {
        this.gisticAnalysis = gisticAnalysis;
    }

    /**
     * @return the amplifiedGenes
     */
    public List<Gene> getAmplifiedGenes() {
        return amplifiedGenes;
    }

    /**
     * @return the deletedGenes
     */
    public List<Gene> getDeletedGenes() {
        return deletedGenes;
    }

    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }
    
    /**
     * 
     * @return creationDate.
     */
    public String getCreationDate() {
        return DateUtil.getDisplayableTimeStamp(gisticAnalysis.getCreationDate());
    }
}
