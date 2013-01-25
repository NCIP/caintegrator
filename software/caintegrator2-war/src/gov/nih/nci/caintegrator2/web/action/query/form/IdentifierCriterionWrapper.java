/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;

/**
 * Wraps access to a <code>IdentifierCriterion</code>.
 */
final class IdentifierCriterionWrapper extends StringComparisonCriterionWrapper {
    
    public static final String IDENTIFIER_FIELD_NAME = "*Unique Identifier";

    IdentifierCriterionWrapper(final IdentifierCriterion criterion, AbstractAnnotationCriterionRow row) {
        super(criterion, row);
    }

    @Override
    String getFieldName() {
        return IDENTIFIER_FIELD_NAME;
    }
    
    @Override
    void setField(AnnotationDefinition field) {
        // no-op
    }
}
