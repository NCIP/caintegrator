/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

/**
 * Invoked when an operator in a <code>AbstractCriterionParameter</code> is changed or to access the value.
 */
interface OperatorHandler {
    
    CriterionOperatorEnum[] getAvailableOperators();
    
    CriterionOperatorEnum getOperator();

    void operatorChanged(AbstractCriterionParameter parameter, CriterionOperatorEnum operator);
    
}
