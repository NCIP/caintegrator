/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

/**
 * Retrieves a GenePatternClient from a server connection.
 */
public interface GenePatternClientFactory {
    
    /**
     * Turns a server connection into a GenePatternClient.
     * @param server connection to use.
     * @return GP Client.
     */
    GenePatternClient retrieveClient(ServerConnectionProfile server);

}
