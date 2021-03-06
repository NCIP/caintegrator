/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.study.management;


import gov.nih.nci.caintegrator.web.DisplayableStudySummary;
import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Opens the currently selected study.
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
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
