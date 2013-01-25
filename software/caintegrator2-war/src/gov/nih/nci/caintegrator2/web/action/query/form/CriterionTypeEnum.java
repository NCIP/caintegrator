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
    SUBJECT_LIST,
    GENE_NAME,
    EXPRESSION_LEVEL,
    FOLD_CHANGE,
    SEGMENTATION,
    CGHCALL;

}
