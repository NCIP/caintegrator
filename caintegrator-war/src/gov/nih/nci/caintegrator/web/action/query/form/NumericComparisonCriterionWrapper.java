/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonCriterion;
import gov.nih.nci.caintegrator.domain.application.NumericComparisonOperatorEnum;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Wraps access to a <code>NumericComparisonCriterion</code>.
 */
final class NumericComparisonCriterionWrapper extends AbstractAnnotationCriterionWrapper {

    private static final Map<CriterionOperatorEnum, NumericComparisonOperatorEnum> OPERATOR_TO_NUMERIC_COMPARISON_MAP =
        new HashMap<CriterionOperatorEnum, NumericComparisonOperatorEnum>();
    private static final Map<NumericComparisonOperatorEnum, CriterionOperatorEnum> NUMERIC_COMPARISON_TO_OPERATOR_MAP =
        new HashMap<NumericComparisonOperatorEnum, CriterionOperatorEnum>();

    static {
        OPERATOR_TO_NUMERIC_COMPARISON_MAP.put(CriterionOperatorEnum.EQUALS, NumericComparisonOperatorEnum.EQUAL);
        OPERATOR_TO_NUMERIC_COMPARISON_MAP.put(CriterionOperatorEnum.GREATER_THAN,
                NumericComparisonOperatorEnum.GREATER);
        OPERATOR_TO_NUMERIC_COMPARISON_MAP.put(CriterionOperatorEnum.GREATER_THAN_OR_EQUAL_TO,
                NumericComparisonOperatorEnum.GREATEROREQUAL);
        OPERATOR_TO_NUMERIC_COMPARISON_MAP.put(CriterionOperatorEnum.LESS_THAN, NumericComparisonOperatorEnum.LESS);
        OPERATOR_TO_NUMERIC_COMPARISON_MAP.put(CriterionOperatorEnum.LESS_THAN_OR_EQUAL_TO,
                NumericComparisonOperatorEnum.LESSOREQUAL);

        NUMERIC_COMPARISON_TO_OPERATOR_MAP.put(NumericComparisonOperatorEnum.EQUAL, CriterionOperatorEnum.EQUALS);
        NUMERIC_COMPARISON_TO_OPERATOR_MAP.put(NumericComparisonOperatorEnum.GREATER,
                CriterionOperatorEnum.GREATER_THAN);
        NUMERIC_COMPARISON_TO_OPERATOR_MAP.put(NumericComparisonOperatorEnum.GREATEROREQUAL,
                CriterionOperatorEnum.GREATER_THAN_OR_EQUAL_TO);
        NUMERIC_COMPARISON_TO_OPERATOR_MAP.put(NumericComparisonOperatorEnum.LESS, CriterionOperatorEnum.LESS_THAN);
        NUMERIC_COMPARISON_TO_OPERATOR_MAP.put(NumericComparisonOperatorEnum.LESSOREQUAL,
                CriterionOperatorEnum.LESS_THAN_OR_EQUAL_TO);
}

    private final NumericComparisonCriterion criterion;

    NumericComparisonCriterionWrapper(final NumericComparisonCriterion criterion, AnnotationCriterionRow row) {
        super(row);
        this.criterion = criterion;
        if (criterion.getNumericValue() == null) {
            criterion.setNumericValue(0.0);
        }
        getParameters().add(createValueParameter());
    }

    private TextFieldParameter createValueParameter() {
        TextFieldParameter valueParameter = new TextFieldParameter(0, getRow().getRowIndex(),
                                                                   criterion.getNumericValue().toString());
        valueParameter.setOperatorHandler(this);
        ValueHandler valueHandler = new ValueHandlerAdapter() {

            @Override
            public boolean isValid(String value) {
                try {
                    Double.parseDouble(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }

            @Override
            public void validate(String formFieldName, String value, ValidationAware action) {
                if (!isValid(value)) {
                    action.addActionError("Numeric value required for " + getFieldName());
                }
            }

            @Override
            public void valueChanged(String value) {
                criterion.setNumericValue(Double.valueOf(value));
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
    @Override
    public CriterionOperatorEnum[] getAvailableOperators() {
        return CriterionOperatorEnum.NUMERIC_OPERATORS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CriterionOperatorEnum getOperator() {
        if (criterion.getNumericComparisonOperator() == null) {
            return null;
        } else {
            return NUMERIC_COMPARISON_TO_OPERATOR_MAP.get(criterion.getNumericComparisonOperator());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void operatorChanged(AbstractCriterionParameter parameter, CriterionOperatorEnum operator) {
        if (operator == null) {
            criterion.setNumericComparisonOperator(null);
        } else {
            criterion.setNumericComparisonOperator(OPERATOR_TO_NUMERIC_COMPARISON_MAP.get(operator));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.NUMERIC_COMPARISON;
    }

}
