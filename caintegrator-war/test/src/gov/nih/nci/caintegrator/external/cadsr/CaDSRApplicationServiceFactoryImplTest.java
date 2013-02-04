/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.cadsr;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.caintegrator.external.cadsr.CaDSRApplicationServiceFactoryImpl;

import org.junit.Test;

public class CaDSRApplicationServiceFactoryImplTest {

    @Test
    public void testRetrieveCaDsrApplicationService() throws ConnectionException {
        CaDSRApplicationServiceFactoryImpl caDsrApplicationServiceFactory = new CaDSRApplicationServiceFactoryImpl();
        caDsrApplicationServiceFactory.retrieveCaDsrApplicationService("random string");
    }

}
