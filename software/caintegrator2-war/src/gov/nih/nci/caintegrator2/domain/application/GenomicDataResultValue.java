/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class GenomicDataResultValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private static final String NEGATIVE_COLOR_CODE_BLUE = "#0066CC";
    private static final String POSITIVE_COLOR_CODE_RED = "#CC3333";
    private static final String HIGH_VARIANCE_APPEND_STRING = "**";
    private Float value;
    private GenomicDataResultColumn column;
    private boolean meetsCriterion = false;
    private boolean highVariance = false;
    
    /**
     * 
     * @return displayable result value.
     */
    public String getDisplayableValue() {
        return value + (highVariance ? HIGH_VARIANCE_APPEND_STRING : "");
    }
    
    /**
     * @return the value
     */
    public Float getValue() {
        return value;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(Float value) {
        this.value = value;
    }
    
    /**
     * @return the column
     */
    public GenomicDataResultColumn getColumn() {
        return column;
    }
    
    /**
     * @param column the column to set
     */
    public void setColumn(GenomicDataResultColumn column) {
        this.column = column;
    }

    /**
     * @return the meetsCriterion
     */
    public boolean isMeetsCriterion() {
        return meetsCriterion;
    }

    /**
     * @param meetsCriterion the meetsCriterion to set
     */
    public void setMeetsCriterion(boolean meetsCriterion) {
        this.meetsCriterion = meetsCriterion;
    }
    
    /**
     * For the JSP to get the color to highlight based on the value.
     * @return highlight color.
     */
    public String getHighlightColor() {
        return value >= 0 ? POSITIVE_COLOR_CODE_RED : NEGATIVE_COLOR_CODE_BLUE;
    }

    /**
     * @return the highVariance
     */
    public boolean isHighVariance() {
        return highVariance;
    }

    /**
     * @param highVariance the highVariance to set
     */
    public void setHighVariance(boolean highVariance) {
        this.highVariance = highVariance;
    }

}
