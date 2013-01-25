/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;


import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;

/**
 * 
 */
public class GenomicDataResultValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    
    private Float value;
    private GenomicDataResultColumn column;
    private boolean meetsCriterion = false;
    
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

}
