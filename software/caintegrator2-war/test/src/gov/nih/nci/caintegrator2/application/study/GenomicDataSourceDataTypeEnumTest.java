/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.arraydata.PlatformDataTypeEnum;

import org.junit.Test;


/**
 * 
 */
public class GenomicDataSourceDataTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(PlatformDataTypeEnum.EXPRESSION, PlatformDataTypeEnum.getByValue("Expression"));
        assertEquals(PlatformDataTypeEnum.COPY_NUMBER, PlatformDataTypeEnum.getByValue("Copy Number"));
        assertEquals(PlatformDataTypeEnum.SNP, PlatformDataTypeEnum.getByValue("SNP"));
        assertNull(PlatformDataTypeEnum.getByValue(null));
    }

    @Test
    public void testCheckType() {
        assertFalse(PlatformDataTypeEnum.checkType("no match"));
        assertTrue(PlatformDataTypeEnum.checkType(PlatformDataTypeEnum.EXPRESSION.getValue()));
    }

    /**
     * Test method for {@link gov.nih.nci.caintegrator2.application.arraydata.platformTypeEnum#getValueToDisplay()}.
     */
    @Test
    public void testGetValueToDisplay() {
        assertFalse(PlatformDataTypeEnum.getStringValues().contains(PlatformDataTypeEnum.SNP.getValue()));
        assertTrue(PlatformDataTypeEnum.getStringValues().contains(PlatformDataTypeEnum.EXPRESSION.getValue()));
        assertTrue(PlatformDataTypeEnum.getStringValues().contains(PlatformDataTypeEnum.COPY_NUMBER.getValue()));
    }

}
