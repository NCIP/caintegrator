/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import edu.mit.broad.genepattern.gp.services.CaIntegrator2GPClient;
import edu.mit.broad.genepattern.gp.services.GenePatternClientImpl;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.file.FileManager;

import org.genepattern.webservice.WebServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the GenePatternClientFactory which returns GenePatternClientImpl objects.
 */
@Service("genePatternClientFactory")
public class GenePatternClientFactoryImpl implements GenePatternClientFactory {
    private FileManager fileManager;

    /**
     * {@inheritDoc}
     */
    public CaIntegrator2GPClient retrieveClient(ServerConnectionProfile server) throws WebServiceException {
        return new CaIntegrator2GPClientImpl(server.getUrl(), server.getUsername(),
                server.getPassword(), fileManager);
    }

    /**
     * {@inheritDoc}
     */
    public CaIntegrator2GPClient retrieveOldGenePatternClient(ServerConnectionProfile server) {
        GenePatternClientImpl client = new GenePatternClientImpl();
        client.setUrl(server.getUrl());
        client.setUsername(server.getUsername());
        client.setPassword(server.getPassword());
        return client;
    }

    /**
     * @return the fileManager
     */
    public FileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    @Autowired
    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

}
