/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.external.caarray;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.ServerConnectionProfile;
import gov.nih.nci.caintegrator.external.caarray.CaArrayServiceFactoryImpl;

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
