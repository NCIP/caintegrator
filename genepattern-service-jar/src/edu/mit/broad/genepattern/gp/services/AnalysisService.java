/**
 * AnalysisService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Nov 16, 2004 (12:19:44 EST) WSDL2Java emitter.
 */

package edu.mit.broad.genepattern.gp.services;

public interface AnalysisService extends javax.xml.rpc.Service {
    public java.lang.String getAnalysisAddress();

    public edu.mit.broad.genepattern.gp.services.Analysis getAnalysis() throws javax.xml.rpc.ServiceException;

    public edu.mit.broad.genepattern.gp.services.Analysis getAnalysis(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
