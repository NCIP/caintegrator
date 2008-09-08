/**
 * AnalysisServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package edu.mit.broad.genepattern.gp.services.Analysis;

@SuppressWarnings("unchecked")
public class AnalysisServiceLocator extends org.apache.axis.client.Service implements edu.mit.broad.genepattern.gp.services.Analysis.AnalysisService {

    private static final long serialVersionUID = 1L;

    public AnalysisServiceLocator() {
    }


    public AnalysisServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AnalysisServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Analysis
    private java.lang.String Analysis_address = "http://genepattern.broad.mit.edu/gp/services/Analysis";

    public java.lang.String getAnalysisAddress() {
        return Analysis_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AnalysisWSDDServiceName = "Analysis";

    public java.lang.String getAnalysisWSDDServiceName() {
        return AnalysisWSDDServiceName;
    }

    public void setAnalysisWSDDServiceName(java.lang.String name) {
        AnalysisWSDDServiceName = name;
    }

    public edu.mit.broad.genepattern.gp.services.Analysis.Analysis getAnalysis() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Analysis_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAnalysis(endpoint);
    }

    public edu.mit.broad.genepattern.gp.services.Analysis.Analysis getAnalysis(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            edu.mit.broad.genepattern.gp.services.Analysis.AnalysisSoapBindingStub _stub = new edu.mit.broad.genepattern.gp.services.Analysis.AnalysisSoapBindingStub(portAddress, this);
            _stub.setPortName(getAnalysisWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAnalysisEndpointAddress(java.lang.String address) {
        Analysis_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (edu.mit.broad.genepattern.gp.services.Analysis.Analysis.class.isAssignableFrom(serviceEndpointInterface)) {
                edu.mit.broad.genepattern.gp.services.Analysis.AnalysisSoapBindingStub _stub = new edu.mit.broad.genepattern.gp.services.Analysis.AnalysisSoapBindingStub(new java.net.URL(Analysis_address), this);
                _stub.setPortName(getAnalysisWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Analysis".equals(inputPortName)) {
            return getAnalysis();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://genepattern.broad.mit.edu/gp/services/Analysis", "AnalysisService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://genepattern.broad.mit.edu/gp/services/Analysis", "Analysis"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        if ("Analysis".equals(portName)) {
            setAnalysisEndpointAddress(address);
        }
        else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
