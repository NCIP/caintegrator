/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package edu.mit.broad.genepattern.gp.services;

import java.io.File;
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

import edu.mit.broad.genepattern.gp.services.Analysis;
import edu.mit.broad.genepattern.gp.services.AnalysisService;
import edu.mit.broad.genepattern.gp.services.AnalysisServiceLocator;
import edu.mit.broad.genepattern.gp.services.AnalysisSoapBindingStub;
import edu.mit.broad.genepattern.gp.services.JobInfo;
import edu.mit.broad.genepattern.gp.services.ParameterInfo;
import edu.mit.broad.genepattern.gp.services.TaskInfo;
import edu.mit.broad.genepattern.gp.services.WebServiceException;

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
        if (url == null || username == null) {
            throw new IllegalStateException("Must set URL and username prior to using GenePattern service");
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
        for (TaskInfo taskInfo : getTasks()) {
            taskMap.put(taskInfo.getName(), taskInfo);
        }
    }
    
    /**
     * Returns information for all available tasks
     * 
     * @return information on all tasks.
     * @throws GenePatternServiceException if there's a problem communicating with the service.
     */
    public TaskInfo[] getTasks() throws GenePatternServiceException {
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
    public JobInfo runAnalysis(String taskName, List<ParameterInfo> parameters) throws GenePatternServiceException {
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
        try {
            return getAnalysis().getResultFiles(jobInfo.getJobNumber());
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
        try {
            FileWrapper[] wrappers = getAnalysis().getResultFiles(jobInfo.getJobNumber(), new String[] {filename});
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

}
