/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * 
 */
public class PlatformTypeEnumTest {

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.PlatformTypeEnum#getValue()}.
     */
    @Test
    public void testGetValue() {
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#getByValue(java.lang.String)}.
     */
    @Test
    public void testGetByValue() {
        assertEquals(PlatformTypeEnum.AFFYMETRIX_DNA_ANALYSIS, PlatformTypeEnum.getByValue(PlatformTypeEnum.AFFYMETRIX_DNA_ANALYSIS.getValue()));
        assertEquals(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION, PlatformTypeEnum.getByValue(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue()));
        assertEquals(PlatformTypeEnum.AGILENT_GENE_EXPRESSION, PlatformTypeEnum.getByValue(PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue()));
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#checkType(java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        PlatformTypeEnum.checkType("not found");
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#getValueToDisplay()}.
     */
    @Test
    public void testGetValueToDisplay() {
        assertTrue(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AFFYMETRIX_DNA_ANALYSIS.getValue()));
        assertTrue(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue()));
        assertTrue(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue()));
    }

}
