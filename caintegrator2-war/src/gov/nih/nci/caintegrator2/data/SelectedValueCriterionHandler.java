/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.common.DateUtil;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.application.SelectedValueCriterion;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


/**
 * Criterion handler for SelectedValueCriterion.
 */
public class SelectedValueCriterionHandler extends AbstractAnnotationCriterionHandler {

    private final SelectedValueCriterion selectedValueCriterion;
    
    /**
     * @param criterion - The criterion object we are going to translate.
     */
    public SelectedValueCriterionHandler(SelectedValueCriterion criterion) {
        selectedValueCriterion = criterion;   
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Criterion translate() {
        switch (selectedValueCriterion.getAnnotationFieldDescriptor().getDefinition().getDataType()) {
        case STRING:
            return Restrictions.in(STRING_VALUE_COLUMN, getSelectedValues());
        case NUMERIC:
            return Restrictions.in(NUMERIC_VALUE_COLUMN, getSelectedNumericValues());
        case DATE:
            return Restrictions.in(DATE_VALUE_COLUMN, getSelectedDateValues());
        default:
            throw new IllegalArgumentException("Unsupported type " 
                    + selectedValueCriterion.getAnnotationFieldDescriptor().getDefinition().getDataType());
        }
    }

    private String[] getSelectedValues() {
        List<String> values = new ArrayList<String>();
        for (PermissibleValue permissibleValue : selectedValueCriterion.getValueCollection()) {
            values.add(permissibleValue.getValue());
        }
        return values.toArray(new String[values.size()]);
    }

    private Double[] getSelectedNumericValues() {
        List<Double> values = new ArrayList<Double>();
        for (PermissibleValue permissibleValue : selectedValueCriterion.getValueCollection()) {
            values.add(Double.valueOf(permissibleValue.getValue()));
        }
        return values.toArray(new Double[values.size()]);
    }

    private Date[] getSelectedDateValues() {
        List<Date> values = new ArrayList<Date>();
        for (PermissibleValue permissibleValue : selectedValueCriterion.getValueCollection()) {
            try {
                values.add(DateUtil.createDate(permissibleValue.getValue()));
            } catch (ParseException e) {
                throw new IllegalStateException("Invalid date format as a Permissible Value, unable to parse.", e);
            }
        }
        return values.toArray(new Date[values.size()]);
    }

}
