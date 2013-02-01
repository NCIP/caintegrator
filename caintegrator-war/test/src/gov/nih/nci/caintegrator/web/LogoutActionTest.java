/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package gov.nih.nci.caintegrator.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.web.action.LogoutAction;

import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class LogoutActionTest {
    private ServletContext servletContext;
    private HttpServletRequest request;
    private LogoutAction action;

    @Before
    public void setUp() {
        ActionContext actionContext = new ActionContext(new HashMap<String, Object>());
        ActionContext.setContext(actionContext);

        request = mock(HttpServletRequest.class);
        servletContext = mock(ServletContext.class);
        ServletActionContext.setServletContext(servletContext);
        ServletActionContext.setRequest(request);
        action = new LogoutAction();
    }

    /**
     * Tests logging out in the case a person isn't already logged in.
     */
    @Test
    public void logoutWithoutLoggingIn() {
        assertNull(action.getCasServerLogoutUrl());
        assertEquals(Action.SUCCESS, action.logout());
    }

    /**
     * Tests standard logout.
     */
    @Test
    public void logout() {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        assertNull(action.getCasServerLogoutUrl());
        assertEquals(Action.SUCCESS, action.logout());
    }

    /**
     * Tests logging out of an SSO enabled application.
     */
    @Test
    public void ssoLogout() {
        when(servletContext.getInitParameter("casServerLogoutUrl")).thenReturn("https://localhost:8443/cas/logout");
        when(servletContext.getInitParameter("ssoEnabled")).thenReturn("true");
        action = new LogoutAction();
        assertEquals("https://localhost:8443/cas/logout", action.getCasServerLogoutUrl());
        assertEquals("casLogout", action.logout());
    }
}
