/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.cadsr;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator.domain.annotation.CommonDataElement;
import gov.nih.nci.caintegrator.domain.annotation.ValueDomain;
import gov.nih.nci.caintegrator.external.ConnectionException;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * caDSR integration tests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:integration-test-config.xml")
public class CaDSRFacadeImplTestIntegration {

    @Autowired
    private CaDSRFacadeImpl caDSRFacade;
    private static final Long VALID_ENUMERATED_STRING_CDE_ID = Long.valueOf(62); // ID for "Gender" in caDSR.
    private static final Long VALID_ENUMERATED_NUMERIC_CDE_ID = Long.valueOf(2183066); // ID for "Month Date" in caDSR.
    private static final Long VALID_NON_ENUMERATED_DATE_CDE_ID = Long.valueOf(2193134); // ID for "Date" in caDSR.

    @Test
    public void testRetreiveCandidateDataElements() throws ConnectionException {
        List<CommonDataElement> dataElements =
            caDSRFacade.retreiveCandidateDataElements(Arrays.asList(new String[]{"congestive", "heart", "failure"}));
        assertNotNull(dataElements);
    }

    @Test
    public void testRetrieveValueDomainForDataElement() throws ConnectionException {
        ValueDomain enumeratedStringValueDomain =
                caDSRFacade.retrieveValueDomainForDataElement(VALID_ENUMERATED_STRING_CDE_ID, null);
        assertTrue(enumeratedStringValueDomain.getPermissibleValueCollection().size() > 1);

        ValueDomain enumeratedNumericValueDomain =
                caDSRFacade.retrieveValueDomainForDataElement(VALID_ENUMERATED_NUMERIC_CDE_ID, null);
        assertTrue(enumeratedNumericValueDomain.getPermissibleValueCollection().size() > 1);

        ValueDomain nonEnumeratedDateValueDomain =
                caDSRFacade.retrieveValueDomainForDataElement(VALID_NON_ENUMERATED_DATE_CDE_ID, null);
        assertNotNull(nonEnumeratedDateValueDomain);
        assertTrue(nonEnumeratedDateValueDomain.getPermissibleValueCollection().isEmpty());

    }
}
