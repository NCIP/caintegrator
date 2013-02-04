/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonOperatorEnum;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


/**
 * Criterion handler for DateComparisonCriterion.
 */
public class DateComparisonCriterionHandler extends AbstractAnnotationCriterionHandler {

    private final DateComparisonCriterion dateComparisonCriterion;

    /**
     * @param criterion - The criterion object we are going to translate.
     */
    public DateComparisonCriterionHandler(DateComparisonCriterion criterion) {
        dateComparisonCriterion = criterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Criterion translate() {
        if (dateComparisonCriterion.getDateComparisonOperator() != null) {
            DateComparisonOperatorEnum operator = dateComparisonCriterion.getDateComparisonOperator();
            switch(operator) {
            case EQUAL:
                return Restrictions.eq(DATE_VALUE_COLUMN, dateComparisonCriterion.getDateValue());
            case GREATER:
                return Restrictions.gt(DATE_VALUE_COLUMN, dateComparisonCriterion.getDateValue());
            case GREATEROREQUAL:
                return Restrictions.ge(DATE_VALUE_COLUMN, dateComparisonCriterion.getDateValue());
            case LESS:
                return Restrictions.lt(DATE_VALUE_COLUMN, dateComparisonCriterion.getDateValue());
            case LESSOREQUAL:
                return Restrictions.le(DATE_VALUE_COLUMN, dateComparisonCriterion.getDateValue());
            case NOTEQUAL:
                return Restrictions.ne(DATE_VALUE_COLUMN, dateComparisonCriterion.getDateValue());
            default:
                throw new IllegalStateException("Unknown DateComparisonOperator: " + operator);
            }
        } else {
            throw new IllegalStateException("DateComparisonOperator is not set");
        }


    }

}
