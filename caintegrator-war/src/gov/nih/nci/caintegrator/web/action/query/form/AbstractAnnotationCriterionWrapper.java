/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;

/**
 * Wraps access to an <code>AbstractAnnotationCriterion</code> subclass instance.
 */
abstract class AbstractAnnotationCriterionWrapper extends AbstractCriterionWrapper implements OperatorHandler {

    private final AnnotationCriterionRow row;

    AbstractAnnotationCriterionWrapper(AnnotationCriterionRow row) {
        this.row = row;
    }

    @Override
    final AbstractCriterion getCriterion() {
        return getAbstractAnnotationCriterion();
    }

    abstract AbstractAnnotationCriterion getAbstractAnnotationCriterion();

    @Override
    String getFieldName() {
        AnnotationFieldDescriptor descriptor = getAbstractAnnotationCriterion().getAnnotationFieldDescriptor();
        if (descriptor == null || descriptor.getDefinition() == null) {
            return "";
        } else {
            return descriptor.getDefinition().getDisplayName();
        }
    }
    
    @Override
    void setField(AnnotationFieldDescriptor field) {
        getAbstractAnnotationCriterion().setAnnotationFieldDescriptor(field);
    }

    AnnotationCriterionRow getRow() {
        return row;
    }

}
