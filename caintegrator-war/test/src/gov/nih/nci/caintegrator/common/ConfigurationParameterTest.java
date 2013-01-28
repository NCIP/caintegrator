/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.common;

import static org.junit.Assert.*;
import gov.nih.nci.caintegrator.common.ConfigurationParameter;

import org.junit.Test;

public class ConfigurationParameterTest {

    @Test
    public void testGetDefaultValue() {
        assertEquals(System.getProperty("java.io.tmpdir"), ConfigurationParameter.STUDY_FILE_STORAGE_DIRECTORY.getDefaultValue());
    }

}
