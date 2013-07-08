/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;

import org.junit.After;
import org.junit.Test;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 *
 */
public class ValidAccessInterceptorTest extends AbstractSessionBasedTest {
    private ActionInvocation invocation;
    private ValidAccessInterceptor interceptor;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        interceptor = new ValidAccessInterceptor();
        interceptor.init();

        invocation = mock(ActionInvocation.class);
        when(invocation.invoke()).thenReturn(Action.SUCCESS);
    }

    /**
     * Tear down method.
     */
    @After
    public void tearDown() {
        interceptor.destroy();
    }

    /**
     * Tests intercepter without valid access.
     * @throws Exception on unexpected error
     */
    @Test
    public void interceptInvalid() throws Exception {
        SessionHelper.getInstance().setInvalidDataBeingAccessed(true);
        assertEquals("invalidDataBeingAccessed", interceptor.intercept(invocation));
        assertFalse(SessionHelper.getInstance().getInvalidDataBeingAccessed());
    }

    /**
     * Tests intercepter with valid access.
     * @throws Exception on unexpected error
     */
    @Test
    public void interceptValid() throws Exception {
        SessionHelper.getInstance().setInvalidDataBeingAccessed(false);
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
    }
}
