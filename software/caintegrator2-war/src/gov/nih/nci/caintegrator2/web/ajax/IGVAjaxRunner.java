/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.application.analysis.igv.IGVParameters;
import gov.nih.nci.caintegrator2.application.query.InvalidCriterionException;

/**
 * This is what calls the analysis service to run IGV, it is 
 * thread based, so it is asynchronous and started via the IGVAjaxUpdater.
 */
public class IGVAjaxRunner implements Runnable {

    private final IGVAjaxUpdater updater;
    private final IGVParameters igvParameters;
    
    IGVAjaxRunner(IGVAjaxUpdater updater, IGVParameters igvParameters) {
        this.updater = updater;
        this.igvParameters = igvParameters;
     }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
        try {
            updater.updateCurrentStatus("Creating IGV Files.");
            updater.finish(updater.getAnalysisService().executeIGV(igvParameters));
            
        } catch (InvalidCriterionException e) {
            updater.addErrorMessage("Invalid Criterion: " 
                    + e.getMessage());
        } catch (Exception e) {
            updater.addErrorMessage("Error: " + e.getMessage());
        }
            
    }

}
