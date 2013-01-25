/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package edu.mit.broad.genepattern.gp.services;

import java.util.List;

/**
 * Interface to GenePattern web service.
 */
public interface GenePatternClient extends CaIntegrator2GPClient {
    
    /**
     * Sets the URL to use to access the service.
     * 
     * @param url the URL
     */
    void setUrl(String url);

    /**
     * Sets the username to use to access the service.
     * 
     * @param username the username.
     */
    void setUsername(String username);
    
    /**
     * Sets the password to use to access the service.
     * 
     * @param password the password.
     */
    void setPassword(String password);
    
    /**
     * Returns information for all available tasks
     * 
     * @return information on all tasks.
     * @throws GenePatternServiceException if there's a problem communicating with the service.
     */
    TaskInfo[] getTasksOld() throws GenePatternServiceException;

    /**
     * Returns the task information for the requested task.
     * 
     * @param name name of the task to retrieve.
     * @return the information.
     * @throws GenePatternServiceException if there's a problem communicating with the service.
     */
    TaskInfo getTaskInfo(String name) throws GenePatternServiceException;
    
    /**
     * Runs the given job.
     * 
     * @param taskName the name of the analysis method
     * @param parameters the parameters to submit
     * @return the job information
     * @throws GenePatternServiceException if there's a failure communicating with GenePattern
     */
    JobInfo runAnalysisCai2(String taskName, List<ParameterInfo> parameters) throws GenePatternServiceException;

    /**
     * Returns the current updated jobInfo for the given task.
     * 
     * @param jobInfo the current job info.
     * @return the updated job info.
     * @throws GenePatternServiceException if there's a failure communicating with GenePattern.
     */
    JobInfo getStatus(JobInfo jobInfo) throws GenePatternServiceException;

    /**
     * Returns the result files for the job.
     * 
     * @param jobInfo get files for this job.
     * @return the file wrappers.
     * @throws GenePatternServiceException if there's a failure communicating with GenePattern.
     */
    FileWrapper[] getResultFiles(JobInfo jobInfo) throws GenePatternServiceException;

    /**
     * Returns the file wrapper for the requested file.
     * 
     * @param jobInfo retrieve file from this job.
     * @param filename the file to retrieve.
     * @return the requested file or null.
     * @throws GenePatternServiceException if there's a failure communicating with GenePattern.
     */
    FileWrapper getResultFile(JobInfo jobInfo, String filename) throws GenePatternServiceException;

}
