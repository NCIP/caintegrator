/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.security;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.SecurityServiceProvider;
import gov.nih.nci.security.exceptions.CSException;

/**
 * Factory for retrieving AuthorizationManager objects.
 */
public class AuthorizationManagerFactoryImpl implements AuthorizationManagerFactory {

    /**
     * {@inheritDoc}
     */
    public AuthorizationManager getAuthorizationManager(String applicationContextName) throws CSException {
        return SecurityServiceProvider.getAuthorizationManager(applicationContextName);
    }

}
