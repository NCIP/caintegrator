/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

import java.text.ParseException;

import org.junit.Test;


public class TimeLoggerHelperTest {

    @Test (expected = IllegalStateException.class)
    public void testStopLog() throws ParseException {
        TimeLoggerHelper logger = new TimeLoggerHelper(TimeLoggerHelperTest.class);
        logger.stopLog("Trying to stop before starting log");
    }

}
