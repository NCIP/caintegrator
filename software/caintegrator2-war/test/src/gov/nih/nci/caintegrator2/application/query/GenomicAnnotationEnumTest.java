/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GenomicAnnotationEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(GenomicAnnotationEnum.FOLD_CHANGE, GenomicAnnotationEnum.getByValue("Fold Change"));
        assertNull(GenomicAnnotationEnum.getByValue(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        GenomicAnnotationEnum.checkType("no match");
    }

}
