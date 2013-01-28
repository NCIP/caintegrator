/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.common;


/**
 * Helper for retrieving system configuration.
 */
public interface ConfigurationHelper {

    /**
     * Returns the configuration value for the given parameter.
     * 
     * @param parameter retrieve configuration value for this parameter
     * @return the configuration value.
     */
    String getString(ConfigurationParameter parameter);

}
