/**
 * AnalysisService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2RC2 Apr 28, 2006 (12:42:00 EDT) WSDL2Java emitter.
 */

package edu.mit.broad.genepattern.gp.services.Analysis;

public interface AnalysisService extends javax.xml.rpc.Service {
    public java.lang.String getAnalysisAddress();

    public edu.mit.broad.genepattern.gp.services.Analysis.Analysis getAnalysis() throws javax.xml.rpc.ServiceException;

    public edu.mit.broad.genepattern.gp.services.Analysis.Analysis getAnalysis(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
