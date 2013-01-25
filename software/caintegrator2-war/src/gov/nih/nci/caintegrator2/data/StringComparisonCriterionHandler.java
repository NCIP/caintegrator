/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;


/**
 * Criterion handler for StringComparisonCriterion.
 */
@SuppressWarnings("PMD.CyclomaticComplexity")   // see translate() method
public class StringComparisonCriterionHandler extends AbstractAnnotationCriterionHandler {

    private final StringComparisonCriterion stringComparisonCriterion;
    private String columnName = STRING_VALUE_COLUMN;
    
    /**
     * @param criterion - The criterion object we are going to translate.
     */
    public StringComparisonCriterionHandler(StringComparisonCriterion criterion) {
        stringComparisonCriterion = criterion;   
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.ExcessiveMethodLength" }) // switch statement
    Criterion translate() {
        if (stringComparisonCriterion.getWildCardType() != null) {
            WildCardTypeEnum wildCardType = stringComparisonCriterion.getWildCardType();
            switch(wildCardType) {
            case WILDCARD_OFF:
                return Restrictions.like(columnName, 
                                         stringComparisonCriterion.getStringValue());
            case WILDCARD_AFTER_STRING:
                return Restrictions.like(columnName, 
                                         stringComparisonCriterion.getStringValue(), 
                                         MatchMode.START);
            case WILDCARD_BEFORE_STRING:
                return Restrictions.like(columnName, 
                                         stringComparisonCriterion.getStringValue(), 
                                         MatchMode.END);
            case WILDCARD_BEFORE_AND_AFTER_STRING:
                return Restrictions.like(columnName, 
                                         stringComparisonCriterion.getStringValue(), 
                                         MatchMode.ANYWHERE);
            case NOT_EQUAL_TO:
                return Restrictions.ne(columnName, 
                                         stringComparisonCriterion.getStringValue());
            default:
                return Restrictions.like(columnName, 
                                         stringComparisonCriterion.getStringValue());
            }
        } else {
            return Restrictions.like(columnName, 
                                     stringComparisonCriterion.getStringValue());
        }
    }
    
    /**
     * Sets the column name for the field to use that we are comparing the string value to.
     * @param columnName columnName to use.
     */
    protected void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
