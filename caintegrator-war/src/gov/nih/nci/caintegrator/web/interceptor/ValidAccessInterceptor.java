/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.interceptor;

import gov.nih.nci.caintegrator.web.SessionHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * This interceptor will be fired after the "prepare" or "validate" method, if study data is trying to be accessed
 *  on a study that is null, invalid or not authorized for the user's session it will redirect the user back to
 *  the homepage.
 */
public class ValidAccessInterceptor implements Interceptor {

    private static final long serialVersionUID = 1L;

    private static final String INVALID_DATA_BEING_ACCESSED = "invalidDataBeingAccessed";

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        //NOOP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // NOOP
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public String intercept(ActionInvocation invocation) throws Exception {
        if (SessionHelper.getInstance().getInvalidDataBeingAccessed()) {
            SessionHelper.getInstance().setInvalidDataBeingAccessed(false);
            return INVALID_DATA_BEING_ACCESSED;
        }
        return invocation.invoke();
    }

}
