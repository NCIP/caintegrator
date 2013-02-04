/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


/**
 * Action used to modify/create/delete SurvivalValueDefinitions for a Study.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // See retrieveFormValues()
public class DefineSurvivalDefinitionAction extends AbstractStudyAction {
    
    private static final long serialVersionUID = 1L;
    private SurvivalValueDefinition survivalValueDefinition = new SurvivalValueDefinition();
    private AnnotationDefinition survivalStartDate = new AnnotationDefinition();
    private AnnotationDefinition survivalDeathDate = new AnnotationDefinition();
    private AnnotationDefinition lastFollowupDate = new AnnotationDefinition();
    private Map<String, AnnotationDefinition> dateAnnotationDefinitions = 
                                                new HashMap<String, AnnotationDefinition>();
    private Map<String, SurvivalValueDefinition> survivalValueDefinitions = 
                                                new HashMap<String, SurvivalValueDefinition>();
    private DefineSurvivalDefinitionActionForm survivalDefinitionFormValues = new DefineSurvivalDefinitionActionForm();
    private String actionType = "";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        clearErrorsAndMessages();
        if ("modify".equals(actionType) && survivalValueDefinition.getId() == null) {
            addActionError("Must select a Survival Value Definition before edit / delete.");
        }
        actionType = "";
    }
    
    /**
     * Refreshes the current clinical source configuration.
     */
    @Override
    public void prepare() {
        super.prepare();
        populateSurvivalValueDefinitions();
        populateDateAnnotationDefinitions();
        retrieveFormValues();
        refreshObjectInstances();
    }

    private void populateSurvivalValueDefinitions() {
        if (getStudyConfiguration() != null 
            && getStudyConfiguration().getStudy().getSurvivalValueDefinitionCollection() != null
            && survivalValueDefinitions.size() 
                != getStudyConfiguration().getStudy().getSurvivalValueDefinitionCollection().size()) {
            survivalValueDefinitions = new HashMap<String, SurvivalValueDefinition>();
            for (SurvivalValueDefinition def 
                    : getStudyConfiguration().getStudy().getSurvivalValueDefinitionCollection()) {
                survivalValueDefinitions.put(def.getId().toString(), def);
            }
        }
    }
    
    private void populateDateAnnotationDefinitions() {
        if (dateAnnotationDefinitions.isEmpty()) {
            dateAnnotationDefinitions = new HashMap<String, AnnotationDefinition>();
            for (AnnotationDefinition definition 
                    : getStudyConfiguration().getStudy().getSubjectAnnotationCollection()) {
                if (AnnotationTypeEnum.DATE.equals(definition.getDataType())) {
                    dateAnnotationDefinitions.put(definition.getId().toString(), definition);
                }
            }
        }
    }
    
    private void refreshObjectInstances() {
        if (survivalValueDefinition.getId() != null) {
            survivalValueDefinition = getStudyManagementService().getRefreshedStudyEntity(survivalValueDefinition);
        }
        
        if (survivalStartDate.getId() != null) {
            survivalStartDate = getStudyManagementService().getRefreshedStudyEntity(survivalStartDate);
        }
        
        if (survivalDeathDate.getId() != null) {
            survivalDeathDate = getStudyManagementService().getRefreshedStudyEntity(survivalDeathDate);
        }
        
        if (lastFollowupDate.getId() != null) {
            lastFollowupDate = getStudyManagementService().getRefreshedStudyEntity(lastFollowupDate);
        }
    }

    @SuppressWarnings("PMD.CyclomaticComplexity") // Null and empty checks
    private void retrieveFormValues() {
        if (survivalDefinitionFormValues.getSurvivalValueDefinitionId() != null 
             && !StringUtils.isEmpty(survivalDefinitionFormValues.getSurvivalValueDefinitionId())) {
            survivalValueDefinition.setId(Long.valueOf(survivalDefinitionFormValues.getSurvivalValueDefinitionId()));
        }
        
        if (survivalDefinitionFormValues.getSurvivalStartDateId() != null
             && !StringUtils.isEmpty(survivalDefinitionFormValues.getSurvivalStartDateId())) {
            survivalStartDate.setId(Long.valueOf(survivalDefinitionFormValues.getSurvivalStartDateId()));
        }
        
        if (survivalDefinitionFormValues.getSurvivalDeathDateId() != null
             && !StringUtils.isEmpty(survivalDefinitionFormValues.getSurvivalDeathDateId())) {
            survivalDeathDate.setId(Long.valueOf(survivalDefinitionFormValues.getSurvivalDeathDateId()));
        }
        
        if (survivalDefinitionFormValues.getLastFollowupDateId() != null
             && !StringUtils.isEmpty(survivalDefinitionFormValues.getLastFollowupDateId())) {
            lastFollowupDate.setId(Long.valueOf(survivalDefinitionFormValues.getLastFollowupDateId()));
        }
    }


    private void clear() {
        survivalDefinitionFormValues.clear();
        this.survivalValueDefinition = new SurvivalValueDefinition();
    }
    
    /**
     * Cancel action.
     * @return struts return string.
     */
    public String cancel() {
        return SUCCESS;
    }
    
    /**
     * Edits the survival value definitions.
     * @return Struts result.
     */
    public String editSurvivalValueDefinitions() {
        clear();
        return SUCCESS;
    }
    
    /**
     * Edits the survival value definition that is chosen.
     * @return the Struts result.
     */
    public String editSurvivalValueDefinition() {
        survivalDefinitionFormValues.load(survivalValueDefinition);
        return SUCCESS;
    }
    
    /**
     * Creates a new survival value definition.
     * 
     * @return the Struts result.
     */
    public String newSurvivalValueDefinition() {
        setLastModifiedByCurrentUser();
        survivalValueDefinition = getStudyManagementService().
                                    createNewSurvivalValueDefinition(getStudyConfiguration().getStudy());
        survivalDefinitionFormValues.clear();
        populateSurvivalValueDefinitions();
        return SUCCESS;
    }
    
    /**
     * Deletes a survival value definition.
     * 
     * @return the Struts result.
     */
    public String deleteSurvivalValueDefinition() {
        setLastModifiedByCurrentUser();
        getStudyManagementService().
            removeSurvivalValueDefinition(getStudyConfiguration().getStudy(), getSurvivalValueDefinition());
        this.clear();
        populateSurvivalValueDefinitions();
        return SUCCESS;
    }
    
    /**
     * Saves a survival value definition.
     * 
     * @return the Struts result.
     */
    public String saveSurvivalValueDefinition() {
        if (survivalStartDate.getId() != null) {
            survivalValueDefinition.setSurvivalStartDate(survivalStartDate);
        }
        if (survivalDeathDate.getId() != null) {
            survivalValueDefinition.setDeathDate(survivalDeathDate);
        }
        if (lastFollowupDate.getId() != null) {
            survivalValueDefinition.setLastFollowupDate(lastFollowupDate);
        }
        setLastModifiedByCurrentUser();
        getStudyManagementService().save(getStudyConfiguration());
        survivalDefinitionFormValues.clear();
        return SUCCESS;
    }

    /**
     * @return the survivalValueDefinition
     */
    public SurvivalValueDefinition getSurvivalValueDefinition() {
        return survivalValueDefinition;
    }

    /**
     * @param survivalValueDefinition the survivalValueDefinition to set
     */
    public void setSurvivalValueDefinition(SurvivalValueDefinition survivalValueDefinition) {
        this.survivalValueDefinition = survivalValueDefinition;
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
     * @return the survivalDefinitionFormValues
     */
    public DefineSurvivalDefinitionActionForm getSurvivalDefinitionFormValues() {
        return survivalDefinitionFormValues;
    }


    /**
     * @param survivalDefinitionFormValues the survivalDefinitionFormValues to set
     */
    public void setSurvivalDefinitionFormValues(DefineSurvivalDefinitionActionForm survivalDefinitionFormValues) {
        this.survivalDefinitionFormValues = survivalDefinitionFormValues;
    }


    /**
     * @return the dateAnnotationDefinitions
     */
    public Map<String, AnnotationDefinition> getDateAnnotationDefinitions() {
        return dateAnnotationDefinitions;
    }


    /**
     * @param dateAnnotationDefinitions the dateAnnotationDefinitions to set
     */
    public void setDateAnnotationDefinitions(Map<String, AnnotationDefinition> dateAnnotationDefinitions) {
        this.dateAnnotationDefinitions = dateAnnotationDefinitions;
    }

    /**
     * @return the actionType
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * @param actionType the actionType to set
     */
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

}
