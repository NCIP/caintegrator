/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

import java.util.List;

/**
 * Handles changes to a multiple-valued select field.
 * 
 * @param <E> type of object selected.
 */
interface ValuesSelectedHandler<E> {
    
    void valuesSelected(List<E> values);

}
