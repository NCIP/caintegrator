/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.data;

import java.io.IOException;

import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.security.authorization.instancelevel.InstanceLevelSecurityHelper;
import gov.nih.nci.security.exceptions.CSException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Allows for Instance Level security filtering for this session factory.
 */
public class SecureSessionFactory extends LocalSessionFactoryBean {

    private SecurityManager securityManager;
    private Resource configLocation;
    private String securityType;

    /**
     * {@inheritDoc}
     */
    @Override
    protected SessionFactory buildSessionFactory() throws IOException {
        Configuration configuration = new Configuration().configure(configLocation.getURL());
        try {
            if ("group".equals(securityType)) {
                InstanceLevelSecurityHelper.addFiltersForGroups(securityManager.getAuthorizationManager(),
                        configuration);
            } else {
                InstanceLevelSecurityHelper.addFilters(securityManager.getAuthorizationManager(),
                        configuration);
            }
        } catch (CSException e) {
            Logger.getLogger(SecureSessionFactory.class).error("Unable to add instance level security");
        }
        return newSessionFactory(configuration);
    }

    /**
     * @param securityManager the securityManager to set
     */
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * @param configLocation the configLocation to set
     */
    @Override
    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
        super.setConfigLocation(configLocation);
    }

    /**
     * @param securityType the securityType to set
     */
    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

}
