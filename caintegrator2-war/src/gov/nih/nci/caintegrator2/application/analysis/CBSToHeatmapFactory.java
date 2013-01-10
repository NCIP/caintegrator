/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.heatmap.CBSToHeatmap;

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
