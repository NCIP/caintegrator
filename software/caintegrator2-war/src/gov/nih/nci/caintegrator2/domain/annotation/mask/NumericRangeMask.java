/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.annotation.mask;

/**
 * Mask used to mask a number between a range.  If the numericRange is 5, the ranges would be:
 * 0-4, 5-9, 10-14, etc.... and the actual number would fall in between a range.
 */
public class NumericRangeMask extends AbstractAnnotationMask {

    private static final long serialVersionUID = 1L;

    private static final Integer SORT_ORDER = 2;

    private Integer numericRange;

    /**
     * @return the numericRange
     */
    public Integer getNumericRange() {
        return numericRange;
    }

    /**
     * @param numericRange the numericRange to set
     */
    public void setNumericRange(Integer numericRange) {
        this.numericRange = numericRange;
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
        return "Numeric Range = " + numericRange;
    }
    
    
}
