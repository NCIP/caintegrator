/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import org.junit.Test;

public class EntityTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(EntityTypeEnum.IMAGE, EntityTypeEnum.getByValue("image"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        EntityTypeEnum.getByValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        EntityTypeEnum.checkType("no match");
    }

}
