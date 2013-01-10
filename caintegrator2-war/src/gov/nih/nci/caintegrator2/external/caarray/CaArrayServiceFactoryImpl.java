/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caarray.services.ServerConnectionException;
import gov.nih.nci.caarray.services.external.v1_0.CaArrayServer;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import javax.ejb.EJBException;
import javax.security.auth.login.FailedLoginException;

import org.apache.commons.lang3.StringUtils;

/**
 * Implementation of the service factory.
 */
public class CaArrayServiceFactoryImpl implements CaArrayServiceFactory {

    /**
     * {@inheritDoc}
     */
    public DataService createDataService(ServerConnectionProfile profile) throws ConnectionException {
        return connect(profile).getDataService();
    }

    /**
     * {@inheritDoc}
     */
    public SearchService createSearchService(ServerConnectionProfile profile) throws ConnectionException {
        return connect(profile).getSearchService();
    }

    private CaArrayServer connect(ServerConnectionProfile profile) throws ConnectionException {
        CaArrayServer server = new CaArrayServer(profile.getHostname(), profile.getPort());
        try {
            if (StringUtils.isEmpty(profile.getUsername())) {
                server.connect();
            } else {
                server.connect(profile.getUsername(), profile.getPassword());
            }
        } catch (FailedLoginException e) {
            throw new ConnectionException("Login to the specified server failed", e);
        } catch (ServerConnectionException e) {
            throw new ConnectionException("Couldn't connect to the specified server", e);
        } catch (EJBException e) {
            /**
             * For some reason (possibly related to differences in java versions between caArray
             * and caIntegrator, EJBAccessExceptions are only being caught by the generic EJBException (see CAINT-1197).
             */
            throw new ConnectionException("Login to the specified server failed", e);
        }
        return server;
    }

}
