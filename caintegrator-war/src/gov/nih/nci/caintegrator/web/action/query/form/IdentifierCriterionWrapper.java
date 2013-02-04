/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;
import gov.nih.nci.caintegrator.domain.application.IdentifierCriterion;
import gov.nih.nci.caintegrator.domain.application.WildCardTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps access to a <code>IdentifierCriterion</code>.
 */
final class IdentifierCriterionWrapper extends AbstractCriterionWrapper implements OperatorHandler {
    
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
    
    private final IdentifierCriterion criterion;
    public static final String SUBJECT_IDENTIFIER_FIELD_NAME = "Unique Subject Identifier";
    public static final String IMAGE_SERIES_IDENTIFIER_FIELD_NAME = "Unique Image Series Identifier";

    IdentifierCriterionWrapper(IdentifierCriterion criterion, IdentifierCriterionRow row) {
        this.criterion = criterion;
        getParameters().add(createValueParameter(row));
    }
    
    private TextFieldParameter createValueParameter(IdentifierCriterionRow row) {
        TextFieldParameter valueParameter = new TextFieldParameter(0, row.getRowIndex(), 
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

    @Override
    String getFieldName() {
        if (EntityTypeEnum.IMAGESERIES.equals(criterion.getEntityType())) {
            return IMAGE_SERIES_IDENTIFIER_FIELD_NAME;
        }
        return SUBJECT_IDENTIFIER_FIELD_NAME;
        
    }
    

    @Override
    AbstractCriterion getCriterion() {
        return criterion;
    }

    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.IDENTIFIER;
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
}
