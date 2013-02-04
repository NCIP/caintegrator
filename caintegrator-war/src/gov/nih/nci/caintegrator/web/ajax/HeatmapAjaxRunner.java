/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapParameters;
import gov.nih.nci.caintegrator.application.query.InvalidCriterionException;
import gov.nih.nci.caintegrator.domain.application.StudySubscription;

/**
 * This is what calls the analysis service to run IGV, it is 
 * thread based, so it is asynchronous and started via the HeatmapAjaxUpdater.
 */
public class HeatmapAjaxRunner implements Runnable {

    private final HeatmapAjaxUpdater updater;
    private final HeatmapParameters heatmapParameters;
    
    HeatmapAjaxRunner(HeatmapAjaxUpdater updater, HeatmapParameters heatmapParameters) {
        this.updater = updater;
        this.heatmapParameters = heatmapParameters;
     }
    
    /**
     * {@inheritDoc}
     */
    public void run() {
        try {
            updater.updateCurrentStatus("Creating Heat Map Files.");
            StudySubscription studySubscription = 
                updater.getAnalysisService().getRefreshedStudySubscription(heatmapParameters.getStudySubscription());
            heatmapParameters.setStudySubscription(studySubscription);
            heatmapParameters.getQuery().setSubscription(studySubscription);
            updater.finish(updater.getAnalysisService().executeHeatmap(heatmapParameters));
            
        } catch (InvalidCriterionException e) {
            updater.addErrorMessage("Invalid Criterion: " 
                    + e.getMessage());
        } catch (Exception e) {
            updater.addErrorMessage("Error: " + e.getMessage());
        }
            
    }

}
