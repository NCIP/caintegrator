/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractGenomicCriterion;

/**
 * Wraps access to an <code>AbstractGenomicCriterion</code> subclass instance.
 */
abstract class AbstractGenomicCriterionWrapper extends AbstractCriterionWrapper {

    private final GeneExpressionCriterionRow row;

    AbstractGenomicCriterionWrapper(GeneExpressionCriterionRow row) {
        this.row = row;
    }

    @Override
    final AbstractCriterion getCriterion() {
        return getAbstractGenomicCriterion();
    }

    abstract AbstractGenomicCriterion getAbstractGenomicCriterion();

    GeneExpressionCriterionRow getRow() {
        return row;
    }

}
