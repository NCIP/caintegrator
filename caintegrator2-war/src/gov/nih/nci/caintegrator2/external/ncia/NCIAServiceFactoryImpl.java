/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

/**
 * Implementation of the NCIAServiceFactory.
 */
public class NCIAServiceFactoryImpl implements NCIAServiceFactory {

    /**
     * {@inheritDoc}
     */
    public NCIASearchService createNCIASearchService(ServerConnectionProfile profile) throws ConnectionException {
        try {
            return new NCIASearchServiceImpl(profile, NBIAVersionUtil.getVersion(profile.getUrl()));
        } catch (MalformedURIException e) {
            throw new ConnectionException("Malformed URI.", e);
        } catch (RemoteException e) {
            throw new ConnectionException("Remote Connection Failed.", e);
        } catch (ResourcePropertyRetrievalException e) {
            throw new ConnectionException("Remote Connection Failed while trying to look up NBIA version.", e);
        }
    }

}
