/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.arraydata;

import gov.nih.nci.caintegrator2.domain.genomic.PlatformConfiguration;

/**
 * Interface realized by clients that need updates of deployment status.
 */
public interface PlatformDeploymentListener {

    /**
     * Invoked when deployment status has changed.
     * 
     * @param configuration the configuration between deployed.
     */
    void statusUpdated(PlatformConfiguration configuration);
    
}
