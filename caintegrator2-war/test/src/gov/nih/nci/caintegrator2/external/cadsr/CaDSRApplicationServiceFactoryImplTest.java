/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.external.cadsr;

import gov.nih.nci.caintegrator2.external.ConnectionException;

import org.junit.Test;

public class CaDSRApplicationServiceFactoryImplTest {

    @Test
    public void testRetrieveCaDsrApplicationService() throws ConnectionException {
        CaDSRApplicationServiceFactoryImpl caDsrApplicationServiceFactory = new CaDSRApplicationServiceFactoryImpl();
        caDsrApplicationServiceFactory.retrieveCaDsrApplicationService("random string");
    }

}
