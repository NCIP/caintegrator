/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;

import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

/**
 * Superclass for all "Study Management" type actions.  The main purpose is to remove study subscriptions
 * from the session so that it isn't out of context.
 */
public abstract class AbstractCai2ManagementAction extends AbstractCaIntegrator2Action {
    
    /**
     * Default serialize.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")     // PMD mistakenly flagging as empty method
    protected boolean isManagementAction() {
        return true;
    }

}
