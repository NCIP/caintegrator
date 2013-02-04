/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        List<PermissibleValue> orderedValues = new ArrayList<PermissibleValue>();
        orderedValues.addAll(criterion.getAnnotationFieldDescriptor().getDefinition().getPermissibleValueCollection());
        Comparator<PermissibleValue> valueComparator = new Comparator<PermissibleValue>() {
            public int compare(PermissibleValue value1, PermissibleValue value2) {
                return value1.toString().compareTo(value2.toString());
            }
        };
        Collections.sort(orderedValues, valueComparator);
        OptionList<PermissibleValue> options = new OptionList<PermissibleValue>();
        for (PermissibleValue value : orderedValues) {
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
