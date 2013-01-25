/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator2.domain.application.AbstractAnnotationCriterion;
import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;

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
