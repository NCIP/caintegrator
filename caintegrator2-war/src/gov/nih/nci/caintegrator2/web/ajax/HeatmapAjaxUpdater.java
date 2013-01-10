/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;

/**
 * This is an object which is turned into an AJAX javascript file using the DWR framework.  The whole purpose
 * is to update the jsp page with information based on the Heatmap status updates.  It
 * uses the reverse-ajax technology of DWR to achieve this.
 */
public class HeatmapAjaxUpdater extends AbstractViewerAjaxUpdater implements IViewerAjaxUpdater { 
    
    /**
     * {@inheritDoc}
     */
    @Override
    void run(DisplayableUserWorkspace workspace) {
        HeatmapAjaxRunner runner = new HeatmapAjaxRunner(this, workspace.getHeatmapParameters());
        new Thread(runner).start();
    }
    
    /**
     * The final update of the dynamic test, assuming a successful finish occurs.
     * @param heatmapUrl the url to forward to Heatmap.
     */
    public void finish(String heatmapUrl) {
        completeJob();
        String finalizedText = "<b> Job Finished </b> <br>" 
                               + "<a href=\"" + heatmapUrl + "\">Launch Heat Map Viewer</a>";
        getUtilThis().setValue("finalizedText", finalizedText, false);
    }

}
