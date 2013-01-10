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
public interface ISubjectDataSourceAjaxUpdater {
    
    /**
     * Initializes the web context to this JSP so the update messages stream here.
     */
    void initializeJsp();
    
    /**
     * Used to run the SubjectDataSource Job.
     * @param studyConfigurationId to load data for study.
     * @param subjectSourceId to load data for subject source (asynchronously).
     * @param jobType the job type to run.
     */
    void runJob(Long studyConfigurationId, Long subjectSourceId, SubjectDataSourceAjaxRunner.JobType jobType);

}
