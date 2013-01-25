/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package edu.mit.broad.genepattern.gp.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.TaskInfo;
import org.genepattern.webservice.WebServiceException;

/**
 * 
 */
public interface CaIntegrator2GPClient {
    
    /**
     * 
     * @return all tasks.
     * @throws WebServiceException if unable to connect.
     */
    TaskInfo[] getTasks() throws WebServiceException;

    /**
     * @param moduleName name of module to run.
     * @param parameters list of parameters for the module.
     * @return JobInfo of the job that is running.
     * @throws WebServiceException if unable to connect to gene pattern.
     */
    JobInfo runAnalysis(String moduleName, List<ParameterInfo> parameters) throws WebServiceException; 

    /**
     * Retrieves results file to the results directory.
     * @param jobInfo for the job.
     * @param resultsDir to download the files for the job.
     * @return file array.
     * @throws WebServiceException if unable to connect to gene pattern.
     * @throws IOException if unable to download files to directory.
     */
    File[] getResultFiles(JobInfo jobInfo, File resultsDir) throws WebServiceException, IOException;

    /**
     * @param jobInfo of the job.
     * @return new job info for the job.
     * @throws WebServiceException if unable to connect to gene pattern.
     */
    JobInfo getStatus(JobInfo jobInfo) throws WebServiceException;

    /**
     * @param jobInfo retrieve file from this job.
     * @param filename the file to retrieve.
     * @return the file from GenePattern or null.
     * @throws WebServiceException if unable to connect.
     */
    File getResultFile(JobInfo jobInfo, String filename) throws WebServiceException;
    
}
