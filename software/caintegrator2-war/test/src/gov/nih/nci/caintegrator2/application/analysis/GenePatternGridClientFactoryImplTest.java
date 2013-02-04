/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.analysis;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import org.junit.Test;

public class GenePatternGridClientFactoryImplTest {


    @Test(expected = IllegalArgumentException.class)
    public void testCreatePreprocessDatasetClientNoUrl() throws ConnectionException {
        GenePatternGridClientFactoryImpl client = new GenePatternGridClientFactoryImpl();
        ServerConnectionProfile server = new ServerConnectionProfile();
        client.createPreprocessDatasetClient(server);
    }

    @Test(expected = ConnectionException.class)
    public void testCreatePreprocessDatasetClientBadUrl() throws ConnectionException {
        GenePatternGridClientFactoryImpl client = new GenePatternGridClientFactoryImpl();
        ServerConnectionProfile server = new ServerConnectionProfile();
        server.setUrl("Invalid URL");
        client.createPreprocessDatasetClient(server);
    }

}
