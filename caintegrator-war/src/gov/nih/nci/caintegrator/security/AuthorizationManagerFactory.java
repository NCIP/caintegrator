/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.security;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException;

/**
 * Factory for retrieving AuthorizationManager objects.
 */
public interface AuthorizationManagerFactory {

    /**
     * Retrieves AuthorizationManager from SecurityContext.
     * @param applicationContextName name of the application.
     * @return AuthorizationManager from context.
     * @throws CSException if unable to retrieve AuthorizationManager.
     */
    AuthorizationManager getAuthorizationManager(String applicationContextName) throws CSException;
}
