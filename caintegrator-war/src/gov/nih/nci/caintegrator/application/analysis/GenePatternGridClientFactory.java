/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import org.genepattern.cagrid.service.compmarker.mage.common.ComparativeMarkerSelMAGESvcI;
import org.genepattern.cagrid.service.preprocessdataset.mage.common.PreprocessDatasetMAGEServiceI;
import org.genepattern.gistic.common.GisticI;
import org.genepattern.pca.common.PCAI;

/**
 * Used to generate GenePatternGridClients.
 */
public interface GenePatternGridClientFactory {

    /**
     * Retrieves client for PreprocessDataset.
     * @param server for grid connection.
     * @return client for PreprocessDataset.
     * @throws ConnectionException if an error occurs connecting to server.
     */
    PreprocessDatasetMAGEServiceI createPreprocessDatasetClient(ServerConnectionProfile server)
            throws ConnectionException;
    
    /**
     * Retrieves client for Comparative Marker Selection.
     * @param server for grid connection.
     * @return client for Comparative Marker Selection.
     * @throws ConnectionException if an error occurs connecting to server.
     */
    ComparativeMarkerSelMAGESvcI createComparativeMarkerSelClient(ServerConnectionProfile server) 
            throws ConnectionException;
    
    /**
     * Retrieves client for PCA.
     * @param server for grid connection.
     * @return client for PCA.
     * @throws ConnectionException if an error occurs connecting to server.
     */
    PCAI createPCAClient(ServerConnectionProfile server) throws ConnectionException;
    
    /**
     * Retrieves client for Gistic.
     * @param server for grid connection.
     * @return client for Gistic.
     * @throws ConnectionException if an error occurs connecting to server.
     */
    GisticI createGisticClient(ServerConnectionProfile server) throws ConnectionException;

}
