/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * 
 */
public class PlatformChannelTypeEnumTest {


    @Test
    public void testGetByValue() {
        assertEquals(PlatformChannelTypeEnum.ONE_COLOR, PlatformChannelTypeEnum.getByValue(PlatformChannelTypeEnum.ONE_COLOR.getValue()));
        assertEquals(PlatformChannelTypeEnum.TWO_COLOR, PlatformChannelTypeEnum.getByValue(PlatformChannelTypeEnum.TWO_COLOR.getValue()));
        assertNull(PlatformChannelTypeEnum.getByValue(null));
    }

    @Test
    public void testCheckType() {
        boolean exception = false;
        try {
            PlatformChannelTypeEnum.checkType("no match");
        } catch (IllegalArgumentException e) {
            exception = true;
        }
        assertTrue(exception);
        PlatformChannelTypeEnum.checkType(PlatformChannelTypeEnum.ONE_COLOR.getValue());
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#getValueToDisplay()}.
     */
    @Test
    public void testGetValueToDisplay() {
        assertTrue(PlatformChannelTypeEnum.getValuesToDisplay().contains(PlatformChannelTypeEnum.ONE_COLOR.getValue()));
        assertTrue(PlatformChannelTypeEnum.getValuesToDisplay().contains(PlatformChannelTypeEnum.TWO_COLOR.getValue()));
    }
}
