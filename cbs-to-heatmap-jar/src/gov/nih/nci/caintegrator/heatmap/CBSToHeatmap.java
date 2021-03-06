/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.heatmap;

import java.io.IOException;

/**
 * Inteface for running CBSToHeatmap algorithm.
 */
public interface CBSToHeatmap {

    void runCBSToHeatmap(HeatMapArgs hma) throws IOException;

}
