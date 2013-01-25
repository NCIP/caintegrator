/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query;

import gov.nih.nci.caintegrator2.web.action.AbstractCaIntegrator2Action;

/**
 * 
 */
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
