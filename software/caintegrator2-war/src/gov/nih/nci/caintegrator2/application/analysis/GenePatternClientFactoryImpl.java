/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import edu.mit.broad.genepattern.gp.services.GenePatternClient;
import edu.mit.broad.genepattern.gp.services.GenePatternClientImpl;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

/**
 * Implementation of the GenePatternClientFactory which returns GenePatternClientImpl objects.
 */
public class GenePatternClientFactoryImpl implements GenePatternClientFactory {

    /**
     * {@inheritDoc}
     */
    public GenePatternClient retrieveClient(ServerConnectionProfile server) {
        GenePatternClient client = new GenePatternClientImpl();
        client.setUrl(server.getUrl());
        client.setUsername(server.getUsername());
        client.setPassword(server.getPassword());
        return client;
    }

}
