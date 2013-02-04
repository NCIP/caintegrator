/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import org.genepattern.webservice.WebServiceException;

/**
 * Retrieves a GenePatternClient from a server connection.
 */
public interface GenePatternClientFactory {
    
    /**
     * Turns a server connection into a GenePatternClient.  Note, this doesn't currently work because it uses
     * the client in GenePattern-3.2.1, which requires axis-1.4.jar and that isn't supported to work alongside
     * axis-1.2RC2.jar which is what everything else uses.  So for now, use the retrieveOldGenePatternClient method.
     * @param server connection to use.
     * @return GP Client.
     * @throws WebServiceException if unable to connect to gene pattern.
     */
    CaIntegrator2GPClient retrieveClient(ServerConnectionProfile server) throws WebServiceException;
    
    /**
     * Turns a server connection into an old version of GenePatternClient.  This is because GenePattern 3.2.1
     * currently requires an Axis 1.4 jar, and we use Axis 1.2RC2.
     * @param server connection to use.
     * @return GP Client.
     */
    CaIntegrator2GPClient retrieveOldGenePatternClient(ServerConnectionProfile server);

}
