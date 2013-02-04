/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.interceptor;

import static org.junit.Assert.assertEquals;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionEventListener;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.ValueStack;


public class AuthorizationInterceptorTest extends AbstractSessionBasedTest {

    
    @Test
    public void testIntercept() throws Exception {
        super.setUp();
        AuthorizationInterceptor interceptor = new AuthorizationInterceptor();
        ActionInvocationStub invocation = new ActionInvocationStub();
        
        // First try unauthorized.
        SessionHelper.getInstance().setAuthorizedPage(false);
        assertEquals("unauthorized", interceptor.intercept(invocation));
        // After it is unauthorized, it should automatically change the session back to authorized.
        assertEquals(true, SessionHelper.getInstance().isAuthorizedPage());
        
        // Now try authorized.
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        
    }

    private static class ActionInvocationStub implements ActionInvocation {

        private static final long serialVersionUID = 1L;
        
        public void addPreResultListener(PreResultListener listener) {
            
        }

        public Object getAction() {
            return null;
        }

        public ActionContext getInvocationContext() {
            return null;
        }

        public ActionProxy getProxy() {
            return null;
        }

        public Result getResult() throws Exception {
            return null;
        }

        public String getResultCode() {
            return null;
        }

        public ValueStack getStack() {
            return null;
        }

        public String invoke() throws Exception {
            return Action.SUCCESS;
        }

        public String invokeActionOnly() throws Exception {
            return null;
        }

        public boolean isExecuted() {
            return false;
        }

        public void setActionEventListener(ActionEventListener listener) {
            
        }

        public void setResultCode(String resultCode) {

        }

        public void init(ActionProxy arg0) {
            
        }
        
    }
}
