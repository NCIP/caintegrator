/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WildCardTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(WildCardTypeEnum.NOT_EQUAL_TO, WildCardTypeEnum.getByValue("notEqualTo"));
        assertNull(WildCardTypeEnum.getByValue(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        WildCardTypeEnum.checkType("no match");
    }

}
