/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.interceptor;

import gov.nih.nci.caintegrator2.web.SessionHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * This interceptor will be fired after the "prepare" method, if an object trying to be accessed
 * from the prepare method isn't authorized, it will set a flag to let this know that it is unauthorized
 * and this interceptor will forward it to the unauthorized page display.
 */
public class AuthorizationInterceptor implements Interceptor {
    
    private static final long serialVersionUID = 1L;
    
    private static final String UNAUTHORIZED_PAGE = "unauthorized";
    
    /**
     * {@inheritDoc}
     */
    public void destroy() {
        //NOOP
    }

    /**
     * {@inheritDoc}
     */
    public void init() {
        // NOOP
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("PMD") // It doesn't like the "throws Exception".
    public String intercept(ActionInvocation invocation) throws Exception {
        if (!SessionHelper.getInstance().isAuthorizedPage()) {
            SessionHelper.getInstance().setAuthorizedPage(true);
            return UNAUTHORIZED_PAGE;
        }
        return invocation.invoke();
    }

}
