/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.common;

import static org.junit.Assert.assertTrue;

import gov.nih.nci.caintegrator.common.NumericUtil;

import java.text.ParseException;

import org.junit.Test;

public class NumericUtilTest {

    @Test
    public void testAll() throws ParseException {
        Double value = Double.valueOf("5.1200");
        assertTrue("5.12".equalsIgnoreCase(NumericUtil.formatDisplay(value)));
        value = Double.valueOf("15.0000");
        assertTrue("15".equalsIgnoreCase(NumericUtil.formatDisplay(value)));

        assertTrue("5.12".equalsIgnoreCase(NumericUtil.formatDisplay("05.1200")));
        assertTrue("0".equalsIgnoreCase(NumericUtil.formatDisplay("0.0")));
        assertTrue("5".equalsIgnoreCase(NumericUtil.formatDisplay("05.00")));
    }
}
