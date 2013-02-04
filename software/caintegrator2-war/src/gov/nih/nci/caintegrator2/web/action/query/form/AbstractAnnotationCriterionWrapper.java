/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;

/**
 * Wraps access to an <code>AbstractAnnotationCriterion</code> subclass instance.
 */
abstract class AbstractAnnotationCriterionWrapper extends AbstractCriterionWrapper implements OperatorHandler {

    private final AbstractAnnotationCriterionRow row;

    AbstractAnnotationCriterionWrapper(AbstractAnnotationCriterionRow row) {
        this.row = row;
    }

    @Override
    final AbstractCriterion getCriterion() {
        return getAbstractAnnotationCriterion();
    }

    abstract AbstractAnnotationCriterion getAbstractAnnotationCriterion();

    @Override
    String getFieldName() {
        if (getAbstractAnnotationCriterion().getAnnotationDefinition() == null) {
            return "";
        } else {
            return getAbstractAnnotationCriterion().getAnnotationDefinition().getDisplayName();
        }
    }

    void setField(AnnotationDefinition field) {
        getAbstractAnnotationCriterion().setAnnotationDefinition(field);
    }

    AbstractAnnotationCriterionRow getRow() {
        return row;
    }

}
