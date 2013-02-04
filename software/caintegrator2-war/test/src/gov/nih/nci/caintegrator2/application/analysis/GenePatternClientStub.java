/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import java.util.List;

import edu.mit.broad.genepattern.gp.services.FileWrapper;
import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.mit.broad.genepattern.gp.services.GenePatternServiceException;
import edu.mit.broad.genepattern.gp.services.JobInfo;
import edu.mit.broad.genepattern.gp.services.ParameterInfo;
import edu.mit.broad.genepattern.gp.services.TaskInfo;

/**
 * 
 */
public class GenePatternClientStub implements GenePatternClient {

    /**
     * {@inheritDoc}
     */
    public TaskInfo getTaskInfo(String name) throws GenePatternServiceException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public TaskInfo[] getTasks() throws GenePatternServiceException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public JobInfo runAnalysis(String taskName, List<ParameterInfo> parameters) throws GenePatternServiceException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setUrl(String url) {
        // no-op
    }

    /**
     * {@inheritDoc}
     */
    public void setUsername(String username) {
        // no-op
    }

    /**
     * {@inheritDoc}
     */
    public void setPassword(String password) {
        // no-op
    }

    /**
     * {@inheritDoc}
     */
    public JobInfo getStatus(JobInfo jobInfo) {
        return jobInfo;
    }

    /**
     * {@inheritDoc}
     */
    public FileWrapper[] getResultFiles(JobInfo jobInfo) {
        return new FileWrapper[0];
    }

    /**
     * {@inheritDoc}
     */
    public FileWrapper getResultFile(JobInfo jobInfo, String filename) {
        return new FileWrapper();
    }

}
