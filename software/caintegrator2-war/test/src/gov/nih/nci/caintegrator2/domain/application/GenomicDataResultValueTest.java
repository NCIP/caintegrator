/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.domain.application;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GenomicDataResultValueTest {


    @Test
    public void testGetHighlightColor() {
        GenomicDataResultValue value = new GenomicDataResultValue();
        value.setValue(1f);
        assertEquals("#CC3333", value.getHighlightColor());
        value.setValue(-1f);
        assertEquals("#0066CC", value.getHighlightColor());
    }

}
