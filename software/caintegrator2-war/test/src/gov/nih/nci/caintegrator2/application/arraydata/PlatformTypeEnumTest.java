/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        assertEquals(PlatformTypeEnum.AFFYMETRIX_SNP, PlatformTypeEnum.getByValue(PlatformTypeEnum.AFFYMETRIX_SNP.getValue()));
        assertEquals(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION, PlatformTypeEnum.getByValue(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue()));
        assertEquals(PlatformTypeEnum.AGILENT_GENE_EXPRESSION, PlatformTypeEnum.getByValue(PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue()));
        assertEquals(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION, PlatformTypeEnum.getByValue(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue()));
        assertEquals(PlatformTypeEnum.AGILENT_GENE_EXPRESSION, PlatformTypeEnum.getByValue(PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue()));
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#checkType(java.lang.String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        PlatformTypeEnum.checkType("not found");
        assertTrue(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.isGeneExpression());
        assertTrue(PlatformTypeEnum.AGILENT_COPY_NUMBER.isCopyNumber());
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#getValueToDisplay()}.
     */
    @Test
    public void testGetValueToDisplay() {
        assertFalse(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AFFYMETRIX_SNP.getValue()));
        assertTrue(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AFFYMETRIX_GENE_EXPRESSION.getValue()));
        assertTrue(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AGILENT_GENE_EXPRESSION.getValue()));
        assertTrue(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AFFYMETRIX_COPY_NUMBER.getValue()));
        assertTrue(PlatformTypeEnum.getValuesToDisplay().contains(PlatformTypeEnum.AGILENT_COPY_NUMBER.getValue()));
    }

}
