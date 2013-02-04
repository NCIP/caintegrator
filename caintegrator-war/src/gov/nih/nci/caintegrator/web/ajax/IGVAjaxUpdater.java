/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  The whole purpose
 * is to update the jsp page with information based on the IGV status updates.  It
 * uses the reverse-ajax technology of DWR to achieve this.
 */
public class IGVAjaxUpdater extends AbstractViewerAjaxUpdater implements IViewerAjaxUpdater { 

    /**
     * {@inheritDoc}
     */
    @Override
    void run(DisplayableUserWorkspace workspace) {
        IGVAjaxRunner runner = new IGVAjaxRunner(this, workspace.getIgvParameters());
        new Thread(runner).start();
    }
    
    /**
     * The final update of the dynamic test, assuming a successful finish occurs.
     * @param igvUrl the url to forward to IGV.
     */
    public void finish(String igvUrl) {
        completeJob();
        String finalizedText = "<b> Job Finished </b> <br>" 
                               + "<a href=\"" + igvUrl + "\">Launch Integrative Genomics Viewer</a>";
        getUtilThis().setValue("finalizedText", finalizedText, false);
    }

}
