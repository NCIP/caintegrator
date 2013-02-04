/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.*;

import org.junit.Test;

public class AnnotationFieldTypeTest {

    @Test
    public void testGetByValue() {
        assertEquals(AnnotationFieldType.ANNOTATION, AnnotationFieldType.getByValue("Annotation"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        AnnotationFieldType.getByValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckType() {
        AnnotationFieldType.checkType("no match");
    }

}
