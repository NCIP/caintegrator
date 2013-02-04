/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */

package edu.mit.broad.genepattern.gp.services;

public interface AnalysisService extends javax.xml.rpc.Service {
    public java.lang.String getAnalysisAddress();

    public edu.mit.broad.genepattern.gp.services.Analysis getAnalysis() throws javax.xml.rpc.ServiceException;

    public edu.mit.broad.genepattern.gp.services.Analysis getAnalysis(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
