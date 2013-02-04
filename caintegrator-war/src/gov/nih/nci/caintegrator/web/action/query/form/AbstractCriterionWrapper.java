/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.domain.application.AbstractCriterion;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages access to a single concrete <code>AbstractCriterion</code> subclass instance.
 */
abstract class AbstractCriterionWrapper {
    
    private final List<AbstractCriterionParameter> parameters = new ArrayList<AbstractCriterionParameter>();
    
    abstract String getFieldName();

    abstract AbstractCriterion getCriterion();

    abstract CriterionTypeEnum getCriterionType();

    List<AbstractCriterionParameter> getParameters() {
        return parameters;
    }

    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract") // empty default implementation
    void setField(AnnotationFieldDescriptor field) {
        // default is no-op, override if necessary.
    }
}
