/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.external.bioconductor;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.bioconductor.packages.caDNAcopy.client.CaDNAcopyClient;
import org.bioconductor.packages.caDNAcopy.common.CaDNAcopyI;
import org.bioconductor.packages.caCGHcall.client.CaCGHcallClient;
import org.bioconductor.packages.caCGHcall.common.CaCGHcallI;

/**
 * Returns Bioconductor grid service instances.
 */
public class BioconductorClientFactoryImpl implements BioconductorClientFactory {

    /**
     * {@inheritDoc}
     */
    public CaDNAcopyI getCaDNAcopyI(String url) throws MalformedURIException, RemoteException {
        return new CaDNAcopyClient(url);
    }
    
    /**
     * {@inheritDoc}
     */
    public CaCGHcallI getCaCGHcallI(String url) throws MalformedURIException, RemoteException {
        return new CaCGHcallClient(url);
    }    

}
