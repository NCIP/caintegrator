/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConfigurationParameterTest {

    @Test
    public void testGetDefaultValue() {
        assertEquals(System.getProperty("java.io.tmpdir"), ConfigurationParameter.STUDY_FILE_STORAGE_DIRECTORY.getDefaultValue());
    }

}
