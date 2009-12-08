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
