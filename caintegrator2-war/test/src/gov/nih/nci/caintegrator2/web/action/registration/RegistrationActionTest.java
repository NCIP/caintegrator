/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.registration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import gov.nih.nci.caintegrator2.application.registration.RegistrationRequest;
import gov.nih.nci.caintegrator2.application.registration.RegistrationService;
import gov.nih.nci.caintegrator2.mockito.AbstractSecurityEnabledMockitoTest;
import gov.nih.nci.security.exceptions.internal.CSInternalConfigurationException;
import gov.nih.nci.security.exceptions.internal.CSInternalInsufficientAttributesException;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;

public class RegistrationActionTest extends AbstractSecurityEnabledMockitoTest {
    private RegistrationAction action;
    private RegistrationServiceStub registrationService;

    @Before
    public void setUp() throws Exception {
        action = new RegistrationAction();
        registrationService = new RegistrationServiceStub();
        action.setRegistrationService(registrationService);
        action.setSecurityManager(secManager);
    }

    @Test
    public void testValidate() {
        registrationService.validLdapInstance = true;
        action.setLdapAuthenticate(true);
        action.prepare();
        action.validate();
        assertFalse(action.hasActionErrors());
        // Create a user that already exists.
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setLoginName("userExists");
        action.setRegistrationRequest(registrationRequest);
        action.validate();
        verify(secManager, times(1)).doesUserExist(anyString());
        assertTrue(action.hasFieldErrors());

        // Create a user that doesn't exist. but given no password
        action.clearErrorsAndMessages();
        registrationRequest.setLoginName("unused");
        action.validate();
        assertTrue(action.hasFieldErrors());

        // Create one with valid user and password, but ldap will fail.
        registrationService.passLdapAuthentication = false;
        action.clearErrorsAndMessages();
        action.setPassword("password");
        action.validate();
        assertTrue(action.hasActionErrors());

        // Now it's completely valid.
        registrationService.passLdapAuthentication = true;
        action.clearErrorsAndMessages();
        action.setPassword("password");
        action.validate();
        assertFalse(action.hasFieldErrors());


    }

    @Test
    public void testSave() {
        action.clearErrorsAndMessages();
        action.setConfigurationHelper(configurationHelper);
        registrationService.throwMessagingException = true;
        action.save();
        assertTrue(action.hasActionErrors());

        action.clearErrorsAndMessages();
        registrationService.throwMessagingException = false;
        action.save();
        assertFalse(action.hasActionErrors());
    }

    @SuppressWarnings("unused")
    private static class RegistrationServiceStub implements RegistrationService {
        public boolean registerUserCalled = false;
        public boolean throwMessagingException = false;
        public boolean ldapAuthenticateCalled = false;
        public boolean passLdapAuthentication = false;
        public boolean validLdapInstance = false;

        public void clear() {
            registerUserCalled = false;
            throwMessagingException = false;
            ldapAuthenticateCalled = false;
            passLdapAuthentication = false;
            validLdapInstance = false;
        }

        public void registerUser(RegistrationRequest registrationRequest) throws MessagingException {
            registerUserCalled = true;
            if (throwMessagingException) {
                throw new MessagingException("Test Error");
            }

        }

        public boolean ldapAuthenticate(Map<String, String> connectionProperties, String userID, String password)
                throws CSInternalConfigurationException, CSInternalInsufficientAttributesException {
            ldapAuthenticateCalled = true;
            return passLdapAuthentication;
        }

        public Map<String, String> getLdapContextParams() {
            Map<String, String> ldapContextParams = new HashMap<String, String>();
            if (validLdapInstance) {
                ldapContextParams.put("ldap", "valid");
            }
            return ldapContextParams;
        }

    }


}
