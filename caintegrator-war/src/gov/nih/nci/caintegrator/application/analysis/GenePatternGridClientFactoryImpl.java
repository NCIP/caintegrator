/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.genepattern.cagrid.service.compmarker.mage.client.ComparativeMarkerSelMAGESvcClient;
import org.genepattern.cagrid.service.compmarker.mage.common.ComparativeMarkerSelMAGESvcI;
import org.genepattern.cagrid.service.preprocessdataset.mage.client.PreprocessDatasetMAGEServiceClient;
import org.genepattern.cagrid.service.preprocessdataset.mage.common.PreprocessDatasetMAGEServiceI;
import org.genepattern.gistic.client.GisticClient;
import org.genepattern.gistic.common.GisticI;
import org.genepattern.pca.client.PCAClient;
import org.genepattern.pca.common.PCAI;
import org.springframework.stereotype.Component;

/**
 * Implementation of GenePatternGridClientFactory.
 */
@Component
public class GenePatternGridClientFactoryImpl implements GenePatternGridClientFactory {

    private static final String MALFORMED_URI = "Malformed URI.";
    private static final String MUST_SPECIFY_GRID_URL = "Must specify grid URL";
    private static final String REMOTE_CONNECTION_FAILED = "Remote Connection Failed.";

    /**
     * {@inheritDoc}
     */
    @Override
    public PreprocessDatasetMAGEServiceI createPreprocessDatasetClient(ServerConnectionProfile server)
            throws ConnectionException {
        if (server == null || server.getUrl() == null) {
            throw new IllegalArgumentException(MUST_SPECIFY_GRID_URL);
        }
        try {
            return new PreprocessDatasetMAGEServiceClient(server.getUrl());
        } catch (MalformedURIException e) {
            throw new ConnectionException(MALFORMED_URI, e);
        } catch (RemoteException e) {
            throw new ConnectionException(REMOTE_CONNECTION_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComparativeMarkerSelMAGESvcI createComparativeMarkerSelClient(ServerConnectionProfile server)
            throws ConnectionException {
        if (server == null || server.getUrl() == null) {
            throw new IllegalArgumentException(MUST_SPECIFY_GRID_URL);
        }
        try {
            return new ComparativeMarkerSelMAGESvcClient(server.getUrl());
        } catch (MalformedURIException e) {
            throw new ConnectionException(MALFORMED_URI, e);
        } catch (RemoteException e) {
            throw new ConnectionException(REMOTE_CONNECTION_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PCAI createPCAClient(ServerConnectionProfile server) throws ConnectionException {
        if (server == null || server.getUrl() == null) {
            throw new IllegalArgumentException(MUST_SPECIFY_GRID_URL);
        }
        try {
            return new PCAClient(server.getUrl());
        } catch (MalformedURIException e) {
            throw new ConnectionException(MALFORMED_URI, e);
        } catch (RemoteException e) {
            throw new ConnectionException(REMOTE_CONNECTION_FAILED, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GisticI createGisticClient(ServerConnectionProfile server) throws ConnectionException {
        if (server == null || server.getUrl() == null) {
            throw new IllegalArgumentException(MUST_SPECIFY_GRID_URL);
        }
        try {
            return new GisticClient(server.getUrl());
        } catch (MalformedURIException e) {
            throw new ConnectionException(MALFORMED_URI, e);
        } catch (RemoteException e) {
            throw new ConnectionException(REMOTE_CONNECTION_FAILED, e);
        }
    }

}
