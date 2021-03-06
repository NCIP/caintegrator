/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.annotation.mask;

/**
 * Mask used to convert any number higher than the maxNumber to the max number, so if it was 90 as the maxNumber
 * and a value was 95, it would be converted to "90+" for the user to see.  See <code>MaxNumberMaskHandler</code>
 */
public class MaxNumberMask extends AbstractAnnotationMask {

    private static final long serialVersionUID = 1L;

    private static final Integer SORT_ORDER = 1;
        
    private Double maxNumber;

    /**
     * @return the maxNumber
     */
    public Double getMaxNumber() {
        return maxNumber;
    }

    /**
     * @param maxNumber the maxNumber to set
     */
    public void setMaxNumber(Double maxNumber) {
        this.maxNumber = maxNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getMaskOrder() {
        return SORT_ORDER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayableRestriction() {
        return "Maximum Number = " + maxNumber;
    }
    
    
}
