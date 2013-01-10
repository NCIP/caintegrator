/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.caarray;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.caintegrator2.external.ServerConnectionProfile;

import org.junit.Test;

public class CaArrayServiceFactoryImplTest {


    @Test(expected=ConnectionException.class)
    public void testCreateSearchService() throws ConnectionException {
        CaArrayServiceFactoryImpl factoryImpl = new CaArrayServiceFactoryImpl();
        ServerConnectionProfile profile = new ServerConnectionProfile();
        profile.setId(Long.valueOf(1));
        profile.setHostname("localhost");
        profile.setPort(1234);
        factoryImpl.createSearchService(profile);
    }

}
