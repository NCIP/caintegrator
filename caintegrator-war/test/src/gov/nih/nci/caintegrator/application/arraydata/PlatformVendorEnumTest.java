/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum;

import org.junit.Test;

/**
 * 
 */
public class PlatformVendorEnumTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum#getValue()}.
     */
    @Test
    public void testGetValue() {
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum#getByValue(java.lang.String)}.
     */
    @Test
    public void testGetByValue() {
        assertEquals(PlatformVendorEnum.AFFYMETRIX, PlatformVendorEnum.getByValue(PlatformVendorEnum.AFFYMETRIX.getValue()));
        assertEquals(PlatformVendorEnum.AGILENT, PlatformVendorEnum.getByValue(PlatformVendorEnum.AGILENT.getValue()));
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum#checkType(java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        PlatformVendorEnum.checkType("not found");
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator.application.arraydata.PlatformVendorEnum#getValueToDisplay()}.
     */
    @Test
    public void testGetValueToDisplay() {
        assertTrue(PlatformVendorEnum.getValuesToDisplay().contains(PlatformVendorEnum.AFFYMETRIX.getValue()));
        assertTrue(PlatformVendorEnum.getValuesToDisplay().contains(PlatformVendorEnum.AGILENT.getValue()));
    }
    
    @Test
    public void testGetNumberColumns() {
        assertEquals(5, PlatformVendorEnum.AFFYMETRIX.getSampleMappingColumns());
        assertEquals(3, PlatformVendorEnum.AFFYMETRIX.getDnaAnalysisMappingColumns());
        assertEquals(6, PlatformVendorEnum.AGILENT.getSampleMappingColumns());
        assertEquals(6, PlatformVendorEnum.AGILENT.getDnaAnalysisMappingColumns());
    }

}