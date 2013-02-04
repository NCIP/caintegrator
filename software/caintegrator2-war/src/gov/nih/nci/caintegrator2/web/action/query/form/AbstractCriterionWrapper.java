/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.caintegrator2.domain.application.AbstractCriterion;

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
}
