/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.domain.AbstractCaIntegrator2Object;
import gov.nih.nci.caintegrator2.domain.annotation.AbstractAnnotationValue;

/**
 * 
 */
public class ResultValue extends AbstractCaIntegrator2Object {

    private static final long serialVersionUID = 1L;
    private AbstractAnnotationValue value;
    private ResultColumn column;

    /**
     * @return the value
     */
    public AbstractAnnotationValue getValue() {
        return value;
    }
    
    /**
     * @param value the value to set
     */
    public void setValue(AbstractAnnotationValue value) {
        this.value = value;
    }
    
    /**
     * @return the column
     */
    public ResultColumn getColumn() {
        return column;
    }
    
    /**
     * @param column the column to set
     */
    public void setColumn(ResultColumn column) {
        this.column = column;
    }
    
    /**
     * {@inheritDoc}
     */
    public String toString() {
        return value != null ? value.toString() : "";
    }
}
