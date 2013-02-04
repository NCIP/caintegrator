/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder for an invalid criterion row.
 */
public class InvalidCriterionRow extends AbstractCriterionRow {

    private InvalidCriterionWrapper criterionWrapper;

    InvalidCriterionRow(CriteriaGroup group) {
        super(group);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFieldName() {
        return "";
    }

    @Override
    void handleFieldNameChange(String fieldName) {
        // NOOP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractCriterionWrapper getCriterionWrapper() {
        return criterionWrapper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableFieldNames() {
        return new ArrayList<String>();
    }

    @Override
    void setCriterion(AbstractCriterion criterion) {
        criterionWrapper = new InvalidCriterionWrapper();
        criterionWrapper.setCriterion(criterion);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRowType() {
        return "Invalid Criterion, Please Delete";
    }

}
