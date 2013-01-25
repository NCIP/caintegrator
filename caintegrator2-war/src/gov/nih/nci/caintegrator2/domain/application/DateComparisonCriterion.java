/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import gov.nih.nci.caintegrator2.common.DateUtil;

import java.util.Date;

/**
 *
 */
public class DateComparisonCriterion extends AbstractComparisonCriterion {

    private static final long serialVersionUID = 1L;

    private DateComparisonOperatorEnum dateComparisonOperator;
    private Date dateValue;

    /**
     * @return the DateComparisonOperator
     */
    public DateComparisonOperatorEnum getDateComparisonOperator() {
        return dateComparisonOperator;
    }

    /**
     * @param dateComparisonOperator the DateComparisonOperator to set
     */
    public void setDateComparisonOperator(DateComparisonOperatorEnum dateComparisonOperator) {
        this.dateComparisonOperator = dateComparisonOperator;
    }

    /**
     * @return the DateValue
     */
    public Date getDateValue() {
        return dateValue;
    }

    /**
     * @param dateValue the DateValue to set
     */
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStringValue() {
        return DateUtil.toString(dateValue);
    }

}
