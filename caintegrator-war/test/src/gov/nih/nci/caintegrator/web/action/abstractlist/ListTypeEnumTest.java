/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.abstractlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import gov.nih.nci.caintegrator.web.action.abstractlist.ListTypeEnum;

import org.junit.Test;


/**
 * 
 */
public class ListTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(ListTypeEnum.GENE, ListTypeEnum.getByValue(ListTypeEnum.GENE.getValue()));
        assertEquals(ListTypeEnum.SUBJECT, ListTypeEnum.getByValue(ListTypeEnum.SUBJECT.getValue()));
    }

    @Test
    public void testCheckType() {
        assertFalse(ListTypeEnum.checkType("not found"));
    }
    
    @Test
    public void testGetValueToDisplayableMap() {
        assertEquals(ListTypeEnum.GENE, ListTypeEnum.getValueToTypeMap().get(ListTypeEnum.GENE.getValue()));
        assertEquals(ListTypeEnum.SUBJECT, ListTypeEnum.getValueToTypeMap().get(ListTypeEnum.SUBJECT.getValue()));
    }

}
