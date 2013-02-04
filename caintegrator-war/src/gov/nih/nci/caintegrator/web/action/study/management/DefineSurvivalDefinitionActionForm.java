/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;

import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueDefinition;
import gov.nih.nci.caintegrator.domain.annotation.SurvivalValueTypeEnum;

/**
 * Since s:select items have to link back to a String, these are placeholders for that on the form.
 */
public class DefineSurvivalDefinitionActionForm {

    private String survivalValueDefinitionId;
    private String survivalValueDefinitionName;
    private String survivalStartDateId;
    private String survivalDeathDateId;
    private String lastFollowupDateId;
    private String survivalValueType;
    private String survivalLengthId;
    private String survivalLengthUnits;
    private String survivalStatusId;
    private String valueForCensored;

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
     * @return the survivalValueDefinitionName
     */
    public String getSurvivalValueDefinitionName() {
        return survivalValueDefinitionName;
    }
    /**
     * @param survivalValueDefinitionName the survivalValueDefinitionName to set
     */
    public void setSurvivalValueDefinitionName(String survivalValueDefinitionName) {
        this.survivalValueDefinitionName = survivalValueDefinitionName;
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
     * @return the survivalValueType
     */
    public String getSurvivalValueType() {
        return survivalValueType;
    }
    /**
     * @param survivalValueType the survivalValueType to set
     */
    public void setSurvivalValueType(String survivalValueType) {
        this.survivalValueType = survivalValueType;
    }
    /**
     * @return the survivalLengthId
     */
    public String getSurvivalLengthId() {
        return survivalLengthId;
    }
    /**
     * @param survivalLengthId the survivalLengthId to set
     */
    public void setSurvivalLengthId(String survivalLengthId) {
        this.survivalLengthId = survivalLengthId;
    }
    /**
     * @return the survivalLengthUnits
     */
    public String getSurvivalLengthUnits() {
        return survivalLengthUnits;
    }
    /**
     * @param survivalLengthUnits the survivalLengthUnits to set
     */
    public void setSurvivalLengthUnits(String survivalLengthUnits) {
        this.survivalLengthUnits = survivalLengthUnits;
    }
    /**
     * @return the survivalStatusId
     */
    public String getSurvivalStatusId() {
        return survivalStatusId;
    }
    /**
     * @param survivalStatusId the survivalStatusId to set
     */
    public void setSurvivalStatusId(String survivalStatusId) {
        this.survivalStatusId = survivalStatusId;
    }
    /**
     * @return the valueForCensored
     */
    public String getValueForCensored() {
        return valueForCensored;
    }
    /**
     * @param valueForCensored the valueForCensored to set
     */
    public void setValueForCensored(String valueForCensored) {
        this.valueForCensored = valueForCensored;
    }
    /**
     * Clears all the variables to null.
     */
    public void clear() {
        setSurvivalValueType(SurvivalValueTypeEnum.DATE.getValue());
        survivalValueDefinitionId = null;
        survivalStartDateId = null;
        survivalDeathDateId = null;
        lastFollowupDateId = null;
        survivalLengthId = null;
        survivalLengthUnits = null;
        survivalStatusId = null;
        valueForCensored = null;
    }

    /**
     * Loads the string IDs to their proper values so that they can show up the current values in the JSP.
     * @param survivalValueDefinition - Values to load from.
     */
    public void load(SurvivalValueDefinition survivalValueDefinition) {
        if (survivalValueDefinition != null) {
            setSurvivalValueDefinitionId(String.valueOf(survivalValueDefinition.getId()));
            setSurvivalValueDefinitionName(survivalValueDefinition.getName());
            setSurvivalValueType(survivalValueDefinition.getSurvivalValueType().getValue());
            if (survivalValueDefinition.getSurvivalStartDate() != null) {
                setSurvivalStartDateId(String.valueOf(survivalValueDefinition.getSurvivalStartDate().getId()));
            }
            if (survivalValueDefinition.getDeathDate() != null) {
                setSurvivalDeathDateId(String.valueOf(survivalValueDefinition.getDeathDate().getId()));
            }
            if (survivalValueDefinition.getLastFollowupDate() != null) {
                setLastFollowupDateId(String.valueOf(survivalValueDefinition.getLastFollowupDate().getId()));
            }
            if (survivalValueDefinition.getSurvivalLength() != null) {
                setSurvivalLengthId(String.valueOf(survivalValueDefinition.getSurvivalLength().getId()));
            }
            if (survivalValueDefinition.getSurvivalStatus() != null) {
                setSurvivalStatusId(String.valueOf(survivalValueDefinition.getSurvivalStatus().getId()));
            }
            if (survivalValueDefinition.getSurvivalLengthUnits() != null) {
                setSurvivalLengthUnits(survivalValueDefinition.getSurvivalLengthUnits().getValue());
            }
            setValueForCensored(survivalValueDefinition.getValueForCensored());
        }
    }


}
