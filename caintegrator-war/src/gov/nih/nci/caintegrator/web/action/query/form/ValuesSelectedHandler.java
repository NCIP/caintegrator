/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.query.form;

import java.util.List;

/**
 * Handles changes to a multiple-valued select field.
 * 
 * @param <E> type of object selected.
 */
interface ValuesSelectedHandler<E> {
    
    void valuesSelected(List<E> values);

}