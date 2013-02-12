/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.analysis.AnalysisViewerTypeEnum;

import org.junit.Test;


/**
 * 
 */
public class AnalysisViewerTypeEnumTest {
    
    @Test
    public void testGetByFilename() {
        assertEquals(AnalysisViewerTypeEnum.HEATMAP, AnalysisViewerTypeEnum.getByValue(AnalysisViewerTypeEnum.HEATMAP.getValue()));
        assertEquals(null, AnalysisViewerTypeEnum.getByValue("fake"));
    }
}