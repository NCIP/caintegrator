/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.study;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator.application.study.AnnotationFieldDescriptor;
import gov.nih.nci.caintegrator.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator.domain.annotation.AnnotationDefinition;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class AnnotationFieldDescriptorTest {
    @Test
    public void testGet() {
        AnnotationFieldDescriptor afd = new AnnotationFieldDescriptor();
        AnnotationDefinition ad = new AnnotationDefinition();
        afd.setDefinition(ad);
        
        // Test getHasPermissible
        assertEquals(0, afd.getPermissibleValues().size());
        ad.setDataType(AnnotationTypeEnum.STRING);
        Set<Object> genders = new HashSet<Object>();
        genders.add("Male");
        ad.addPermissibleValues(genders);
        assertEquals(1, afd.getPermissibleValues().size());
        
        // Test invalid numeric format
        ad = new AnnotationDefinition();
        afd.setDefinition(ad);
        ad.setDataType(AnnotationTypeEnum.NUMERIC);
        Set<Object> age = new HashSet<Object>();
        age.add(Double.valueOf(12));
        age.add(null);
        ad.addPermissibleValues(age);
        assertEquals(1, ad.getPermissibleValueCollection().size());
        
        // Test Display Name
        assertEquals("--Undefine--", afd.getDisplayName());
        ad.setDisplayName("Gender");
        assertEquals("Gender", afd.getDisplayName());
    }

}
