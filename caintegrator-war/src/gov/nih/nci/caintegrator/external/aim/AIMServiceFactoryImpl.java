/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.aim;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

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
