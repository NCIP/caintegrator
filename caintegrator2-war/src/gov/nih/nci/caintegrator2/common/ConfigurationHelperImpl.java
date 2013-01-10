/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.common;

import javax.sql.DataSource;

import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.DatabaseConfiguration;

/**
 * Helper class for retrieving system configuration.
 */
public final class ConfigurationHelperImpl implements ConfigurationHelper {
    
    private static final String TABLE_NAME = "CONFIGURATION_PARAMETER";
    private static final String PARAM_NAME_COLUMN = "PARAMETER";
    private static final String PARAM_VALUE_COLUMN = "RAW_VALUE";
    
    private DataSource dataSource;

    /**
     * Creates a new instance.
     */
    public ConfigurationHelperImpl() {
        super();
    }

    private DataConfiguration getConfiguration() {
        DatabaseConfiguration config = 
            new DatabaseConfiguration(dataSource, TABLE_NAME, PARAM_NAME_COLUMN, PARAM_VALUE_COLUMN);
        config.setDelimiterParsingDisabled(true);
        return new DataConfiguration(config);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getString(ConfigurationParameter parameter) {
        return getConfiguration().getString(parameter.name(), parameter.getDefaultValue());
    }

    /**
     * @return the dataSource
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * @param dataSource the dataSource to set
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    
}
