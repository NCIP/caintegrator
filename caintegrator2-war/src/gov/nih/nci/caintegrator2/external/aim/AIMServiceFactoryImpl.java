/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.aim;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

/**
 * Implementation of the AIMServiceFactory.
 */
public class AIMServiceFactoryImpl implements AIMServiceFactory {

    /**
     * {@inheritDoc}
     */
    public AIMSearchService createAIMSearchService(ServerConnectionProfile connection) throws ConnectionException {
        try {
            return new AIMSearchServiceImpl(connection);
        } catch (MalformedURIException e) {
            throw new ConnectionException("Malformed URI.", e);
        } catch (RemoteException e) {
            throw new ConnectionException("Remote Connection Failed.", e);
        }
    }

}
