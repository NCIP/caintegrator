/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.*;

import org.junit.Test;

public class HighVarianceCalculationTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(HighVarianceCalculationTypeEnum.PERCENTAGE, HighVarianceCalculationTypeEnum.getByValue("Relative (Percentage)"));
        assertNull(HighVarianceCalculationTypeEnum.getByValue(null));
    }

    public void testCheckType() {
        assertEquals(false, HighVarianceCalculationTypeEnum.checkType("no match"));
    }

}
