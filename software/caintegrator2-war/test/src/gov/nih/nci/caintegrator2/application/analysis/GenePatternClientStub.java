/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.TaskInfo;
import org.genepattern.webservice.WebServiceException;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;

/**
 * 
 */
public class GenePatternClientStub implements CaIntegrator2GPClient {

    public TaskInfo getTaskInfo(String name) throws WebServiceException {
        return null;
    }

    public TaskInfo[] getTasks() throws WebServiceException {
        return null;
    }


    public JobInfo runAnalysis(String taskName, List<ParameterInfo> parameters) throws WebServiceException {
        return null;
    }


    public void setUrl(String url) {
        // no-op
    }


    public void setUsername(String username) {
        // no-op
    }


    public void setPassword(String password) {
        // no-op
    }


    public JobInfo getStatus(JobInfo jobInfo) {
        return jobInfo;
    }


    public File getResultFile(JobInfo jobInfo, String filename) {
        return null;
    }

    public File[] getResultFiles(JobInfo jobInfo, File resultsDir) throws WebServiceException, IOException {
        return null;
    }

}
