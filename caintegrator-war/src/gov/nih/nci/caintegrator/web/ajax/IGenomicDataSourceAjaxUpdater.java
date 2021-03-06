/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.ajax;



/**
 * This interface is to allow DWR to javascript remote the methods using Spring. 
 */
public interface IGenomicDataSourceAjaxUpdater {
    
    /**
     * Initializes the web context to this JSP so the update messages stream here.
     */
    void initializeJsp();
    
    /**
     * Used to run the GenomicDataSource Job.
     * @param genomicSourceId to retrieve genomic data for (asynchronously).
     */
    void runJob(Long genomicSourceId);

}
