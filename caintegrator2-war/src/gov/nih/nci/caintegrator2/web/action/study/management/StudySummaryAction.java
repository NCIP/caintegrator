/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.study.management;


import gov.nih.nci.caintegrator2.web.DisplayableStudySummary;
import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

/**
 * Opens the currently selected study.
 */
public class StudySummaryAction extends AbstractCaIntegrator2Action {
    
    private static final long serialVersionUID = 1L;
    private DisplayableStudySummary studySummary;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        if (getStudySubscription() != null) {
            studySummary = getWorkspaceService().createDisplayableStudySummary(getStudySubscription().getStudy());
            return SUCCESS;
        } else {
            addActionError(getText("struts.messages.error.study.not.in.session"));
            return ERROR;
        }
    }
    
    /**
     * @return the studyInformation
     */
    public DisplayableStudySummary getStudySummary() {
        return studySummary;
    }

}
