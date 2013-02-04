/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Contains information for a single <code>AbstractCriterion</code>.
 */
public abstract class AbstractCriterionRow {

    private final CriteriaGroup group;
    private String newFieldName;

    AbstractCriterionRow(CriteriaGroup group) {
        this.group = group;
    }
    
    /**
     * Returns the available field names for this criterion.
     * 
     * @return the field names.
     */
    public abstract List<String> getAvailableFieldNames();

    /**
     * @return the fieldName
     */
    public abstract String getFieldName();
    
    /**
     * @param fieldName the fieldName to set
     */
    public final void setFieldName(String fieldName) {
        newFieldName = fieldName;
    }
    
    
    /**
     * @return the row type.
     */
    public abstract String getRowType();

    abstract AbstractCriterionWrapper getCriterionWrapper();

    CriteriaGroup getGroup() {
        return group;
    }
    
    AbstractCriterion getCriterion() {
        if (getCriterionWrapper() == null) {
            return null;
        } else {
            return getCriterionWrapper().getCriterion();
        }
    }

    void removeCriterionFromQuery() {
        getGroup().getCompoundCriterion().getCriterionCollection().remove(getCriterion());
    }
    
    void addCriterionToQuery() {
        getGroup().getCompoundCriterion().getCriterionCollection().add(getCriterion());
    }
    
    /**
     * Returns the parameters for this row.
     * 
     * @return the parameters.
     */
    public List<AbstractCriterionParameter> getParameters() {
        if (getCriterionWrapper() == null) {
            return Collections.emptyList();
        } else {
            return getCriterionWrapper().getParameters();
        }
    }

    abstract void setCriterion(AbstractCriterion criterion);

    void validate(ValidationAware action) {
        for (AbstractCriterionParameter parameter : getParameters()) {
            parameter.validate(action);
        }
    }

    int getRowIndex() {
        return getGroup().getRows().indexOf(this);
    }

    void processCriteriaChanges() {
        if (!StringUtils.equals(getFieldName(), newFieldName)) {
            handleFieldNameChange(newFieldName);
        } else {
            processParameterChanges();
        }
    }

    private void processParameterChanges() {
        for (AbstractCriterionParameter parameter : getParameters()) {
            parameter.processCriteriaChanges();
        }
    }

    abstract void handleFieldNameChange(String fieldName);

}
