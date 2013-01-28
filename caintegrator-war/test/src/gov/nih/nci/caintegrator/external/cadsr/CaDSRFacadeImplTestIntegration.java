/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.cadsr;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.cadsr.CaDSRFacadeImpl;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("unused")
public class CaDSRFacadeImplTestIntegration {

    private CaDSRFacadeImpl caDSRFacade;
    private static final Logger LOGGER = Logger.getLogger(CaDSRFacadeImplTestIntegration.class);
    private static final Long VALID_ENUMERATED_STRING_CDE_ID = Long.valueOf(62); // ID for "Gender" in caDSR.
    private static final Long VALID_ENUMERATED_NUMERIC_CDE_ID = Long.valueOf(2183066); // ID for "Month Date" in caDSR.
    private static final Long VALID_NON_ENUMERATED_DATE_CDE_ID = Long.valueOf(2193134); // ID for "Date" in caDSR.
    
    @Before
    public void setUp() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "cadsr-test-config.xml", CaDSRFacadeImplTestIntegration.class); 
        caDSRFacade = (CaDSRFacadeImpl) context.getBean("caDSRFacade"); 
    }

    @Test
    public void testRetreiveCandidateDataElements() throws ConnectionException {
        List<CommonDataElement> dataElements = 
            caDSRFacade.retreiveCandidateDataElements(Arrays.asList(new String[]{"congestive", "heart", "failure"}));
        assertNotNull(dataElements);        
    }
    
    @Test
    public void testRetrieveValueDomainForDataElement() throws ConnectionException {
        ValueDomain enumeratedStringValueDomain = caDSRFacade.retrieveValueDomainForDataElement(VALID_ENUMERATED_STRING_CDE_ID, null);
        assertTrue(enumeratedStringValueDomain.getPermissibleValueCollection().size() > 1);
        
        ValueDomain enumeratedNumericValueDomain = caDSRFacade.retrieveValueDomainForDataElement(VALID_ENUMERATED_NUMERIC_CDE_ID, null);
        assertTrue(enumeratedNumericValueDomain.getPermissibleValueCollection().size() > 1);
        
        ValueDomain nonEnumeratedDateValueDomain = caDSRFacade.retrieveValueDomainForDataElement(VALID_NON_ENUMERATED_DATE_CDE_ID, null);
        assertNotNull(nonEnumeratedDateValueDomain);
        assertTrue(nonEnumeratedDateValueDomain.getPermissibleValueCollection().isEmpty());
        
    }
}
