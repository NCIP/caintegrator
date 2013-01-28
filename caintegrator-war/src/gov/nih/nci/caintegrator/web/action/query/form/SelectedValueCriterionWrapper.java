/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator.domain.application.SelectedValueCriterion;

import java.util.List;

/**
 * Wraps access to a <code>SelectedValueCriterion</code>.
 */
final class SelectedValueCriterionWrapper extends AbstractAnnotationCriterionWrapper {

    private final SelectedValueCriterion criterion;
    private CriterionOperatorEnum selectOperator;

    SelectedValueCriterionWrapper(SelectedValueCriterion criterion, AnnotationCriterionRow row) {
        super(row);
        this.criterion = criterion;
        if (criterion.getValueCollection().size() <= 1) {
            selectOperator = CriterionOperatorEnum.EQUALS;
        } else {
            selectOperator = CriterionOperatorEnum.IN;
        }
        getParameters().add(createParameter());
    }

    /**
     * @return
     */
    private AbstractCriterionParameter createParameter() {
        switch (selectOperator) {
        case EQUALS:
            return createSelectListParameter();
        case IN:
            return createMultiSelectParameter();
        default: 
            throw new IllegalStateException("Unsupported operator: " + selectOperator);
        }
    }

    private AbstractCriterionParameter createMultiSelectParameter() {
        ValuesSelectedHandler<PermissibleValue> handler = 
            new ValuesSelectedHandler<PermissibleValue>() {

                public void valuesSelected(List<PermissibleValue> values) {
                    criterion.getValueCollection().clear();
                    criterion.getValueCollection().addAll(values);
                }
            
        };
        MultiSelectParameter<PermissibleValue> parameter = 
            new MultiSelectParameter<PermissibleValue>(0, getRow().getRowIndex(), 
                    getOptions(), handler, criterion.getValueCollection());
        parameter.setOperatorHandler(this);
        return parameter;
    }

    private AbstractCriterionParameter createSelectListParameter() {
        ValueSelectedHandler<PermissibleValue> handler = new ValueSelectedHandler<PermissibleValue>() {
            public void valueSelected(PermissibleValue value) {
                criterion.getValueCollection().clear();
                if (value != null) {
                    criterion.getValueCollection().add(value);
                }
            }
        };
        PermissibleValue value;
        if (criterion.getValueCollection().isEmpty()) {
            value = null;
        } else {
            value = criterion.getValueCollection().iterator().next();
        }
        SelectListParameter<PermissibleValue> parameter = 
            new SelectListParameter<PermissibleValue>(0, getRow().getRowIndex(), getOptions(), handler, value);
        parameter.setOperatorHandler(this);
        return parameter;
    }
    
    private OptionList<PermissibleValue> getOptions() {
        OptionList<PermissibleValue> options = new OptionList<PermissibleValue>();
        for (PermissibleValue value
                : criterion.getAnnotationFieldDescriptor().getDefinition().getSortedPermissibleValueList()) {
            options.addOption(value.toString(), value);
        }
        return options;
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
        return CriterionOperatorEnum.SELECT_LIST_OPERATORS;
    }

    /**
     * {@inheritDoc}
     */
    public CriterionOperatorEnum getOperator() {
        return selectOperator;
    }

    /**
     * {@inheritDoc}
     */
    public void operatorChanged(AbstractCriterionParameter parameter, CriterionOperatorEnum operator) {
        selectOperator = operator;
        refreshParameters();
    }

    private void refreshParameters() {
        getParameters().clear();
        getParameters().add(createParameter());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return CriterionTypeEnum.SELECTED_VALUE;
    }

    @Override
    void setField(AnnotationFieldDescriptor field) {
        super.setField(field);
        criterion.getValueCollection().clear();
        refreshParameters();
    }

}
