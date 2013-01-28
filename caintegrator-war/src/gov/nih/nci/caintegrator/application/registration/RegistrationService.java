/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.application.registration;

import gov.nih.nci.security.exceptions.internal.CSInternalConfigurationException;
import gov.nih.nci.security.exceptions.internal.CSInternalInsufficientAttributesException;

import java.util.Map;

import javax.mail.MessagingException;

/**
 * 
 */
public interface RegistrationService {
    
    /**
     * Registers the user with the system.
     * @param registrationRequest from user.
     * @throws MessagingException if unable to send email.
     */
    void registerUser(RegistrationRequest registrationRequest) throws MessagingException;
    
    /**
     * Retrieves ldap context params if they exist in this application container.
     * @return ldap context params, or empty map if ldap doesn't exist.
     */
    Map<String, String> getLdapContextParams();
    
    /**
     * Authenticates a Username / Password with LDAP.
     * @param connectionProperties  for ldap configuration.
     * @param userID user's id.
     * @param password user's password.
     * @return T/F depending on if it authenticates.
     * @throws CSInternalConfigurationException CSM exception.
     * @throws CSInternalInsufficientAttributesException CSM exception.
     */
    boolean ldapAuthenticate(Map<String, String> connectionProperties, String userID, String password) 
        throws CSInternalConfigurationException, CSInternalInsufficientAttributesException;

}
