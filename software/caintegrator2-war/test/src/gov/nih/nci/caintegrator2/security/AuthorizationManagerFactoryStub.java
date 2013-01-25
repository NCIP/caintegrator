/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.security;

import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException;

/**
 * 
 */
public class AuthorizationManagerFactoryStub implements AuthorizationManagerFactory {

    public AuthorizationManagerStub authorizationManager = new AuthorizationManagerStub();;
    
    public AuthorizationManager getAuthorizationManager(String applicationContextName) throws CSException {
        return authorizationManager;
    }
}
