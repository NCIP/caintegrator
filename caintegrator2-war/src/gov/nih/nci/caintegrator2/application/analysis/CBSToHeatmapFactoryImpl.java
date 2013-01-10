/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.heatmap.CBS2HeatMap;
import gov.nih.nci.caintegrator2.heatmap.CBSToHeatmap;

/**
 * 
 */
public class CBSToHeatmapFactoryImpl implements CBSToHeatmapFactory {

    /**
     * {@inheritDoc}
     */
    public CBSToHeatmap getCbsToHeatmap() {
        return new CBS2HeatMap();
    }

}
