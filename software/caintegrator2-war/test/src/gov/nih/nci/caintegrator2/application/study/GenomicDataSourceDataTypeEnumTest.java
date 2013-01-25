/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * 
 */
public class GenomicDataSourceDataTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GenomicDataSourceDataTypeEnum.EXPRESSION, GenomicDataSourceDataTypeEnum.getByValue("Expression"));
        assertEquals(GenomicDataSourceDataTypeEnum.COPY_NUMBER, GenomicDataSourceDataTypeEnum.getByValue("Copy Number"));
        assertEquals(GenomicDataSourceDataTypeEnum.SNP, GenomicDataSourceDataTypeEnum.getByValue("SNP"));
        assertNull(GenomicDataSourceDataTypeEnum.getByValue(null));
    }

    @Test
    public void testCheckType() {
        assertFalse(GenomicDataSourceDataTypeEnum.checkType("no match"));
        assertTrue(GenomicDataSourceDataTypeEnum.checkType(GenomicDataSourceDataTypeEnum.EXPRESSION.getValue()));
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#getValueToDisplay()}.
     */
    @Test
    public void testGetValueToDisplay() {
        assertFalse(GenomicDataSourceDataTypeEnum.getStringValues().contains(GenomicDataSourceDataTypeEnum.SNP.getValue()));
        assertTrue(GenomicDataSourceDataTypeEnum.getStringValues().contains(GenomicDataSourceDataTypeEnum.EXPRESSION.getValue()));
        assertTrue(GenomicDataSourceDataTypeEnum.getStringValues().contains(GenomicDataSourceDataTypeEnum.COPY_NUMBER.getValue()));
    }

}
