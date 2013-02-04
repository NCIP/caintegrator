/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.domain.application.EntityTypeEnum;

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
