/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gov.nih.nci.caintegrator.application.study.GenomicDataSourceConfiguration;
import gov.nih.nci.caintegrator.application.study.HighVarianceCalculationTypeEnum;
import gov.nih.nci.caintegrator.web.DisplayableGenomicSource;

import org.junit.Test;

/**
 * 
 */
public class DisplayableGenomicSourceTest {
    
    
    @Test
    public void testGetHighVarianceThresholdString() {
        GenomicDataSourceConfiguration source = new GenomicDataSourceConfiguration();
        DisplayableGenomicSource displayableSource;
        try {
            displayableSource = new DisplayableGenomicSource(null);
            fail();
        } catch(IllegalStateException e) {
            // noop
        }
        displayableSource = new DisplayableGenomicSource(source);
        source.setUseHighVarianceCalculation(false);
        assertEquals("", displayableSource.getHighVarianceThresholdString());
        assertEquals("", displayableSource.getHighVarianceThresholdLabel());
        
        source.setUseHighVarianceCalculation(true);
        source.setHighVarianceCalculationType(HighVarianceCalculationTypeEnum.PERCENTAGE);
        assertTrue(displayableSource.getHighVarianceThresholdString().contains("%"));
        assertTrue(displayableSource.getHighVarianceThresholdLabel().contains("Relative"));
        
        source.setHighVarianceCalculationType(HighVarianceCalculationTypeEnum.VALUE);
        assertFalse(displayableSource.getHighVarianceThresholdLabel().contains("Relative"));
        
    }
}
