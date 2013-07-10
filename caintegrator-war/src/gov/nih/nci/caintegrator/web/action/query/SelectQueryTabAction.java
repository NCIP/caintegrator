/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query;

import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class SelectQueryTabAction extends AbstractCaIntegrator2Action {
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_PAGE_SIZE = 20;
    private String selectedAction;
    private Long queryId;   // to prevent warnings in log

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() {
        return selectedAction;
    }

    /**
     * @return the selectedAction
     */
    public String getSelectedAction() {
        return selectedAction;
    }

    /**
     * @param selectedAction the selectedAction to set
     */
    public void setSelectedAction(String selectedAction) {
        if (selectedAction.contains(",")) {
            this.selectedAction = selectedAction.split(",")[0];
        } else {
            this.selectedAction = selectedAction;
        }
    }

    /**
     * @return page size
     */
    public int getPageSize() {
        if (getQueryResult() != null) {
            return getQueryResult().getPageSize();
        }
        if (getCopyNumberQueryResult() != null) {
            return getCopyNumberQueryResult().getPageSize();
        }
        return DEFAULT_PAGE_SIZE;
    }

    /**
     * @return the queryId
     */
    public Long getQueryId() {
        return queryId;
    }

    /**
     * @param queryId the queryId to set
     */
    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

}
