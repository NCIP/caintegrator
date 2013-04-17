/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.registration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests registration requests.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class RegistrationRequestTest {

    /**
     * Tests retrieval of a registration request's mail body.
     */
    @Test
    public void getMailBody() {
        RegistrationRequest request = new RegistrationRequest();
        assertTrue(request.getMailBody().contains("No LDAP Authentication"));
        request.setLoginName("username");
        assertTrue(request.getMailBody().contains("LDAP Authentication Username: username"));
        assertFalse(request.getMailBody().contains("Requested studies to be accessed"));
        request.setRequestedStudies("studies");
        assertTrue(request.getMailBody().contains("Requested studies to be accessed: studies"));
    }
}
