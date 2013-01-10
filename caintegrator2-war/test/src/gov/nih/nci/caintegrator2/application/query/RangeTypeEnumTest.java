/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator2.domain.application.RangeTypeEnum;

import org.junit.Test;

public class RangeTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(RangeTypeEnum.GREATER_OR_EQUAL, RangeTypeEnum.getByValue(">="));
        assertNull(GenomicAnnotationEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        RangeTypeEnum.checkType("no match");
    }

}
