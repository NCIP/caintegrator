/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.domain.annotation.SurvivalValueDefinition;

/**
 * Since s:select items have to link back to a String, these are placeholders for that on the form.
 */
public class DefineSurvivalDefinitionActionForm {
    
    private String survivalValueDefinitionId;
    private String survivalStartDateId;
    private String survivalDeathDateId;
    private String lastFollowupDateId;
    
    
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
     * @return the survivalStartDateId
     */
    public String getSurvivalStartDateId() {
        return survivalStartDateId;
    }
    /**
     * @param survivalStartDateId the survivalStartDateId to set
     */
    public void setSurvivalStartDateId(String survivalStartDateId) {
        this.survivalStartDateId = survivalStartDateId;
    }
    /**
     * @return the survivalDeathDateId
     */
    public String getSurvivalDeathDateId() {
        return survivalDeathDateId;
    }
    /**
     * @param survivalDeathDateId the survivalDeathDateId to set
     */
    public void setSurvivalDeathDateId(String survivalDeathDateId) {
        this.survivalDeathDateId = survivalDeathDateId;
    }
    /**
     * @return the lastFollowupDateId
     */
    public String getLastFollowupDateId() {
        return lastFollowupDateId;
    }
    /**
     * @param lastFollowupDateId the lastFollowupDateId to set
     */
    public void setLastFollowupDateId(String lastFollowupDateId) {
        this.lastFollowupDateId = lastFollowupDateId;
    }
    
    /**
     * Clears all the variables to null.
     */
    public void clear() {
        survivalValueDefinitionId = null;
        survivalStartDateId = null;
        survivalDeathDateId = null;
        lastFollowupDateId = null;
    }
    
    /**
     * Loads the string IDs to their proper values so that they can show up the current values in the JSP.
     * @param survivalValueDefinition - Values to load from.
     */
    public void load(SurvivalValueDefinition survivalValueDefinition) {
        if (survivalValueDefinition != null) {
            setSurvivalValueDefinitionId(String.valueOf(survivalValueDefinition.getId()));
            if (survivalValueDefinition.getSurvivalStartDate() != null) {
                setSurvivalStartDateId(String.valueOf(survivalValueDefinition.getSurvivalStartDate().getId()));
            }
            if (survivalValueDefinition.getDeathDate() != null) {
                setSurvivalDeathDateId(String.valueOf(survivalValueDefinition.getDeathDate().getId()));
            }
            if (survivalValueDefinition.getLastFollowupDate() != null) {
                setLastFollowupDateId(String.valueOf(survivalValueDefinition.getLastFollowupDate().getId()));
            }
        }
    }
    

}
