/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.domain.application.DateComparisonOperatorEnum;

import org.junit.Test;

public class DateComparisonOperatorEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(DateComparisonOperatorEnum.EQUAL, DateComparisonOperatorEnum.getByValue("=="));
        assertNull(DateComparisonOperatorEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        DateComparisonOperatorEnum.checkType("no match");
    }

}