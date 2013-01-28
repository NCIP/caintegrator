/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import gov.nih.nci.caintegrator.domain.application.GenomicIntervalTypeEnum;

import org.junit.Test;

public class GenomicIntervalTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GenomicIntervalTypeEnum.CHROMOSOME_COORDINATES, GenomicIntervalTypeEnum.getByValue("Chromosome Coordinates"));
        assertNull(GenomicIntervalTypeEnum.getByValue(null));
    }
    
    @Test
    public void testCheckType() {
        assertFalse(GenomicIntervalTypeEnum.checkType("no match"));
    }

}
