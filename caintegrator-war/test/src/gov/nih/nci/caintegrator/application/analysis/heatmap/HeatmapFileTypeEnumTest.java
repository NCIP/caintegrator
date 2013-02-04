/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis.heatmap;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapFileTypeEnum;

import org.junit.Test;


/**
 * 
 */
public class HeatmapFileTypeEnumTest {
    
    @Test
    public void testGetByFilename() {
        assertEquals(HeatmapFileTypeEnum.GENOMIC_DATA, HeatmapFileTypeEnum.getByFilename(HeatmapFileTypeEnum.GENOMIC_DATA.getFilename()));
        assertEquals(null, HeatmapFileTypeEnum.getByFilename("fake"));
    }
}
