/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

import gov.nih.nci.caintegrator2.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.springframework.mock.web.MockHttpSession;

/**
 * Stub for DWR's WebContextBuilder.
 */
@SuppressWarnings("unused") // Unused methods
public class WebContextBuilderStub implements WebContextBuilder {

    public WebContext get() {
        return new WebContextStub();
    }

    public void set(HttpServletRequest arg0, HttpServletResponse arg1, ServletConfig arg2, ServletContext arg3,
            Container arg4) {
    }

    public void unset() {

    }

    private static class WebContextStub implements WebContext {

        public String forwardToString(String arg0) throws ServletException, IOException {

            return null;
        }

        public String getCurrentPage() {

            return null;
        }

        public HttpServletRequest getHttpServletRequest() {
            return null;
        }

        public HttpServletResponse getHttpServletResponse() {
            return null;
        }

        public ScriptSession getScriptSession() {
            return new ScriptSessionStub();
        }

        public HttpSession getSession() {
            MockHttpSession session = new MockHttpSession();
            DisplayableUserWorkspace workspace = (DisplayableUserWorkspace) SessionHelper.getInstance()
                    .getDisplayableUserWorkspace();
            workspace.setCurrentStudySubscriptionId(Long.valueOf(1));
            session.putValue("displayableWorkspace", workspace);
            return session;
        }

        public HttpSession getSession(boolean arg0) {

            return null;
        }

        public Collection<ScriptSession> getAllScriptSessions() {
            return null;
        }

        public Container getContainer() {
            return null;
        }

        public Collection<ScriptSession> getScriptSessionsByPage(String arg0) {
            return null;
        }

        public ServletConfig getServletConfig() {

            return null;
        }

        public ServletContext getServletContext() {
            return null;
        }

        public String getVersion() {
            return null;
        }

        private static class ScriptSessionStub implements ScriptSession {

            public void addScript(ScriptBuffer arg0) {

            }

            public Object getAttribute(String arg0) {
                return null;
            }

            public Iterator<String> getAttributeNames() {
                return null;
            }

            public long getCreationTime() {
                return 0;
            }

            public String getId() {
                return null;
            }

            public long getLastAccessedTime() {

                return 0;
            }

            public void invalidate() {

            }

            public boolean isInvalidated() {
                return false;
            }

            public void removeAttribute(String arg0) {

            }

            public void setAttribute(String arg0, Object arg1) {

            }

            public String getPage() {
                return null;
            }

        }

        public String getContextPath() {
            return null;
        }

        public ScriptSession getScriptSessionById(String arg0) {
            return null;
        }

        public void setCurrentPageInformation(String page, String scriptSessionId) {
            
        }
    }
}
