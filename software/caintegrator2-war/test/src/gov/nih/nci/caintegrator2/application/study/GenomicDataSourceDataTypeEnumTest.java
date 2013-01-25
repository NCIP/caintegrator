/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * 
 */
public class GenomicDataSourceDataTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GenomicDataSourceDataTypeEnum.EXPRESSION, GenomicDataSourceDataTypeEnum.getByValue("Expression"));
        assertEquals(GenomicDataSourceDataTypeEnum.COPY_NUMBER, GenomicDataSourceDataTypeEnum.getByValue("Copy Number"));
        assertNull(GenomicDataSourceDataTypeEnum.getByValue(null));
    }

    @Test
    public void testCheckType() {
        assertFalse(GenomicDataSourceDataTypeEnum.checkType("no match"));
        assertTrue(GenomicDataSourceDataTypeEnum.checkType(GenomicDataSourceDataTypeEnum.EXPRESSION.getValue()));
    }

}
