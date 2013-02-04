/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.security;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationServiceException;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.apache.commons.lang.StringUtils;

/**
 * Created this class to pass AppScan.
 */
public class Cai2AuthenticationProcessingFilter extends AuthenticationProcessingFilter {
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request) {
        // Based on AppScan failing because of a "Cross-Site Request Forgery"
        if (StringUtils.isBlank(request.getRequestedSessionId()) 
            || !request.getRequestedSessionId().equals(request.getSession().getId())) {
            throw new AuthenticationServiceException("The session ID is not attached with this request.");
        }
        request.getSession().invalidate();
        request.getSession(true);
        return super.attemptAuthentication(request);
    }
}
