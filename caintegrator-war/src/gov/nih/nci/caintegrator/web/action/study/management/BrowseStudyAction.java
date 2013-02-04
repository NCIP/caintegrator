/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;


import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

/**
 * Opens the currently selected study.
 */
public class BrowseStudyAction extends AbstractCaIntegrator2Action {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (getStudySubscription() != null) {
            return SUCCESS;
        } else {
            addActionError("There is no DisplayableStudySubscription object on the session");
            return ERROR;
        }
    }

}
