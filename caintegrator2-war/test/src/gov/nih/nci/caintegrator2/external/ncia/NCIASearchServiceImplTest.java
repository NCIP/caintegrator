/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.ncia;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

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
