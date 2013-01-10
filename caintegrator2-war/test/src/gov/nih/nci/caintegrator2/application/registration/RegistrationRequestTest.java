/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.application.registration;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegistrationRequestTest {

    @Test
    public void testGetMailBody() {
        RegistrationRequest request = new RegistrationRequest();
        assertTrue(request.getMailBody().contains("No LDAP Authentication"));
        request.setLoginName("username");
        assertTrue(request.getMailBody().contains("LDAP Authentication Username: username"));
        assertFalse(request.getMailBody().contains("Requested studies to be accessed"));
        request.setRequestedStudies("studies");
        assertTrue(request.getMailBody().contains("Requested studies to be accessed: studies"));
    }

}
