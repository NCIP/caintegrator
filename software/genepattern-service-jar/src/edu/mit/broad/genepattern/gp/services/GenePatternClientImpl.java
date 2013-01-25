/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package edu.mit.broad.genepattern.gp.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.rpc.ServiceException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Wrapper around the GenePattern web service.
 */
public class GenePatternClientImpl implements GenePatternClient {
    
    private static final String CLIENT_FILENAME_ATTRIBUTE = "client_filename";
    private static final String MODE_ATTRIBUTE = "MODE";
    private static final String TYPE_ATTRIBUTE = "TYPE";
    private static final String INPUT_MODE = "IN";
    private static final String URL_INPUT_MODE = "URL_IN";
    private static final String FILE_TYPE = "FILE";
    private Analysis analysis;
    private Map<String, TaskInfo> taskMap;
    private String url;
    private String username;
    private String password;

    private Analysis getAnalysis() throws GenePatternServiceException {
        if (analysis == null) {
            analysis = openAnalysisSerivce();
        }
        return analysis;
    }

    private void closeAnalysisService() {
        analysis = null;
        taskMap = null;
    }

    private Analysis openAnalysisSerivce() throws GenePatternServiceException {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(username)) {
            throw new GenePatternServiceException(
                    new Exception("Must set URL and username prior to using GenePattern service."));
        }
        try {
            AnalysisService service = new AnalysisServiceLocator();
            URL address = new URL(url);
            Analysis analysis = service.getAnalysis(address);
            ((AnalysisSoapBindingStub) analysis).setUsername(username);
            ((AnalysisSoapBindingStub) analysis).setPassword(password);
            ((AnalysisSoapBindingStub) analysis).setMaintainSession(true);
            return analysis;
        } catch (ServiceException e) {
            throw new GenePatternServiceException(e);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Bad URL " + url, e);
        }
    }
    
    /**
     * Returns the task information for the requested task.
     * 
     * @param name name of the task to retrieve.
     * @return the information.
     * @throws GenePatternServiceException if there's a problem communicating with the service.
     */
    public TaskInfo getTaskInfo(String name) throws GenePatternServiceException {
        return getTaskMap().get(name);
    }
    
    private Map<String, TaskInfo> getTaskMap() throws GenePatternServiceException {
        if (taskMap == null) {
            loadTaskMap();
        }
        return taskMap;
    }

    private void loadTaskMap() throws GenePatternServiceException {
        taskMap = new HashMap<String, TaskInfo>();
        for (TaskInfo taskInfo : getTasksOld()) {
            taskMap.put(taskInfo.getName(), taskInfo);
        }
    }
    
    /**
     * Returns information for all available tasks
     * 
     * @return information on all tasks.
     * @throws GenePatternServiceException if there's a problem communicating with the service.
     */
    public TaskInfo[] getTasksOld() throws GenePatternServiceException {
        try {
            return getAnalysis().getTasks();
        } catch (WebServiceException e) {
            throw new GenePatternServiceException(e);
        } catch (RemoteException e) {
            throw new GenePatternServiceException(e);
        }
    }
    
    /**
     * Runs the given job.
     * 
     * @param taskName the name of the analysis method
     * @param parameters the parameters to submit
     * @return the job information
     * @throws GenePatternServiceException if there's a failure communicating with GenePattern
     */
    public JobInfo runAnalysisCai2(String taskName, List<ParameterInfo> parameters) throws GenePatternServiceException {
        TaskInfo taskInfo = getTaskInfo(taskName);
        ParameterInfo[] fullJobParameters = getFullJobParameters(parameters, taskInfo);
        HashMap<String, DataHandler> fileDataHandlers = getFileDataHandlers(fullJobParameters);
        try {
            return getAnalysis().submitJob(taskInfo.getID(), fullJobParameters, fileDataHandlers);
        } catch (WebServiceException e) {
            throw new GenePatternServiceException(e);
        } catch (RemoteException e) {
            throw new GenePatternServiceException(e);
        }
    }

    private HashMap<String, DataHandler> getFileDataHandlers(ParameterInfo[] parameters) {
        HashMap<String, DataHandler> handlers = new HashMap<String, DataHandler>();
        for (ParameterInfo parameter : parameters) {
            if (isFileParameter(parameter)) {
                DataHandler handler = getFileDataHandler(parameter);
                handlers.put(handler.getName(), handler);
            }
        }
        return handlers;
    }

    @SuppressWarnings("unchecked")  // parameter attributes is untyped
    private boolean isFileParameter(ParameterInfo parameter) {
        Map attributes = parameter.getAttributes();
        return attributes != null
            && FILE_TYPE.equals(attributes.get(TYPE_ATTRIBUTE))
            && INPUT_MODE.equals(attributes.get(MODE_ATTRIBUTE));
    }

    private DataHandler getFileDataHandler(ParameterInfo parameter) {
        String filePath = parameter.getValue();
        FileDataSource fileDataSource = new FileDataSource(filePath) {
            @Override
            public String getName() {
                return getFile().getName();
            }
        };
        DataHandler handler = new DataHandler(fileDataSource);
        parameter.setValue(fileDataSource.getName());
        return handler;
    }

    private ParameterInfo[] getFullJobParameters(List<ParameterInfo> parameters, TaskInfo taskInfo) {
        List<ParameterInfo> fullParameters = new ArrayList<ParameterInfo>();
        Map<String, ParameterInfo> templateParameterMap = getTemplateParameterMap(taskInfo);
        addUserSuppliedParameters(fullParameters, parameters, templateParameterMap);
        addMissingParameters(fullParameters, templateParameterMap);
        return fullParameters.toArray(new ParameterInfo[fullParameters.size()]);
    }

    private void addUserSuppliedParameters(List<ParameterInfo> fullParameters, List<ParameterInfo> userParameters,
            Map<String, ParameterInfo> templateParameterMap) {
        for (ParameterInfo userParameter : userParameters) {
            ParameterInfo parameterTemplate = templateParameterMap.remove(userParameter.getName());
            ParameterInfo newParameter = new ParameterInfo();
            newParameter.setName(userParameter.getName());
            newParameter.setValue(getValue(userParameter, parameterTemplate));
            if (parameterTemplate.isInputFile()) {
                handleInputFileParameter(newParameter, parameterTemplate);
            }
            fullParameters.add(newParameter);
        }
    }

    private void handleInputFileParameter(ParameterInfo parameter, ParameterInfo parameterTemplate) {
        HashMap<String, String> attributes = new HashMap<String, String>();
        parameter.setAttributes(attributes);
        attributes.put(CLIENT_FILENAME_ATTRIBUTE, parameter.getValue());
        parameter.setInputFile(true);
        if (parameter.getValue() != null && new File(parameter.getValue()).exists()) {
            attributes.put(TYPE_ATTRIBUTE, FILE_TYPE);
            attributes.put(MODE_ATTRIBUTE, INPUT_MODE);
        } else if (parameter.getValue() != null) {
            attributes.remove(TYPE_ATTRIBUTE);
            attributes.put(MODE_ATTRIBUTE, URL_INPUT_MODE);
        }
    }

    private String getValue(ParameterInfo userParameter, ParameterInfo taskParameter) {
        if (userParameter.getValue() != null) {
            return userParameter.getValue();
        } else
            return getDefaultValue(taskParameter);
    }

    private String getDefaultValue(ParameterInfo parameterTemplate) {
        if (parameterTemplate.getDefaultValue() == null && !parameterTemplate.isOptional()) {
            throw new IllegalArgumentException("No value supplied for required parameter " + parameterTemplate.getName());
        } else {
            return parameterTemplate.getDefaultValue();
        }
    }

    private void addMissingParameters(List<ParameterInfo> fullParameters, Map<String, ParameterInfo> templateParameterMap) {
        for (ParameterInfo parameterTemplate : templateParameterMap.values()) {
            ParameterInfo newParameter = new ParameterInfo();
            newParameter.setName(parameterTemplate.getName());
            newParameter.setValue(getDefaultValue(parameterTemplate));
            if (parameterTemplate.isInputFile()) {
                handleInputFileParameter(newParameter, parameterTemplate);
            }
            fullParameters.add(newParameter);
        }
    }

    private Map<String, ParameterInfo> getTemplateParameterMap(TaskInfo taskInfo) {
        Map<String, ParameterInfo> templateParameterMap = new HashMap<String, ParameterInfo>();
        for (ParameterInfo parameterInfo : taskInfo.getParameterInfoArray()) {
            templateParameterMap.put(parameterInfo.getName(), parameterInfo);
        }
        return templateParameterMap;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        closeAnalysisService();
        this.url = url;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    public JobInfo getStatus(JobInfo jobInfo) throws GenePatternServiceException {
        try {
            return getAnalysis().checkStatus(jobInfo.getJobNumber());
        } catch (WebServiceException e) {
            throw new GenePatternServiceException(e);
        } catch (RemoteException e) {
            throw new GenePatternServiceException(e);
        } catch (GenePatternServiceException e) {
            throw new GenePatternServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public FileWrapper[] getResultFiles(JobInfo jobInfo) throws GenePatternServiceException {
        return getResultFilesFromJobNumber(jobInfo.getJobNumber());
    }

    private FileWrapper[] getResultFilesFromJobNumber(int jobInfoNumber) throws GenePatternServiceException {
        try {
            return getAnalysis().getResultFiles(jobInfoNumber);
        } catch (WebServiceException e) {
            throw new GenePatternServiceException(e);
        } catch (RemoteException e) {
            throw new GenePatternServiceException(e);
        } catch (GenePatternServiceException e) {
            throw new GenePatternServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public FileWrapper getResultFile(JobInfo jobInfo, String filename) throws GenePatternServiceException {
        return getResultFileFromJobNumber(jobInfo.getJobNumber(), filename);
    }


    private FileWrapper getResultFileFromJobNumber(int jobInfoNumber, String filename) throws GenePatternServiceException {
        try {
            FileWrapper[] wrappers = getAnalysis().getResultFiles(jobInfoNumber, new String[] {filename});
            if (wrappers.length == 0) {
                return null;
            } else {
                return wrappers[0];
            }
        } catch (WebServiceException e) {
            throw new GenePatternServiceException(e);
        } catch (RemoteException e) {
            throw new GenePatternServiceException(e);
        } catch (GenePatternServiceException e) {
            throw new GenePatternServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public File getResultFile(org.genepattern.webservice.JobInfo jobInfo, String filename)
            throws org.genepattern.webservice.WebServiceException {
        
        try {
            return createFileFromFileWrapper(getResultFileFromJobNumber(jobInfo.getJobNumber(), filename), null);
        } catch (Exception e) {
            throw new org.genepattern.webservice.WebServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public File[] getResultFiles(org.genepattern.webservice.JobInfo jobInfo, File resultsDir)
            throws org.genepattern.webservice.WebServiceException, IOException {
        try {
            FileWrapper[] files = getResultFilesFromJobNumber(jobInfo.getJobNumber());
            File[] newFiles = new File[files.length];
            int ctr = 0;
            for (FileWrapper fileWrapper : files) {
                newFiles[ctr] = createFileFromFileWrapper(fileWrapper, resultsDir);
                ctr++;
            }
            return newFiles;
        } catch (GenePatternServiceException e) {
            throw new org.genepattern.webservice.WebServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public org.genepattern.webservice.JobInfo getStatus(org.genepattern.webservice.JobInfo jobInfo)
            throws org.genepattern.webservice.WebServiceException {
        try {
            return convertJobInfoToNewVersion(getAnalysis().checkStatus(jobInfo.getJobNumber()));
        } catch (Exception e) {
            throw new org.genepattern.webservice.WebServiceException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public org.genepattern.webservice.JobInfo runAnalysis(String moduleName,
            List<org.genepattern.webservice.ParameterInfo> parameters)
            throws org.genepattern.webservice.WebServiceException {
        try {
            return convertJobInfoToNewVersion(runAnalysisCai2(moduleName, convertParametersToOldVersion(parameters)));
        } catch (GenePatternServiceException e) {
            throw new org.genepattern.webservice.WebServiceException(e);
        }
        
    }

    /**
     * {@inheritDoc}
     */
    public org.genepattern.webservice.TaskInfo[] getTasks() throws org.genepattern.webservice.WebServiceException {
        try {
            TaskInfo[] origTasks = getTasksOld();
            List<org.genepattern.webservice.TaskInfo> tasksList = new ArrayList<org.genepattern.webservice.TaskInfo>();
            for (TaskInfo taskInfo : origTasks) {
                if (taskInfo != null) {
                    tasksList.add(convertTaskInfoToNewVersion(taskInfo));
                }
            }
            return tasksList.toArray(new org.genepattern.webservice.TaskInfo[tasksList.size()]);
        } catch (GenePatternServiceException e) {
            throw new org.genepattern.webservice.WebServiceException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private org.genepattern.webservice.TaskInfo convertTaskInfoToNewVersion(TaskInfo taskInfo) {
        org.genepattern.webservice.TaskInfo newTaskInfo = new org.genepattern.webservice.TaskInfo();
        newTaskInfo.setAccessId(taskInfo.getAccessId());
        newTaskInfo.setAttributes(taskInfo.getAttributes());
        newTaskInfo.setDescription(taskInfo.getDescription());
        newTaskInfo.setID(taskInfo.getID());
        newTaskInfo.setName(taskInfo.getName());
        newTaskInfo.setParameterInfo(taskInfo.getParameterInfo());
        newTaskInfo.setParameterInfoArray(convertParameterInfoToNewVersion(taskInfo.getParameterInfoArray()));
        newTaskInfo.setTaskInfoAttributes((Map) taskInfo.getTaskInfoAttributes());
        newTaskInfo.setUserId(taskInfo.getUserId());
        return newTaskInfo;
    }

    private org.genepattern.webservice.ParameterInfo[] convertParameterInfoToNewVersion(
            ParameterInfo[] parameterInfoArray) {
        List<org.genepattern.webservice.ParameterInfo> newParameterInfoArray = new ArrayList<org.genepattern.webservice.ParameterInfo>(); 
        for (ParameterInfo parameterInfo : parameterInfoArray) {
            if (parameterInfo != null) {
                org.genepattern.webservice.ParameterInfo newParameterInfo = 
                    new org.genepattern.webservice.ParameterInfo();
                newParameterInfo.setAttributes(parameterInfo.getAttributes());
                newParameterInfo.setDescription(parameterInfo.getDescription());
                newParameterInfo.setLabel(parameterInfo.getLabel());
                newParameterInfo.setName(parameterInfo.getName());
                newParameterInfo.setValue(parameterInfo.getValue());
                newParameterInfoArray.add(newParameterInfo);
            }
        }
        return newParameterInfoArray.toArray(new org.genepattern.webservice.ParameterInfo[newParameterInfoArray.size()]);
    }
    
    private org.genepattern.webservice.JobInfo convertJobInfoToNewVersion(JobInfo jobInfo) {
        org.genepattern.webservice.JobInfo newJobInfo = new org.genepattern.webservice.JobInfo();
        newJobInfo.setJobNumber(jobInfo.getJobNumber());
        newJobInfo.setStatus(jobInfo.getStatus());
        newJobInfo.setTaskID(jobInfo.getTaskID());
        newJobInfo.setTaskLSID(jobInfo.getTaskLSID());
        newJobInfo.setTaskName(jobInfo.getTaskName());
        newJobInfo.setUserId(jobInfo.getUserId());
        return newJobInfo;
    }

    private List<ParameterInfo> convertParametersToOldVersion(List<org.genepattern.webservice.ParameterInfo> parameters) {
        List<ParameterInfo> parameterInfoList = new ArrayList<ParameterInfo>();
        for (org.genepattern.webservice.ParameterInfo newParameterInfo : parameters) {
            ParameterInfo oldParameterInfo = new ParameterInfo();
            oldParameterInfo.setAttributes(newParameterInfo.getAttributes());
            oldParameterInfo.setDescription(newParameterInfo.getDescription());
            oldParameterInfo.setLabel(newParameterInfo.getLabel());
            oldParameterInfo.setName(newParameterInfo.getName());
            oldParameterInfo.setValue(newParameterInfo.getValue());
            parameterInfoList.add(oldParameterInfo);
        }
        return parameterInfoList;
    }
    
    private File createFileFromFileWrapper(FileWrapper fileWrapper, File directory) throws IOException {
        File outputFile = directory == null ? 
                File.createTempFile(fileWrapper.getFilename(), "") 
                : File.createTempFile(fileWrapper.getFilename(), "", directory); 
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        IOUtils.copy(fileWrapper.getDataHandler().getInputStream(), fileOutputStream);
        fileOutputStream.close();
        return outputFile;
    }

    /**
     * {@inheritDoc}
     */
    public boolean validateConnection() {
        try {
            getAnalysis().ping();
        } catch (Exception e) {
            return false;
        }
        return true;
        
    }

}
