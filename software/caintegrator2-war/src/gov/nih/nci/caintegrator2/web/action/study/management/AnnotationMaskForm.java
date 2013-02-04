/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.domain.annotation.mask.MaxNumberMask;
import gov.nih.nci.caintegrator2.domain.annotation.mask.NumericRangeMask;

/**
 * 
 */
public class AnnotationMaskForm {
    
    private boolean hasMaxNumberMask = false;
    private boolean hasNumericRangeMask = false;
    private MaxNumberMask maxNumberMask = new MaxNumberMask();
    private NumericRangeMask numericRangeMask = new NumericRangeMask();
    
    /**
     * Clears the existing data.
     */
    public void clear() {
        hasMaxNumberMask = false;
        hasNumericRangeMask = false;
        maxNumberMask = new MaxNumberMask();
        numericRangeMask = new NumericRangeMask();
    }
    
    /**
     * @return the hasMaxNumberMask
     */
    public boolean isHasMaxNumberMask() {
        return hasMaxNumberMask;
    }

    /**
     * @param hasMaxNumberMask the hasMaxNumberMask to set
     */
    public void setHasMaxNumberMask(boolean hasMaxNumberMask) {
        this.hasMaxNumberMask = hasMaxNumberMask;
    }

    /**
     * @return the hasNumericRangeMask
     */
    public boolean isHasNumericRangeMask() {
        return hasNumericRangeMask;
    }

    /**
     * @param hasNumericRangeMask the hasNumericRangeMask to set
     */
    public void setHasNumericRangeMask(boolean hasNumericRangeMask) {
        this.hasNumericRangeMask = hasNumericRangeMask;
    }

    /**
     * @return the maxNumberMask
     */
    public MaxNumberMask getMaxNumberMask() {
        return maxNumberMask;
    }

    /**
     * @param maxNumberMask the maxNumberMask to set
     */
    public void setMaxNumberMask(MaxNumberMask maxNumberMask) {
        this.maxNumberMask = maxNumberMask;
    }

    /**
     * @return the numericRangeMask
     */
    public NumericRangeMask getNumericRangeMask() {
        return numericRangeMask;
    }

    /**
     * @param numericRangeMask the numericRangeMask to set
     */
    public void setNumericRangeMask(NumericRangeMask numericRangeMask) {
        this.numericRangeMask = numericRangeMask;
    }

}
