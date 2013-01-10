/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.bioconductor;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.bioconductor.packages.caDNAcopy.common.CaDNAcopyI;
import org.bioconductor.packages.caCGHcall.common.CaCGHcallI;

/**
 * Creates Bioconductor grid client instances.
 */
public interface BioconductorClientFactory {

    /**
     * Returns a <code>CaDNAcopyI</code> for the grid URL given.
     *
     * @param url location of the service.
     * @return the service instance.
     * @throws RemoteException if the service connection failed.
     * @throws MalformedURIException if the URL is incorrect.
     */
    CaDNAcopyI getCaDNAcopyI(String url) throws MalformedURIException, RemoteException;
    
    /**
     * Returns a <code>CaCGHcallI</code> for the grid URL given.
     *
     * @param url location of the service.
     * @return the service instance.
     * @throws RemoteException if the service connection failed.
     * @throws MalformedURIException if the URL is incorrect.
     */
    CaCGHcallI getCaCGHcallI(String url) throws MalformedURIException, RemoteException;    

}
