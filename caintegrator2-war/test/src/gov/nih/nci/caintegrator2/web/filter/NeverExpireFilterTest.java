/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.filter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Tests for web user filter.
 */
public class NeverExpireFilterTest extends AbstractSessionBasedTest {

    @Test
    public void testFilter() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        NeverExpireFilter ccf = new NeverExpireFilter();
        ccf.init(null);
        ccf.doFilter(request, response, chain);
        assertNotNull(response.getHeader("Expires"));

        request.setRequestURI("/index.action");
        response = new MockHttpServletResponse();
        ccf.doFilter(request, response, chain);
        assertNotNull(response.getHeader("Expires"));
        assertNull(response.getHeader("Cache-control"));
        assertNull(response.getHeader("Pragma"));

        ccf.destroy();
    }
}


