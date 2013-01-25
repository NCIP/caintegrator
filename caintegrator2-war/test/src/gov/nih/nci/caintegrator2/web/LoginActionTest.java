/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.quartz.DataRefreshJob;
import gov.nih.nci.caintegrator2.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator2.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator2.web.action.LoginAction;

import java.util.HashMap;

import javax.servlet.ServletContext;

import org.acegisecurity.context.SecurityContextHolder;
import org.junit.Before;
import org.junit.Test;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class LoginActionTest extends AbstractMockitoTest {
    private LoginAction action;
    private ServletContext servletContext;
    private ActionContext actionContext;

    @Before
    public void setUp() {
        servletContext = mock(ServletContext.class);
        when(servletContext.getInitParameter(eq("ssoEnabled"))).thenReturn("false");

        actionContext = new ActionContext(new HashMap<String, Object>());
        ActionContext.setContext(actionContext);
        ActionContext.getContext().setSession(new HashMap<String, Object>());

        DataRefreshJob runner = new DataRefreshJob();
        runner.setCaArrayFacade(caArrayFacade);
        runner.setDao(dao);

        action = new LoginAction();
        action.setDataRefreshRunner(runner);
        action.setServletContext(servletContext);
    }

    /**
     * Tests the login method for a non SSO installation.
     */
    @Test
    public void nonSSOLogin() throws Exception {
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
        authentication.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertEquals("login", action.openLogin());

        authentication = new AcegiAuthenticationStub();
        authentication.setUsername("validUserName");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        assertEquals("login", action.openLogin());
    }

    /**
     * Tests the login method for an SSO installation.
     */
    @Test
    public void ssoLogin() throws Exception {
        when(servletContext.getInitParameter(eq("ssoEnabled"))).thenReturn("true");
        AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
        authentication.setUsername("manager");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SessionHelper.getInstance().setStudyManager(false);
        assertEquals("login", action.openLogin());

        SessionHelper.getInstance().setStudyManager(true);
        assertEquals("login", action.openLogin());
    }
}
