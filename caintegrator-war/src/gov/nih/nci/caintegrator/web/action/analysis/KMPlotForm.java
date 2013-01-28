/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder for the different types of KM Plot Forms.
 */
public class KMPlotForm {

    private KMPlotAnnotationBasedActionForm annotationBasedForm = new KMPlotAnnotationBasedActionForm();
    private KMPlotGeneExpressionBasedActionForm geneExpressionBasedForm = new KMPlotGeneExpressionBasedActionForm();
    private KMPlotQueryBasedActionForm queryBasedForm = new KMPlotQueryBasedActionForm();
    
    private Map<String, SurvivalValueDefinition> survivalValueDefinitions = 
        new HashMap<String, SurvivalValueDefinition>();
    private String survivalValueDefinitionId;

    /**
     * Clears all forms and the KM Plots out of the session.
     */
    public void clear() {
        SessionHelper.clearKmPlots();
        survivalValueDefinitions.clear();
        survivalValueDefinitionId = null;
        annotationBasedForm.clear();
        geneExpressionBasedForm.clear();
        queryBasedForm.clear();
    }
    
    /**
     * @return the annotationBasedForm
     */
    public KMPlotAnnotationBasedActionForm getAnnotationBasedForm() {
        return annotationBasedForm;
    }
    /**
     * @param annotationBasedForm the annotationBasedForm to set
     */
    public void setAnnotationBasedForm(KMPlotAnnotationBasedActionForm annotationBasedForm) {
        this.annotationBasedForm = annotationBasedForm;
    }
    /**
     * @return the survivalValueDefinitions
     */
    public Map<String, SurvivalValueDefinition> getSurvivalValueDefinitions() {
        return survivalValueDefinitions;
    }
    /**
     * @param survivalValueDefinitions the survivalValueDefinitions to set
     */
    public void setSurvivalValueDefinitions(Map<String, SurvivalValueDefinition> survivalValueDefinitions) {
        this.survivalValueDefinitions = survivalValueDefinitions;
    }
    /**
     * @return the survivalValueDefinitionId
     */
    public String getSurvivalValueDefinitionId() {
        return survivalValueDefinitionId;
    }
    /**
     * @param survivalValueDefinitionId the survivalValueDefinitionId to set
     */
    public void setSurvivalValueDefinitionId(String survivalValueDefinitionId) {
        this.survivalValueDefinitionId = survivalValueDefinitionId;
    }
    /**
     * @return the geneExpressionBasedForm
     */
    public KMPlotGeneExpressionBasedActionForm getGeneExpressionBasedForm() {
        return geneExpressionBasedForm;
    }
    /**
     * @param geneExpressionBasedForm the geneExpressionBasedForm to set
     */
    public void setGeneExpressionBasedForm(KMPlotGeneExpressionBasedActionForm geneExpressionBasedForm) {
        this.geneExpressionBasedForm = geneExpressionBasedForm;
    }

    /**
     * @return the queryBasedForm
     */
    public KMPlotQueryBasedActionForm getQueryBasedForm() {
        return queryBasedForm;
    }

    /**
     * @param queryBasedForm the queryBasedForm to set
     */
    public void setQueryBasedForm(KMPlotQueryBasedActionForm queryBasedForm) {
        this.queryBasedForm = queryBasedForm;
    }
    
    
}
