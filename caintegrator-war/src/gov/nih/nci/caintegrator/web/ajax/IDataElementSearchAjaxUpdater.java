/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.ajax;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IDataElementSearchAjaxUpdater {

    /**
     * Runs an asynchronous search on both the caDSR and the local repository for matching definitions
     * based on the keywords.
     * @param type - Corresponds to an EntityTypeEnum (currently only subject or image)
     * @param studyConfigurationId - ID of the study configuration.
     * @param fileColumnId - ID of the file column.
     * @param keywords - keywords to search on.
     * @param searchResultJsp - the include search result jsp.
     * @throws ServletException exception
     * @throws IOException exception
     */
    void runSearch(String type, String studyConfigurationId, String fileColumnId, String keywords,
            String searchResultJsp) throws ServletException, IOException;
    
    
    /**
     * Initializes the web context to this JSP so the update messages stream here, also checks to make sure
     * the threads aren't timed out and if they're still running in the background will let the user know this.
     * @param searchResultJsp - the include search result jsp.
     * @throws ServletException exception
     * @throws IOException exception
     */
    void initializeJsp(String searchResultJsp) throws ServletException, IOException;

}
