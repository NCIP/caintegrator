/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.query.form;

/**
 * Handles changes to a singly-valued select field.
 * 
 * @param <E> type of object selected.
 */
interface ValueSelectedHandler<E> {
    
    void valueSelected(E value);

}
