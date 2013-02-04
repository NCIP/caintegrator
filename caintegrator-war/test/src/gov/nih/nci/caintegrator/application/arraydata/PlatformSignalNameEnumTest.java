/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.arraydata.PlatformDataTypeEnum;
import gov.nih.nci.caintegrator.application.arraydata.PlatformSignalNameEnum;

import org.junit.Test;


/**
 * 
 */
public class PlatformSignalNameEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(PlatformSignalNameEnum.AFFYMETRIX_SIGNAL_NAME, PlatformSignalNameEnum.getByValue("CHPSignal"));
        assertEquals(PlatformSignalNameEnum.TWO_COLOR_SIGNAL_NAME, PlatformSignalNameEnum.getByValue("gProcessedSignal"));
        assertEquals(PlatformSignalNameEnum.TWO_COLOR_SIGNAL_NAME, PlatformSignalNameEnum.getByValue("gProcessedSignal"));
        assertNull(PlatformDataTypeEnum.getByValue(null));
    }   

    @Test
    public void testCheckType() throws IllegalArgumentException {
        PlatformSignalNameEnum.checkType(PlatformSignalNameEnum.AFFYMETRIX_SIGNAL_NAME.getValue());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCheckTypeWithNoMatch() throws IllegalArgumentException {
        PlatformSignalNameEnum.checkType("no match");
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testCheckTypeWithNull() throws IllegalArgumentException {
        PlatformSignalNameEnum.checkType(null);
    }     

    /**
     * Test method for {@link gov.nih.nci.caintegrator.application.arraydata.platformSignalNameEnum#getValuesToDisplay()}.
     */
    @Test
    public void testGetValueToDisplay() {
        assertTrue(PlatformSignalNameEnum.getValuesToDisplay().contains(PlatformSignalNameEnum.AFFYMETRIX_SIGNAL_NAME.getValue()));
        assertTrue(PlatformSignalNameEnum.getValuesToDisplay().contains(PlatformSignalNameEnum.ONE_COLOR_SIGNAL_NAME.getValue()));
        assertTrue(PlatformSignalNameEnum.getValuesToDisplay().contains(PlatformSignalNameEnum.TWO_COLOR_SIGNAL_NAME.getValue()));
    }

}
