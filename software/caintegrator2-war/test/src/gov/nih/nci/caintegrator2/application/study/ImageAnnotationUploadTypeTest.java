/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * 
 */
public class ImageAnnotationUploadTypeTest {

    @Test
    public void testGetByValue() {
        assertEquals(ImageAnnotationUploadType.FILE, ImageAnnotationUploadType.getByValue("Upload Annotation File"));
        assertNull(ImageAnnotationUploadType.getByValue(null));
        assertEquals(2, ImageAnnotationUploadType.getStringValues().size());
    }

    @Test
    public void testCheckType() {
        assertFalse(ImageContentTypeEnum.checkType("no match"));
        assertTrue(ImageAnnotationUploadType.checkType(ImageAnnotationUploadType.AIM.getValue()));
    }

}
