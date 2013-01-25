/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.common;

/**
 * Helper class for retrieving system configuration.
 */
public final class ConfigurationHelperStub implements ConfigurationHelper {
    
    public ConfigurationParameter parameterPassed;
    public boolean getStringCalled;

    /**
     * Creates a new instance.
     */
    public ConfigurationHelperStub() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public String getString(ConfigurationParameter parameter) {
        parameterPassed = parameter;
        getStringCalled = true;
        return parameter.getDefaultValue();
    }

    public void clear() {
        parameterPassed = null;
        getStringCalled = false;
    }

    
}
