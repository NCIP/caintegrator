/**
 * Analysis.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Nov 16, 2004 (12:19:44 EST) WSDL2Java emitter.
 */

package edu.mit.broad.genepattern.gp.services;

@SuppressWarnings("unchecked")
public interface Analysis extends java.rmi.Remote {
    public int[] getChildren(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo getJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public void deleteJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public void deleteJobResultFile(int jobId, java.lang.String value) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public void terminateJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo[] getJobs(java.lang.String username, int maxJobNumber, int maxEntries, boolean allJobs) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo[] getJobs(java.lang.String username, int maxJobNumber, int maxEntries, boolean allJobs, java.lang.Object jobSortOrder, boolean asc) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public java.lang.String createProvenancePipeline(edu.mit.broad.genepattern.gp.services.JobInfo[] jobs, java.lang.String pipelineName) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public java.lang.String createProvenancePipeline(java.lang.String fileUrlOrJobNumber, java.lang.String pipelineName) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo submitJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters, java.util.HashMap files) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo submitJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters, java.util.HashMap files, int parentJobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo checkStatus(int jobID) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo recordClientJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo recordClientJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters, int parentJobNumber) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public void setJobStatus(int jobId, java.lang.String status) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.TaskInfo[] getTasks() throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.JobInfo[] findJobsThatCreatedFile(java.lang.String fileURLOrJobNumber) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.FileWrapper[] getResultFiles(int jobId, java.lang.String[] filenames) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public edu.mit.broad.genepattern.gp.services.FileWrapper[] getResultFiles(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public void purgeJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException;
    public java.lang.String getWebServiceName() throws java.rmi.RemoteException;
    public java.lang.String getWebServiceInfo() throws java.rmi.RemoteException;
    public void setEncodingScheme(java.lang.String scheme) throws java.rmi.RemoteException;
    public java.lang.String getEncodingScheme() throws java.rmi.RemoteException;
    public float getEncodingSchemeVersion() throws java.rmi.RemoteException;
    public void setEncodingSchemeVersion(float version) throws java.rmi.RemoteException;
    public java.lang.String ping() throws java.rmi.RemoteException;
}
