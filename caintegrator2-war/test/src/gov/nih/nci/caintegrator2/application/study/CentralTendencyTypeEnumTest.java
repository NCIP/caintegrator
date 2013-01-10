/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.*;

import org.junit.Test;

public class CentralTendencyTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(CentralTendencyTypeEnum.MEAN, CentralTendencyTypeEnum.getByValue("Mean"));
        assertNull(CentralTendencyTypeEnum.getByValue(null));
    }

    public void testCheckType() {
        assertEquals(false, CentralTendencyTypeEnum.checkType("no match"));
    }

}
