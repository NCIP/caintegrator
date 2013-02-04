/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.query;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator.domain.application.SortTypeEnum;

import org.junit.Test;

public class SortTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(SortTypeEnum.ASCENDING, SortTypeEnum.getByValue("Ascending"));
        assertNull(SortTypeEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        SortTypeEnum.checkType("no match");
    }

}
