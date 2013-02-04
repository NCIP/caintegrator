/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.geneexpression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PlotCalculationTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(PlotCalculationTypeEnum.MEAN, PlotCalculationTypeEnum.getByValue(PlotCalculationTypeEnum.MEAN.getValue()));
    }
    
    @Test
    public void testCheckType() {
        assertFalse(PlotCalculationTypeEnum.checkType("not found"));
        assertFalse(PlotCalculationTypeEnum.checkType(null));
    }

}
