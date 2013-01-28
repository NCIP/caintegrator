/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import gov.nih.nci.caintegrator.application.study.ImageContentTypeEnum;

import org.junit.Test;


/**
 * 
 */
public class ImageContentTypeEnumTest {

    @Test
    public void testGetByValue() {
        assertEquals(ImageContentTypeEnum.JPEG, ImageContentTypeEnum.getByValue("image/jpeg"));
        assertNull(ImageContentTypeEnum.getByValue(null));
    }

    @Test
    public void testCheckType() {
        assertFalse(ImageContentTypeEnum.checkType("no match"));
        assertTrue(ImageContentTypeEnum.checkType(ImageContentTypeEnum.JPEG.getValue()));
    }

}
