/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.external.cadsr;

import gov.nih.nci.caintegrator.external.ConnectionException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import org.springframework.stereotype.Component;

/**
 * Implementation of the CaDSRApplicationServiceFactory.
 */
@Component
public class CaDSRApplicationServiceFactoryImpl implements CaDSRApplicationServiceFactory {
    private static final String CADSR_SERVICE = "CaDsrServiceInfo";

    /**
     * {@inheritDoc}
     */
    public ApplicationService retrieveCaDsrApplicationService(String caDsrUrl) throws ConnectionException {
        try {
            return ApplicationServiceProvider.getApplicationServiceFromUrl(caDsrUrl, CADSR_SERVICE);
        } catch (Exception e) {
            throw new ConnectionException("Error occurred when trying to create caDSR Application Service.", e);
        }
    }
}
