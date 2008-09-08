/**
 * Analysis.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package edu.mit.broad.genepattern.gp.services.Analysis;

@SuppressWarnings("unchecked")
public interface Analysis extends java.rmi.Remote {
    public int[] getChildren(int jobId) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo getJob(int jobId) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public void deleteJob(int jobId) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public void deleteJobResultFile(int jobId, java.lang.String value) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public void terminateJob(int jobId) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo[] getJobs(java.lang.String username, int maxJobNumber, int maxEntries, boolean allJobs) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo[] getJobs(java.lang.String username, int maxJobNumber, int maxEntries, boolean allJobs, java.lang.Object jobSortOrder, boolean asc) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public java.lang.String createProvenancePipeline(analysis.JobInfo[] jobs, java.lang.String pipelineName) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public java.lang.String createProvenancePipeline(java.lang.String fileUrlOrJobNumber, java.lang.String pipelineName) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo submitJob(int taskID, analysis.ParameterInfo[] parameters, java.util.HashMap files) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo submitJob(int taskID, analysis.ParameterInfo[] parameters, java.util.HashMap files, int parentJobId) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo checkStatus(int jobID) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo recordClientJob(int taskID, analysis.ParameterInfo[] parameters) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo recordClientJob(int taskID, analysis.ParameterInfo[] parameters, int parentJobNumber) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public void setJobStatus(int jobId, java.lang.String status) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.TaskInfo[] getTasks() throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.JobInfo[] findJobsThatCreatedFile(java.lang.String fileURLOrJobNumber) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.FileWrapper[] getResultFiles(int jobId, java.lang.String[] filenames) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public analysis.FileWrapper[] getResultFiles(int jobId) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public void purgeJob(int jobId) throws java.rmi.RemoteException, org.genepattern.webservice.WebServiceException;
    public java.lang.String getWebServiceName() throws java.rmi.RemoteException;
    public java.lang.String getWebServiceInfo() throws java.rmi.RemoteException;
    public void setEncodingScheme(java.lang.String scheme) throws java.rmi.RemoteException;
    public java.lang.String getEncodingScheme() throws java.rmi.RemoteException;
    public float getEncodingSchemeVersion() throws java.rmi.RemoteException;
    public void setEncodingSchemeVersion(float version) throws java.rmi.RemoteException;
    public java.lang.String ping() throws java.rmi.RemoteException;
}
