/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator2.file.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.genepattern.client.GPClient;
import org.genepattern.webservice.AnalysisWebServiceProxy;
import org.genepattern.webservice.JobInfo;
import org.genepattern.webservice.JobResult;
import org.genepattern.webservice.Parameter;
import org.genepattern.webservice.ParameterInfo;
import org.genepattern.webservice.TaskInfo;
import org.genepattern.webservice.WebServiceException;

/**
 * 
 */
public class CaIntegrator2GPClientImpl extends GPClient implements CaIntegrator2GPClient {

    private final AnalysisWebServiceProxy analysisProxy;
    private final FileManager fileManager;
    private static final String GP_TEMP_DIRECTORY = "genePatternFiles";

    /**
     * Constructor for GPClient.
     * @param server of GenePattern.
     * @param username for GenePattern.
     * @param password for GenePattern.
     * @param fileManager to be able to save files retrieved from GenePattern.
     * @throws WebServiceException if unable to connect.
     */
    public CaIntegrator2GPClientImpl(String server, String username, String password, FileManager fileManager) 
    throws WebServiceException {
        super(server, username, password);
        analysisProxy = new AnalysisWebServiceProxy(server, username, password);
        this.fileManager = fileManager;
    }
    
    /**
     * {@inheritDoc}
     */
    public TaskInfo[] getTasks() throws WebServiceException {
        return analysisProxy.getTasks();
        
    }

    /**
     * {@inheritDoc}
     */
    public JobInfo runAnalysis(String moduleName, List<ParameterInfo> parameters) throws WebServiceException {
        Parameter[] newParams = new Parameter[parameters.size()];
        int currParam = 0;
        for (ParameterInfo parameterInfo : parameters) {
            Parameter parameter = new Parameter(parameterInfo.getName(), parameterInfo.getValue());
            newParams[currParam] = parameter;
            currParam++;
        }
        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobNumber(runAnalysisNoWait(moduleName, newParams));
        return getStatus(jobInfo);
    }

    /**
     * {@inheritDoc}
     */
    public TaskInfo getTaskInfo(String taskId) throws WebServiceException {
        return adminProxy.getTask(taskId);

    }

    /**
     * {@inheritDoc}
     */
    public File[] getResultFiles(JobInfo jobInfo, File resultsDir) throws WebServiceException, IOException {
        JobResult jobResult = createJobResult(jobInfo.getJobNumber());
        return jobResult.downloadFiles(resultsDir.getAbsolutePath());
    }

    /**
     * {@inheritDoc}
     */
    public JobInfo getStatus(JobInfo jobInfo) throws WebServiceException {
        return analysisProxy.checkStatus(jobInfo.getJobNumber());
    
    }

    /**
     * {@inheritDoc}
     */
    public File getResultFile(JobInfo jobInfo, String filename) throws WebServiceException {
        String[] fileNameArray = {filename };
        File[] files = analysisProxy.getResultFiles(jobInfo.getJobNumber(), fileNameArray, fileManager
                .getNewTemporaryDirectory(GP_TEMP_DIRECTORY), true);
        if (files.length != 1) {
            return null;
        }
        return files[0];
    }
        
    

}
