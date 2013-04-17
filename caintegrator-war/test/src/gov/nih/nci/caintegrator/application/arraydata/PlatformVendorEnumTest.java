/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.arraydata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the platform vendor enum.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class PlatformVendorEnumTest {

    /**
     * Tests column retrieval.
     */
    @Test
    public void getNumberColumns() {
        assertEquals(5, PlatformVendorEnum.AFFYMETRIX.getSampleMappingColumns());
        assertEquals(3, PlatformVendorEnum.AFFYMETRIX.getDnaAnalysisMappingColumns());
        assertEquals(6, PlatformVendorEnum.AGILENT.getSampleMappingColumns());
        assertEquals(6, PlatformVendorEnum.AGILENT.getDnaAnalysisMappingColumns());
    }
}
