/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator.domain.application.UserWorkspace;
import gov.nih.nci.caintegrator.mockito.AbstractMockitoTest;
import gov.nih.nci.caintegrator.web.DisplayableUserWorkspace;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.acegisecurity.context.SecurityContextHolder;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory.WebContextBuilder;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockHttpSession;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;

public abstract class AbstractSessionBasedTest extends AbstractMockitoTest {

    private AcegiAuthenticationStub authentication = new AcegiAuthenticationStub();
    protected WebContextBuilder webContextBuilder;

    @Before
    public void setUp() throws Exception {
        authentication = new AcegiAuthenticationStub();
        authentication.setUsername("user");
        ConfigurationManager configurationManager = new ConfigurationManager();
        configurationManager.addContainerProvider(new XWorkConfigurationProvider());
        Configuration config = configurationManager.getConfiguration();
        Container container = config.getContainer();

        ValueStack stack = container.getInstance(ValueStackFactory.class).createValueStack();
        stack.getContext().put(ActionContext.CONTAINER, container);
        ActionContext.setContext(new ActionContext(stack.getContext()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        ActionContext.getContext().setSession(new HashMap<String, Object>());

        SessionHelper.getInstance().setStudyManager(true);

        setUpWebContextBuilder();
    }

    private void setUpWebContextBuilder() {
        ScriptSession scriptSession = mock(ScriptSession.class);
        WebContext context = mock(WebContext.class);
        when(context.getScriptSession()).thenReturn(scriptSession);
        when(context.getSession()).thenAnswer(new Answer<HttpSession>() {
            @Override
            public HttpSession answer(InvocationOnMock invocation) throws Throwable {
                MockHttpSession session = new MockHttpSession();
                DisplayableUserWorkspace workspace = SessionHelper.getInstance().getDisplayableUserWorkspace();
                workspace.setCurrentStudySubscriptionId(1L);
                session.putValue("displayableWorkspace", workspace);
                return session;
            }
        });
        webContextBuilder = mock(WebContextBuilder.class);
        when(webContextBuilder.get()).thenReturn(context);
    }

    protected void setUserAnonymous() {
        authentication.setUsername(UserWorkspace.ANONYMOUS_USER_NAME);
    }

    protected void setUsername(String username) {
        authentication.setUsername(username);
    }
}
