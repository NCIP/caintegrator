/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


/**
 * 
 */
public class SessionTimeoutFilterTest {

    @Test
    public void testFilter() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new FilterChainImpl();
        SessionTimeoutFilter stf = new SessionTimeoutFilter();
        stf.init(null);
        request.setRequestedSessionIdValid(true);
        request.setRequestURI("/login.action");
        stf.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());

        request.setRequestURI("/sessionTimeout.action");
        response = new MockHttpServletResponse();
        stf.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());

        request.setRequestURI("/query.action");
        response = new MockHttpServletResponse();
        stf.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());
        
        // Test expired session
        request.setRequestedSessionIdValid(false);
        request.setRequestURI("/login.action");
        stf.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());

        request.setRequestURI("/sessionTimeout.action");
        response = new MockHttpServletResponse();
        stf.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());

        request.setRequestURI("/query.action");
        response = new MockHttpServletResponse();
        stf.doFilter(request, response, chain);
        assertEquals("/sessionTimeout.action", response.getRedirectedUrl());

        stf.destroy();
    }
    
    public class FilterChainImpl implements FilterChain {

        /* (non-Javadoc)
         * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
         */
        public void doFilter(ServletRequest arg0, ServletResponse arg1)
                throws IOException, ServletException {
            
        }
        
    }

}
