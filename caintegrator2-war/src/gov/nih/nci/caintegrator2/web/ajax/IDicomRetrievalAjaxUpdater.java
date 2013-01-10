/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.ajax;

/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IDicomRetrievalAjaxUpdater {
    
    /**
     * Retrieves the DicomJob from the session and creates a new DicomRetrievalAjaxRunner thread and starts it.
     * This function is called directly from the dicomJobAjax_tile.jsp page using DWR's reverse ajax mechanism.
     */
    void runDicomJob();

}
