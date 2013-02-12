/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.domain.application;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.domain.application.ServerConnectionTypeEnum;

import org.junit.Test;

public class ServerConnectionTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(ServerConnectionTypeEnum.GRID, ServerConnectionTypeEnum.getByValue("Grid"));
        assertNull(ServerConnectionTypeEnum.getByValue(null));
    }
    
    @Test
    public void testCheckType() {
        assertFalse(ServerConnectionTypeEnum.checkType("no match"));
    }

}