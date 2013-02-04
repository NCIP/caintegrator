/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import gov.nih.nci.caintegrator.common.NumericUtil;

/**
 *
 */
public class NumericComparisonCriterion extends AbstractComparisonCriterion {

    private static final long serialVersionUID = 1L;

    private NumericComparisonOperatorEnum numericComparisonOperator;
    private Double numericValue;

    /**
     * @return the numericValue
     */
    public Double getNumericValue() {
        return numericValue;
    }

    /**
     * @param numericValue the numericValue to set
     */
    public void setNumericValue(Double numericValue) {
        this.numericValue = numericValue;
    }

    /**
     * @return the numericComparisonOperator
     */
    public NumericComparisonOperatorEnum getNumericComparisonOperator() {
        return numericComparisonOperator;
    }

    /**
     * @param numericComparisonOperator the numericComparisonOperator to set
     */
    public void setNumericComparisonOperator(NumericComparisonOperatorEnum numericComparisonOperator) {
        this.numericComparisonOperator = numericComparisonOperator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringValue() {
        return numericValue != null ? NumericUtil.formatDisplay(numericValue) : "";
    }

}
