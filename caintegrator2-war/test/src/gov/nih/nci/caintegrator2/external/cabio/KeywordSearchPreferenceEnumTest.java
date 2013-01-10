/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
