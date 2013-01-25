/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

/**
 * Criterion type.
 */
enum CriterionTypeEnum {
    
    IDENTIFIER,
    STRING_COMPARISON,
    NUMERIC_COMPARISON,
    DATE_COMPARISON,
    SELECTED_VALUE,
    GENE_NAME,
    FOLD_CHANGE;

}
