/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis;

import gov.nih.nci.caintegrator.web.action.AbstractCaIntegrator2Action;

/**
 * 
 */
public class SelectAnalysisAction extends AbstractCaIntegrator2Action {
    
    private static final long serialVersionUID = 1L;
    
    private String analysisType;

    /**
     * {@inheritDoc}
     */
    public String execute() {
        return getAnalysisType();
    }

    /**
     * @return the analysisType
     */
    public String getAnalysisType() {
        return analysisType;
    }

    /**
     * @param analysisType the analysisType to set
     */
    public void setAnalysisType(String analysisType) {
        this.analysisType = analysisType;
    }
}
