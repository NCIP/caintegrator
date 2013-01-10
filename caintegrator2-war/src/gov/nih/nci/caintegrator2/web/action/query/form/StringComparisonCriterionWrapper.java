/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.StringComparisonCriterion;
import gov.nih.nci.caintegrator2.domain.application.WildCardTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps access to a <code>StringComparisonCriterion</code>.
 */
class StringComparisonCriterionWrapper extends AbstractAnnotationCriterionWrapper {

    private static final Map<CriterionOperatorEnum, WildCardTypeEnum> OPERATOR_TO_WILDCARD_MAP = 
        new HashMap<CriterionOperatorEnum, WildCardTypeEnum>();
    private static final Map<WildCardTypeEnum, CriterionOperatorEnum> WILDCARD_TO_OPERATOR_MAP = 
        new HashMap<WildCardTypeEnum, CriterionOperatorEnum>();
    
    static {
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.BEGINS_WITH, WildCardTypeEnum.WILDCARD_AFTER_STRING);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.CONTAINS, WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.ENDS_WITH, WildCardTypeEnum.WILDCARD_BEFORE_STRING);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.EQUALS, WildCardTypeEnum.WILDCARD_OFF);
        OPERATOR_TO_WILDCARD_MAP.put(CriterionOperatorEnum.NOT_EQUAL_TO, WildCardTypeEnum.NOT_EQUAL_TO);

        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_AFTER_STRING, CriterionOperatorEnum.BEGINS_WITH);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_BEFORE_AND_AFTER_STRING, CriterionOperatorEnum.CONTAINS);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_BEFORE_STRING, CriterionOperatorEnum.ENDS_WITH);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.WILDCARD_OFF, CriterionOperatorEnum.EQUALS);
        WILDCARD_TO_OPERATOR_MAP.put(WildCardTypeEnum.NOT_EQUAL_TO, CriterionOperatorEnum.NOT_EQUAL_TO);
    }
    
    private final StringComparisonCriterion criterion;

    StringComparisonCriterionWrapper(final StringComparisonCriterion criterion, AnnotationCriterionRow row) {
        super(row);
        this.criterion = criterion;
        getParameters().add(createValueParameter());
    }

    private TextFieldParameter createValueParameter() {
        TextFieldParameter valueParameter = new TextFieldParameter(0, getRow().getRowIndex(), 
                                                                   criterion.getStringValue());
        valueParameter.setOperatorHandler(this);
        ValueHandler valueHandler = new ValueHandlerAdapter() {
            /**
             * {@inheritDoc}
             */
            public void valueChanged(String value) {
                criterion.setStringValue(value);
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
        return CriterionOperatorEnum.STRING_OPERATORS;
    }

    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum getOperator() {
        if (criterion.getWildCardType() == null) {
            return null;
        } else {
            return WILDCARD_TO_OPERATOR_MAP.get(criterion.getWildCardType());
        }
    }


    /**
     * {@inheritDoc}
     */
    public void operatorChanged(AbstractCriterionParameter parameter, CriterionOperatorEnum operator) {
        if (operator == null) {
            criterion.setWildCardType(null);
        } else {
            criterion.setWildCardType(OPERATOR_TO_WILDCARD_MAP.get(operator));
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.STRING_COMPARISON;
    }

}
