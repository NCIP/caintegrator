/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SegmentBoundaryTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(SegmentBoundaryTypeEnum.INCLUSIVE, SegmentBoundaryTypeEnum.getByValue("Inclusive"));
        assertNull(SegmentBoundaryTypeEnum.getByValue(null));
    }
    
    @Test
    public void testCheckType() {
        assertFalse(SegmentBoundaryTypeEnum.checkType("no match"));
    }

}
