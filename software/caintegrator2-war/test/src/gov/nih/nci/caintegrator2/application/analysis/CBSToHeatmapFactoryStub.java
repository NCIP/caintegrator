/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.io.IOException;

import gov.nih.nci.caintegrator2.heatmap.CBSToHeatmap;
import gov.nih.nci.caintegrator2.heatmap.HeatMapArgs;

/**
 * 
 */
public class CBSToHeatmapFactoryStub implements CBSToHeatmapFactory {

    public boolean runCBSToHeatmapCalled = false;
    
    public CBSToHeatmap getCbsToHeatmap() {
        return new CBSToHeatmapStub();
    }
    
    private class CBSToHeatmapStub implements CBSToHeatmap {

        public void runCBSToHeatmap(HeatMapArgs hma) throws IOException {
            runCBSToHeatmapCalled = true;
        }
        
    }

}
