/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnnotationTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(AnnotationTypeEnum.DATE, AnnotationTypeEnum.getByValue("date"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        AnnotationTypeEnum.getByValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        AnnotationTypeEnum.checkType("no match");
    }

}
