/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;


/**
 *
 */
public class SessionTimeoutFilterTest extends AbstractSessionBasedTest {

    @Test
    public void testFilter() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        SessionTimeoutFilter stf = new SessionTimeoutFilter();
        stf.setFileManager(analysisFileManager);
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
}
