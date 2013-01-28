/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.interceptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.interceptor.AuthorizationInterceptor;

import org.junit.After;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;


public class AuthorizationInterceptorTest extends AbstractSessionBasedTest {
    private ActionInvocation invocation;
    private AuthorizationInterceptor interceptor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interceptor = new AuthorizationInterceptor();
        interceptor.init();

        invocation = mock(ActionInvocation.class);
        when(invocation.invoke()).thenReturn(Action.SUCCESS);
        when(invocation.isExecuted()).thenReturn(Boolean.FALSE);
    }

    @After
    public void tearDown() {
        interceptor.destroy();
    }

    @Test
    public void intercept() throws Exception {
        // First try unauthorized.
        SessionHelper.getInstance().setAuthorizedPage(false);
        assertEquals("unauthorized", interceptor.intercept(invocation));

        // After it is unauthorized, it should automatically change the session back to authorized.
        assertEquals(true, SessionHelper.getInstance().isAuthorizedPage());

        // Now try authorized.
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
    }
}
