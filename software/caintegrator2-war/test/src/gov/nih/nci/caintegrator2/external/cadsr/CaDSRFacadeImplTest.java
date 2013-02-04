/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.study.AnnotationTypeEnum;
import gov.nih.nci.caintegrator2.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator2.domain.annotation.PermissibleValue;
import gov.nih.nci.caintegrator2.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator2.external.ConnectionException;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 */
public class CaDSRFacadeImplTest {

    CaDSRFacadeImpl caDSRFacade;

    @Before
    public void setUp() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("cadsr-test-config.xml", CaDSRFacadeImplTest.class); 
        caDSRFacade = (CaDSRFacadeImpl) context.getBean("caDSRFacadeUnit"); 
    }

    @Test
    public void testRetreiveCandidateDataElements() {
        List<CommonDataElement> dataElements = caDSRFacade.retreiveCandidateDataElements(Arrays.asList(new String[]{"congestive", "heart", "failure"}));
        CommonDataElement de = dataElements.get(0);
        assertNotNull(de);
        assertEquals("test", de.getLongName());
        assertEquals("congestive heart failure", de.getDefinition());
        assertEquals(Long.valueOf(2), de.getPublicID());
    }
    
    @Test
    public void testRetrieveValueDomainForDataElement() throws ConnectionException {
        ValueDomain valueDomain = caDSRFacade.retrieveValueDomainForDataElement(Long.valueOf(1), null);
        assertEquals(AnnotationTypeEnum.NUMERIC, valueDomain.getDataType());
        assertEquals(Long.valueOf(2),valueDomain.getPublicID());
        assertTrue(!valueDomain.getPermissibleValueCollection().isEmpty());
        PermissibleValue numericPermissibleValue = (PermissibleValue) valueDomain.getPermissibleValueCollection().iterator().next();
        assertTrue(numericPermissibleValue.getValue().equals("1.0"));
    }

}
