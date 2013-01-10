/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import static org.junit.Assert.assertEquals;

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
