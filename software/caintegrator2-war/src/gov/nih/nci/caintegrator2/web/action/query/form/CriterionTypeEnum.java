/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
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
    SUBJECT_LIST,
    GENE_NAME,
    FOLD_CHANGE;

}
