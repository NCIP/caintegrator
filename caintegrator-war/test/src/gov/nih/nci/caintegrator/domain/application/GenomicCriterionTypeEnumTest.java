/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GenomicCriterionTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GenomicCriterionTypeEnum.COPY_NUMBER, GenomicCriterionTypeEnum.getByValue("copyNumber"));
        assertNull(GenomicCriterionTypeEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        GenomicCriterionTypeEnum.checkType("no match");
    }

}
