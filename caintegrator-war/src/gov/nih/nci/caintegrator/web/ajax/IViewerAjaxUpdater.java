/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IViewerAjaxUpdater {
    
    /**
     * Retrieves the viewer parameters from the session and creates a new ViewerAjaxRunner thread and starts it.
     * This function is called directly from the igvAjax_tile.jsp page using DWR's reverse ajax mechanism.
     */
    void runViewer();

}
