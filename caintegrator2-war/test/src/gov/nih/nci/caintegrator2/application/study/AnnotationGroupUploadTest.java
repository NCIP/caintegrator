/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.study;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.domain.annotation.AnnotationDefinition;
import gov.nih.nci.caintegrator2.domain.application.EntityTypeEnum;

import org.junit.Before;
import org.junit.Test;

public class AnnotationGroupUploadTest {

    AnnotationGroupUploadContent uploadContent;
    
    @Before
    public void setUp() throws Exception {
        uploadContent = new AnnotationGroupUploadContent();
    } 
    @Test
    public void testSetGoodValues() {
        uploadContent.setCdeId("");
        assertNull(uploadContent.getCdeId());
        uploadContent.setVersion("");
        assertNull(uploadContent.getVersion());
        uploadContent.setAnnotationType("annotation");
        uploadContent.setCdeId("12345");
        assertEquals(Long.valueOf("12345"), uploadContent.getCdeId());
        uploadContent.setDataType("string");
        uploadContent.setVersion("1.2");
        assertEquals(Float.valueOf("1.2"), uploadContent.getVersion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnnotationType() {
        uploadContent.setAnnotationType("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCdeId() {
        uploadContent.setCdeId("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataType() {
        uploadContent.setDataType("Unknown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVersion() {
        uploadContent.setVersion("Unknown");
    }
    
    @Test
    public void testCreateAnnotationFieldDescriptor() throws ValidationException {
        AnnotationFieldDescriptor afd;
        uploadContent.setColumnName("ID");
        uploadContent.setEntityType("subject");
        uploadContent.setAnnotationType("identifier");
        uploadContent.setPermissible("no");
        uploadContent.setVisible("yes");
        afd = uploadContent.createAnnotationFieldDescriptor();
        assertEquals("ID", afd.getName());
        assertEquals(EntityTypeEnum.SUBJECT, afd.getAnnotationEntityType());
        assertEquals(AnnotationFieldType.IDENTIFIER, afd.getType());
        assertEquals("ID", afd.getName());
        assertFalse(afd.isUsePermissibleValues());
        assertTrue(afd.isShownInBrowse());
    }
    
    @Test
    public void testCreateAnnotationDefinition() throws ValidationException {
        AnnotationDefinition ad;
        uploadContent.setDefinitionName("ID");
        uploadContent.setDataType("string");
        ad = uploadContent.createAnnotationDefinition();
        assertEquals("ID", ad.getKeywords());
        assertEquals("ID", ad.getCommonDataElement().getLongName());
        assertEquals(AnnotationTypeEnum.STRING, ad.getCommonDataElement().getValueDomain().getDataType());

        // Test invalid
        uploadContent.setCdeId("123");
        boolean catchException = false;
        try {
            ad = uploadContent.createAnnotationDefinition();
        } catch (ValidationException e) {
            catchException = true;
        }
        assertTrue(catchException);
    }
    
    @Test
    public void testValidate() {
        uploadContent.setDefinitionName("ID");
        uploadContent.setDataType("string");
        AnnotationDefinition ad = new AnnotationDefinition();
        
        // Test matching local ad
        ad.setDefault("ID");
        assertTrue(uploadContent.matching(ad));
        
        // Test not matching name local ad
        uploadContent.setDefinitionName("ID_2");
        assertFalse(uploadContent.matching(ad));
        
        // Test not matching data type in local ad
        uploadContent.setDefinitionName("ID");
        uploadContent.setDataType("numeric");
        assertFalse(uploadContent.matching(ad));
        
        // Test not matching long name with local ad
        uploadContent.setDataType("string");
        ad.getCommonDataElement().setLongName("ID_2");
        assertFalse(uploadContent.matching(ad));
        
        // Test matching cdeId
        uploadContent.setCdeId("123");
        ad.getCommonDataElement().setPublicID(123L);
        ad.getCommonDataElement().setVersion("1.0");
        assertTrue(uploadContent.matching(ad));
        uploadContent.setVersion("1.0");
        assertTrue(uploadContent.matching(ad));

        //Test not matching version
        uploadContent.setVersion("1.1");
        assertFalse(uploadContent.matching(ad));
        
        //Test not matching cdeId
        uploadContent.setCdeId("124");
        uploadContent.setVersion("1.0");
        assertFalse(uploadContent.matching(ad));
    }

}
