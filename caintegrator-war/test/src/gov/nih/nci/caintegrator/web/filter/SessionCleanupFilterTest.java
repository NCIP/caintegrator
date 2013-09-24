/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.filter;

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
 * Tests for the session cleanup filter.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class SessionCleanupFilterTest extends AbstractSessionBasedTest {

    @Test
    public void testFilter() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        SessionCleanupFilter sessionCleanupFilter = new SessionCleanupFilter();
        sessionCleanupFilter.setFileManager(analysisFileManager);
        sessionCleanupFilter.init(null);

        request.setRequestedSessionIdValid(true);
        request.setRequestURI("/login.action");
        request.setRequestedSessionId("sessionId");

        sessionCleanupFilter.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());

        request.setRequestURI("/sessionTimeout.action");
        response = new MockHttpServletResponse();
        sessionCleanupFilter.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());

        request.setRequestURI("/query.action");
        response = new MockHttpServletResponse();
        sessionCleanupFilter.doFilter(request, response, chain);
        assertNull(response.getRedirectedUrl());

        sessionCleanupFilter.destroy();
    }


}
