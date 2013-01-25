/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.annotation;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class SurvivalValueDefinition extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private String name;
    private String survivalValueType;
    private AnnotationDefinition survivalStartDate;
    private AnnotationDefinition lastFollowupDate;
    private AnnotationDefinition deathDate;
    
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

}
