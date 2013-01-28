/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator.application.study.CentralTendencyTypeEnum;

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
