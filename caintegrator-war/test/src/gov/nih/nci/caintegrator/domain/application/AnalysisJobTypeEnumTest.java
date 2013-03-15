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

import org.junit.Test;

public class AnalysisJobTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(AnalysisJobTypeEnum.GENE_PATTERN, AnalysisJobTypeEnum.getByValue("Gene Pattern"));
        assertNull(AnalysisJobTypeEnum.getByValue(null));
    }

    @Test
    public void testCheckType() {
        assertFalse(AnalysisJobTypeEnum.checkType("no match"));
    }

}
