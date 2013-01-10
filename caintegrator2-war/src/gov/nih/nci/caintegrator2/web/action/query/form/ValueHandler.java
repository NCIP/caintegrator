/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import com.opensymphony.xwork2.ValidationAware;

/**
 * Handles changes and validation for a singly-valued field.
 */
interface ValueHandler {
    
    void valueChanged(String value);

    boolean isValid(String value);

    void validate(String formFieldName, String value, ValidationAware action);

}
