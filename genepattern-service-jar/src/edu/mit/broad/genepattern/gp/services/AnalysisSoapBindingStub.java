/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */

package edu.mit.broad.genepattern.gp.services;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class AnalysisSoapBindingStub extends org.apache.axis.client.Stub implements edu.mit.broad.genepattern.gp.services.Analysis {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[27];
        _initOperationDesc1();
        _initOperationDesc2();
        _initOperationDesc3();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getChildren");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_xsd_int"));
        oper.setReturnClass(int[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getChildrenReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getJob");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("Analysis", "JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getJobReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteJob");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteJobResultFile");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "value"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("terminateJob");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getJobs");
        oper.addParameter(new javax.xml.namespace.QName("", "username"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "maxJobNumber"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "maxEntries"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "allJobs"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getJobsReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getJobs");
        oper.addParameter(new javax.xml.namespace.QName("", "username"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "maxJobNumber"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "maxEntries"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "allJobs"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "jobSortOrder"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"), java.lang.Object.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "asc"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getJobsReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createProvenancePipeline");
        oper.addParameter(new javax.xml.namespace.QName("", "jobs"), new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_JobInfo"), edu.mit.broad.genepattern.gp.services.JobInfo[].class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "pipelineName"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "createProvenancePipelineReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("createProvenancePipeline");
        oper.addParameter(new javax.xml.namespace.QName("", "fileUrlOrJobNumber"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "pipelineName"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "createProvenancePipelineReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("submitJob");
        oper.addParameter(new javax.xml.namespace.QName("", "taskID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "parameters"), new javax.xml.namespace.QName("Analysis", "ParameterInfoArray"), edu.mit.broad.genepattern.gp.services.ParameterInfo[].class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "files"), new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("Analysis", "JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "submitJobReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("submitJob");
        oper.addParameter(new javax.xml.namespace.QName("", "taskID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "parameters"), new javax.xml.namespace.QName("Analysis", "ParameterInfoArray"), edu.mit.broad.genepattern.gp.services.ParameterInfo[].class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "files"), new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"), java.util.HashMap.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "parentJobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("Analysis", "JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "submitJobReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("checkStatus");
        oper.addParameter(new javax.xml.namespace.QName("", "jobID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("Analysis", "JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "checkStatusReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("recordClientJob");
        oper.addParameter(new javax.xml.namespace.QName("", "taskID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "parameters"), new javax.xml.namespace.QName("Analysis", "ParameterInfoArray"), edu.mit.broad.genepattern.gp.services.ParameterInfo[].class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("Analysis", "JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "recordClientJobReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("recordClientJob");
        oper.addParameter(new javax.xml.namespace.QName("", "taskID"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "parameters"), new javax.xml.namespace.QName("Analysis", "ParameterInfoArray"), edu.mit.broad.genepattern.gp.services.ParameterInfo[].class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "parentJobNumber"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("Analysis", "JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "recordClientJobReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setJobStatus");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "status"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getTasks");
        oper.setReturnType(new javax.xml.namespace.QName("Analysis", "TaskInfoArray"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.TaskInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getTasksReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("findJobsThatCreatedFile");
        oper.addParameter(new javax.xml.namespace.QName("", "fileURLOrJobNumber"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_JobInfo"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.JobInfo[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "findJobsThatCreatedFileReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResultFiles");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "filenames"), new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_soapenc_string"), java.lang.String[].class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_FileWrapper"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.FileWrapper[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getResultFilesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[17] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getResultFiles");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_FileWrapper"));
        oper.setReturnClass(edu.mit.broad.genepattern.gp.services.FileWrapper[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getResultFilesReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[18] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("purgeJob");
        oper.addParameter(new javax.xml.namespace.QName("", "jobId"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "fault"),
                      "edu.mit.broad.genepattern.gp.services.WebServiceException",
                      new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException"), 
                      true
                     ));
        _operations[19] = oper;

    }

    private static void _initOperationDesc3(){
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getWebServiceName");
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getWebServiceNameReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[20] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getWebServiceInfo");
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getWebServiceInfoReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[21] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setEncodingScheme");
        oper.addParameter(new javax.xml.namespace.QName("", "scheme"), new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[22] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getEncodingScheme");
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getEncodingSchemeReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[23] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getEncodingSchemeVersion");
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        oper.setReturnClass(float.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "getEncodingSchemeVersionReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[24] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setEncodingSchemeVersion");
        oper.addParameter(new javax.xml.namespace.QName("", "version"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[25] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ping");
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "pingReturn"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[26] = oper;

    }

    public AnalysisSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public AnalysisSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public AnalysisSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://webservice.genepattern.org", "WebServiceException");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.WebServiceException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "mapItem");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.MapItem.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_FileWrapper");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.FileWrapper[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_tns3_JobInfo");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.JobInfo[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_soapenc_string");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("Analysis", "ParameterInfo");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.ParameterInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("Analysis", "FileWrapper");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.FileWrapper.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("Analysis", "JobInfo");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.JobInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://genepattern.broadinstitute.org/gp/services/Analysis", "ArrayOf_xsd_int");
            cachedSerQNames.add(qName);
            cls = int[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("Analysis", "TaskInfo");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.TaskInfo.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("Analysis", "TaskInfoArray");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.TaskInfo[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

            qName = new javax.xml.namespace.QName("Analysis", "ParameterInfoArray");
            cachedSerQNames.add(qName);
            cls = edu.mit.broad.genepattern.gp.services.ParameterInfo[].class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(arraysf);
            cachedDeserFactories.add(arraydf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                        java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                        _call.registerTypeMapping(cls, qName, sf, df, false);
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public int[] getChildren(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "getChildren"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (int[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (int[]) org.apache.axis.utils.JavaUtils.convert(_resp, int[].class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo getJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "getJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo.class);
            }
        }
    }

    public void deleteJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "deleteJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void deleteJobResultFile(int jobId, java.lang.String value) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "deleteJobResultFile"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId), value});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void terminateJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "terminateJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo[] getJobs(java.lang.String username, int maxJobNumber, int maxEntries, boolean allJobs) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "getJobs"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {username, new java.lang.Integer(maxJobNumber), new java.lang.Integer(maxEntries), new java.lang.Boolean(allJobs)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo[].class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo[] getJobs(java.lang.String username, int maxJobNumber, int maxEntries, boolean allJobs, java.lang.Object jobSortOrder, boolean asc) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "getJobs"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {username, new java.lang.Integer(maxJobNumber), new java.lang.Integer(maxEntries), new java.lang.Boolean(allJobs), jobSortOrder, new java.lang.Boolean(asc)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo[].class);
            }
        }
    }

    public java.lang.String createProvenancePipeline(edu.mit.broad.genepattern.gp.services.JobInfo[] jobs, java.lang.String pipelineName) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "createProvenancePipeline"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {jobs, pipelineName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String createProvenancePipeline(java.lang.String fileUrlOrJobNumber, java.lang.String pipelineName) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "createProvenancePipeline"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {fileUrlOrJobNumber, pipelineName});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo submitJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters, java.util.HashMap files) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "submitJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(taskID), parameters, files});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo.class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo submitJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters, java.util.HashMap files, int parentJobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "submitJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(taskID), parameters, files, new java.lang.Integer(parentJobId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo.class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo checkStatus(int jobID) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "checkStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo.class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo recordClientJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "recordClientJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(taskID), parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo.class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo recordClientJob(int taskID, edu.mit.broad.genepattern.gp.services.ParameterInfo[] parameters, int parentJobNumber) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "recordClientJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(taskID), parameters, new java.lang.Integer(parentJobNumber)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo.class);
            }
        }
    }

    public void setJobStatus(int jobId, java.lang.String status) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "setJobStatus"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId), status});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public edu.mit.broad.genepattern.gp.services.TaskInfo[] getTasks() throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "getTasks"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.TaskInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.TaskInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.TaskInfo[].class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.JobInfo[] findJobsThatCreatedFile(java.lang.String fileURLOrJobNumber) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "findJobsThatCreatedFile"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {fileURLOrJobNumber});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.JobInfo[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.JobInfo[]) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.JobInfo[].class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.FileWrapper[] getResultFiles(int jobId, java.lang.String[] filenames) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "getResultFiles"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId), filenames});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.FileWrapper[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.FileWrapper[]) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.FileWrapper[].class);
            }
        }
    }

    public edu.mit.broad.genepattern.gp.services.FileWrapper[] getResultFiles(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[18]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "getResultFiles"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (edu.mit.broad.genepattern.gp.services.FileWrapper[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (edu.mit.broad.genepattern.gp.services.FileWrapper[]) org.apache.axis.utils.JavaUtils.convert(_resp, edu.mit.broad.genepattern.gp.services.FileWrapper[].class);
            }
        }
    }

    public void purgeJob(int jobId) throws java.rmi.RemoteException, edu.mit.broad.genepattern.gp.services.WebServiceException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[19]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://server.webservice.server.genepattern.org", "purgeJob"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(jobId)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public java.lang.String getWebServiceName() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[20]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.server.genepattern.org", "getWebServiceName"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public java.lang.String getWebServiceInfo() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[21]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.server.genepattern.org", "getWebServiceInfo"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public void setEncodingScheme(java.lang.String scheme) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[22]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.server.genepattern.org", "setEncodingScheme"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {scheme});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public java.lang.String getEncodingScheme() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[23]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.server.genepattern.org", "getEncodingScheme"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

    public float getEncodingSchemeVersion() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[24]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.server.genepattern.org", "getEncodingSchemeVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Float) _resp).floatValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Float) org.apache.axis.utils.JavaUtils.convert(_resp, float.class)).floatValue();
            }
        }
    }

    public void setEncodingSchemeVersion(float version) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[25]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.server.genepattern.org", "setEncodingSchemeVersion"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Float(version)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public java.lang.String ping() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[26]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://webservice.server.genepattern.org", "ping"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
    }

}
