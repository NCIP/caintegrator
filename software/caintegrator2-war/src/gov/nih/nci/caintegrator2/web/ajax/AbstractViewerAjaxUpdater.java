/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.analysis.AnalysisService;
import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  The whole purpose
 * is to update the jsp page with information based on the viewer status updates.  It
 * uses the reverse-ajax technology of DWR to achieve this.
 */
public abstract class AbstractViewerAjaxUpdater implements IViewerAjaxUpdater { 
    private Util utilThis;
    private AnalysisService analysisService;
    
    /**
     * {@inheritDoc}
     */
    public void runViewer() {
        WebContext wctx = WebContextFactory.get();
        DisplayableUserWorkspace workspace = (DisplayableUserWorkspace) 
            wctx.getSession().getAttribute("displayableWorkspace");
        utilThis = new Util(wctx.getScriptSession());
        run(workspace);
    }
    
    abstract void run(DisplayableUserWorkspace workspace);

    /**
     * 
     * @param currentStatus sets status on JSP page.
     */
    public void updateCurrentStatus(String currentStatus) {
        utilThis.setValue("currentStatus", currentStatus);
    }
    
    /**
     * Dynamically adds an error message to the page.
     * @param message error message to add.
     */
    public void addErrorMessage(String message) {
        completeJob();
        utilThis.setValue("errorMessages", message);
    }

    /**
     * Set complete status.
     */
    protected void completeJob() {
        utilThis.setValue("overallStatusDiv", "");
    }
    
    /**
     * @return the analysisService
     */
    public AnalysisService getAnalysisService() {
        return analysisService;
    }

    /**
     * @param analysisService the analysisService to set
     */
    public void setAnalysisService(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    /**
     * @return the utilThis
     */
    public Util getUtilThis() {
        return utilThis;
    }

}
