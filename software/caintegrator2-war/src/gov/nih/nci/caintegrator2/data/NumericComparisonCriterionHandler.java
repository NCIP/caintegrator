/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.NumericComparisonOperatorEnum;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


/**
 * Criterion handler for NumericComparisonCriterion.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // see translate() method
public class NumericComparisonCriterionHandler extends AbstractAnnotationCriterionHandler {

    private final NumericComparisonCriterion numericComparisonCriterion;
    
    /**
     * @param criterion - The criterion object we are going to translate.
     */
    public NumericComparisonCriterionHandler(NumericComparisonCriterion criterion) {
        numericComparisonCriterion = criterion;   
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.CyclomaticComplexity")   // switch statement and argument checking
    Criterion translate() {
        if (numericComparisonCriterion.getNumericComparisonOperator() != null) {
            NumericComparisonOperatorEnum operator = numericComparisonCriterion.getNumericComparisonOperator();
            switch(operator) {
            case EQUAL:
                return Restrictions.eq(NUMERIC_VALUE_COLUMN, numericComparisonCriterion.getNumericValue());
            case GREATER:
                return Restrictions.gt(NUMERIC_VALUE_COLUMN, numericComparisonCriterion.getNumericValue());
            case GREATEROREQUAL:
                return Restrictions.ge(NUMERIC_VALUE_COLUMN, numericComparisonCriterion.getNumericValue());
            case LESS:
                return Restrictions.lt(NUMERIC_VALUE_COLUMN, numericComparisonCriterion.getNumericValue());
            case LESSOREQUAL:
                return Restrictions.le(NUMERIC_VALUE_COLUMN, numericComparisonCriterion.getNumericValue());
            case NOTEQUAL:
                return Restrictions.ne(NUMERIC_VALUE_COLUMN, numericComparisonCriterion.getNumericValue());
            default:
                throw new IllegalStateException("Unknown NumericComparisonOperator: " + operator);
            }
        } else {
            throw new IllegalStateException("NumericComparisonOperator is not set");
        }
        
        
    }

}
