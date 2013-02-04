/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.ncia;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.ncia.NCIAServiceFactoryImpl;

import org.junit.Test;


public class NCIASearchServiceImplTest {

    @Test(expected = ConnectionException.class)
    public void testCreateNCIACoreService() throws ConnectionException {
        ServerConnectionProfile profile = new ServerConnectionProfile();
        profile.setHostname("localhost");
        profile.setPort(1234);
        profile.setUsername("dummy_username");
        profile.setPassword("dummy_password");
        NCIAServiceFactoryImpl nciaServiceClient = new NCIAServiceFactoryImpl();
        nciaServiceClient.createNCIASearchService(profile);
    }
}
