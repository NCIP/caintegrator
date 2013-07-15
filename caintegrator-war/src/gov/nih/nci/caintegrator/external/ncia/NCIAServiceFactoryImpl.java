/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import gov.nih.nci.cagrid.metadata.exceptions.ResourcePropertyRetrievalException;
import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;
import org.springframework.stereotype.Component;

/**
 * Implementation of the NCIAServiceFactory.
 */
@Component
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
