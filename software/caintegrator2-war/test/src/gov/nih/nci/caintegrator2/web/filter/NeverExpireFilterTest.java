/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.filter;

import static org.junit.Assert.*;

import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

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
        FilterChain chain = new FilterChainImpl();
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
    
    public class FilterChainImpl implements FilterChain {

		/* (non-Javadoc)
		 * @see javax.servlet.FilterChain#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
		 */
		public void doFilter(ServletRequest arg0, ServletResponse arg1)
				throws IOException, ServletException {
			
		}
    	
    }
}


