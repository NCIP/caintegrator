/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.external.cabio;

import gov.nih.nci.caintegrator2.external.ConnectionException;
import gov.nih.nci.system.applicationservice.CaBioApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

/**
 * Implementation of the CaBIOApplicationServiceFactory.
 */
public class CaBioApplicationServiceFactoryImpl implements CaBioApplicationServiceFactory {
    private static final String CABIO_SERVICE = "CaBioServiceInfo";

    /**
     * {@inheritDoc}
     */
    public CaBioApplicationService retrieveCaBioApplicationService(String caBioUrl) throws ConnectionException {
        try {
            return (CaBioApplicationService)
            ApplicationServiceProvider.getApplicationServiceFromUrl(
                    caBioUrl, CABIO_SERVICE);
        } catch (Exception e) {
            throw new ConnectionException("Error occurred when trying to create caBIO Application Service.", e);
        }
    }
}
