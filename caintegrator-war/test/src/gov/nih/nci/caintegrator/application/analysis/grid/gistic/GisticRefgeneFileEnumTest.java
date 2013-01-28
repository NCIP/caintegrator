/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.analysis.grid.gistic;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.application.analysis.grid.gistic.GisticRefgeneFileEnum;

import org.junit.Test;

public class GisticRefgeneFileEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GisticRefgeneFileEnum.HUMAN_HG16, GisticRefgeneFileEnum.getByValue("Human Hg16"));
        assertNull(GisticRefgeneFileEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        GisticRefgeneFileEnum.checkType("no match");
    }

}
