/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;

/**
 * 
 */
public class InvalidCriterionWrapper extends AbstractCriterionWrapper {
    private AbstractCriterion criterion;
    
    
    /**
     * @param criterion the criterion to set
     */
    public void setCriterion(AbstractCriterion criterion) {
        this.criterion = criterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    AbstractCriterion getCriterion() {
        return criterion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    CriterionTypeEnum getCriterionType() {
        return null;
    }

    @Override
    String getFieldName() {
        return null;
    }

}
