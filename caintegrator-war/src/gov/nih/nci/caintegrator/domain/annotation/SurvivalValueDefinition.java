/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.annotation;

import gov.nih.nci.caintegrator.domain.AbstractCaIntegrator2Object;

/**
 * Used to be able to calculate survival values of subjects for KM Plots based on defined annotations.
 */
public class SurvivalValueDefinition extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private SurvivalValueTypeEnum survivalValueType = SurvivalValueTypeEnum.DATE;
    private SurvivalLengthUnitsEnum survivalLengthUnits = SurvivalLengthUnitsEnum.MONTHS;
    
    //////////
    // For "DATE" type.
    //////////
    private AnnotationDefinition survivalStartDate = new AnnotationDefinition();
    private AnnotationDefinition lastFollowupDate = new AnnotationDefinition();
    private AnnotationDefinition deathDate = new AnnotationDefinition();
    
    //////////
    // For "LENGTH_OF_TIME" type.
    //////////
    private AnnotationDefinition survivalLength = new AnnotationDefinition();
    private AnnotationDefinition survivalStatus = new AnnotationDefinition();
    private String valueForCensored;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the survivalValueType
     */
    public SurvivalValueTypeEnum getSurvivalValueType() {
        return survivalValueType;
    }
    
    /**
     * @param survivalValueType the survivalValueType to set
     */
    public void setSurvivalValueType(SurvivalValueTypeEnum survivalValueType) {
        this.survivalValueType = survivalValueType;
    }
    
    /**
     * @return the survivalStartDate
     */
    public AnnotationDefinition getSurvivalStartDate() {
        return survivalStartDate;
    }
    
    /**
     * @param survivalStartDate the survivalStartDate to set
     */
    public void setSurvivalStartDate(AnnotationDefinition survivalStartDate) {
        this.survivalStartDate = survivalStartDate;
    }
    
    /**
     * @return the lastFollowupDate
     */
    public AnnotationDefinition getLastFollowupDate() {
        return lastFollowupDate;
    }
    
    /**
     * @param lastFollowupDate the lastFollowupDate to set
     */
    public void setLastFollowupDate(AnnotationDefinition lastFollowupDate) {
        this.lastFollowupDate = lastFollowupDate;
    }
    
    /**
     * @return the deathDate
     */
    public AnnotationDefinition getDeathDate() {
        return deathDate;
    }
    
    /**
     * @param deathDate the deathDate to set
     */
    public void setDeathDate(AnnotationDefinition deathDate) {
        this.deathDate = deathDate;
    }

    /**
     * @return the survivalLength
     */
    public AnnotationDefinition getSurvivalLength() {
        return survivalLength;
    }

    /**
     * @param survivalLength the survivalLength to set
     */
    public void setSurvivalLength(AnnotationDefinition survivalLength) {
        this.survivalLength = survivalLength;
    }

    /**
     * @return the survivalStatus
     */
    public AnnotationDefinition getSurvivalStatus() {
        return survivalStatus;
    }

    /**
     * @param survivalStatus the survivalStatus to set
     */
    public void setSurvivalStatus(AnnotationDefinition survivalStatus) {
        this.survivalStatus = survivalStatus;
    }

    /**
     * @return the survivalLengthUnits
     */
    public SurvivalLengthUnitsEnum getSurvivalLengthUnits() {
        return survivalLengthUnits;
    }

    /**
     * @param survivalLengthUnits the survivalLengthUnits to set
     */
    public void setSurvivalLengthUnits(SurvivalLengthUnitsEnum survivalLengthUnits) {
        this.survivalLengthUnits = survivalLengthUnits;
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

}
