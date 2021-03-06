/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.heatmap.CBSToHeatmap;

/**
 * Factory for retrieving the CBSToHeatmap object.
 */
public interface CBSToHeatmapFactory {
    
    /**
     * 
     * @return CBSToHeatmap object.
     */
    CBSToHeatmap getCbsToHeatmap();

}
