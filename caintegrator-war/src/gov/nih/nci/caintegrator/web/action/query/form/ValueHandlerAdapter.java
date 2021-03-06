/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Provides empty implementations of methods from <code>ValueHandler</code>, allowing
 * subclasses to override selected functionality.
 */
class ValueHandlerAdapter implements ValueHandler {

    /**
     * {@inheritDoc}
     */
    public boolean isValid(String value) {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public void validate(String formFieldName, String value, ValidationAware action) {
        // no-op
    }

    /**
     * {@inheritDoc}
     */
    public void valueChanged(String value) {
        // no-op
    }

}
