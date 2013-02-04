/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.DateComparisonOperatorEnum;
import gov.nih.nci.caintegrator2.common.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a <code>DateComparisonCriterion</code>.
 */
@SuppressWarnings("PMD.CyclomaticComplexity") // anonymous inner class
final class DateComparisonCriterionWrapper extends AbstractAnnotationCriterionWrapper {

    private static final Map<CriterionOperatorEnum, DateComparisonOperatorEnum> OPERATOR_TO_DATE_COMPARISON_MAP = 
        new HashMap<CriterionOperatorEnum, DateComparisonOperatorEnum>();
    private static final Map<DateComparisonOperatorEnum, CriterionOperatorEnum> DATE_COMPARISON_TO_OPERATOR_MAP = 
        new HashMap<DateComparisonOperatorEnum, CriterionOperatorEnum>();
    
    static {
        OPERATOR_TO_DATE_COMPARISON_MAP.put(CriterionOperatorEnum.EQUALS, DateComparisonOperatorEnum.EQUAL);
        OPERATOR_TO_DATE_COMPARISON_MAP.put(CriterionOperatorEnum.GREATER_THAN, 
                DateComparisonOperatorEnum.GREATER);
        OPERATOR_TO_DATE_COMPARISON_MAP.put(CriterionOperatorEnum.GREATER_THAN_OR_EQUAL_TO, 
                DateComparisonOperatorEnum.GREATEROREQUAL);
        OPERATOR_TO_DATE_COMPARISON_MAP.put(CriterionOperatorEnum.LESS_THAN, DateComparisonOperatorEnum.LESS);
        OPERATOR_TO_DATE_COMPARISON_MAP.put(CriterionOperatorEnum.LESS_THAN_OR_EQUAL_TO, 
                DateComparisonOperatorEnum.LESSOREQUAL);

        DATE_COMPARISON_TO_OPERATOR_MAP.put(DateComparisonOperatorEnum.EQUAL, CriterionOperatorEnum.EQUALS);
        DATE_COMPARISON_TO_OPERATOR_MAP.put(DateComparisonOperatorEnum.GREATER, 
                CriterionOperatorEnum.GREATER_THAN);
        DATE_COMPARISON_TO_OPERATOR_MAP.put(DateComparisonOperatorEnum.GREATEROREQUAL, 
                CriterionOperatorEnum.GREATER_THAN_OR_EQUAL_TO);
        DATE_COMPARISON_TO_OPERATOR_MAP.put(DateComparisonOperatorEnum.LESS, CriterionOperatorEnum.LESS_THAN);
        DATE_COMPARISON_TO_OPERATOR_MAP.put(DateComparisonOperatorEnum.LESSOREQUAL, 
                CriterionOperatorEnum.LESS_THAN_OR_EQUAL_TO);
}

    private final DateComparisonCriterion criterion;

    DateComparisonCriterionWrapper(final DateComparisonCriterion criterion,
            AnnotationCriterionRow row) {
        super(row);
        this.criterion = criterion;
        if (criterion.getDateValue() == null) {
            criterion.setDateValue(new Date());
        }
        getParameters().add(createValueParameter());
    }

    @SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.ExcessiveMethodLength" }) // anonymous inner class
    private TextFieldParameter createValueParameter() {
        TextFieldParameter valueParameter = new TextFieldParameter(0, getRow().getRowIndex(),
                DateUtil.toString(criterion.getDateValue()));
        valueParameter.setOperatorHandler(this);
        ValueHandler valueHandler = new ValueHandlerAdapter() {

            @Override
            public boolean isValid(String value) {
                try {
                    DateUtil.createDate(value);
                    return true;
                } catch (ParseException e) {
                    return false;
                }
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (StringUtils.isEmpty(value)) {
                    action.addActionError("Date value is required for " + getFieldName());
                } else if (!isValid(value)) {
                    action.addActionError("Date value is invalid for " + getFieldName() + "='" + value + "'");
                }
            }
            
            @Override
            public void valueChanged(String value) {
                try {
                    criterion.setDateValue(DateUtil.createDate(value));
                } catch (ParseException e) {
                    return;  //This shouldn't happen since we already called validate
                }
            }
        };
        valueParameter.setValueHandler(valueHandler);
        return valueParameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractAnnotationCriterion getAbstractAnnotationCriterion() {
        return criterion;
    }

    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum[] getAvailableOperators() {
        return CriterionOperatorEnum.DATE_OPERATORS;
    }

    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum getOperator() {
        if (criterion.getDateComparisonOperator() == null) {
            return null;
        } else {
            return DATE_COMPARISON_TO_OPERATOR_MAP.get(criterion.getDateComparisonOperator());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void operatorChanged(AbstractCriterionParameter parameter, CriterionOperatorEnum operator) {
        if (operator == null) {
            criterion.setDateComparisonOperator(null);
        } else {
            criterion.setDateComparisonOperator(OPERATOR_TO_DATE_COMPARISON_MAP.get(operator));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.DATE_COMPARISON;
    }

}
