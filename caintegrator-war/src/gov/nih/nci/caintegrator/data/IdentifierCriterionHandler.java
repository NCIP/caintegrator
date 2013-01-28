/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.data;

import gov.nih.nci.caintegrator.domain.application.IdentifierCriterion;

/**
 * Criterion handler for IdentifierCriterion.
 */
public class IdentifierCriterionHandler extends StringComparisonCriterionHandler {

    private static final String COLUMN_NAME = "identifier";
    
    /**
     * @param criterion - The criterion object we are going to translate.
     */
    public IdentifierCriterionHandler(IdentifierCriterion criterion) {
        super(criterion);   
        setColumnName(COLUMN_NAME);
    }
}
