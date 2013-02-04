/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action;


/**
 * Action used to log in a user (may not actually need this action when using ACEGI).
 */
public class LoginAction {
    
    private static final long serialVersionUID = 1L;

    private static final String LOGIN = "login";
     
    /**
     * Opens login screen.
     * @return - login.
     */
    public String openLogin() {
        return LOGIN;
    }
    
}
