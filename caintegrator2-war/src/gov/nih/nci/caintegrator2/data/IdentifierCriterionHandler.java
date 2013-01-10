/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.data;

import gov.nih.nci.caintegrator2.domain.application.IdentifierCriterion;

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
