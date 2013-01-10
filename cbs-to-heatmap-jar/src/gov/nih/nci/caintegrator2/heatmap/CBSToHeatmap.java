/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.heatmap;

import java.io.IOException;

/**
 * Inteface for running CBSToHeatmap algorithm.
 */
public interface CBSToHeatmap {

    void runCBSToHeatmap(HeatMapArgs hma) throws IOException;

}
