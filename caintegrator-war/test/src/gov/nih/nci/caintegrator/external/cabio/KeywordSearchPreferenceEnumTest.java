/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.cabio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.external.cabio.KeywordSearchPreferenceEnum;

import org.junit.Test;

/**
 * 
 */
public class KeywordSearchPreferenceEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(KeywordSearchPreferenceEnum.ALL, KeywordSearchPreferenceEnum.getByValue("All"));
        assertNull(KeywordSearchPreferenceEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        KeywordSearchPreferenceEnum.checkType("no match");
    }

}
