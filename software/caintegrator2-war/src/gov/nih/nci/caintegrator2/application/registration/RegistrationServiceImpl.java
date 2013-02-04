/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.application.registration;

import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.common.EmailUtil;
import gov.nih.nci.security.authentication.helper.LDAPHelper;
import gov.nih.nci.security.exceptions.internal.CSInternalConfigurationException;
import gov.nih.nci.security.exceptions.internal.CSInternalInsufficientAttributesException;
import gov.nih.nci.security.exceptions.internal.CSInternalLoginException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.mail.MessagingException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

import org.apache.commons.lang3.StringUtils;
import org.jboss.security.auth.login.XMLLoginConfigImpl;

/**
 *
 */
public class RegistrationServiceImpl implements RegistrationService {

    private static final String REG_EMAIL_SUBJECT = "caIntegrator Registration";
    private static final String CONFIRM_EMAIL_SUBJECT = "caIntegrator Registration Confirmation";
    private static final String CONFIRM_EMAIL_CONTENT = "Dear NCICB User\n\nThank you for your submission concerning"
            + " caIntegrator registration request.  You will receive a followup call or email shortly.\n\nThank you,"
            + "\n\nNCICB Application Support Group";

    private ConfigurationHelper configurationHelper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerUser(RegistrationRequest registrationRequest) throws MessagingException {
        sendRegistrationConfirmation(registrationRequest);
        sendRegistrationRequestToAdmin(registrationRequest);
    }

    private void sendRegistrationConfirmation(RegistrationRequest registrationRequest) throws MessagingException {
        String emailFrom = configurationHelper.getString(ConfigurationParameter.REGISTRATION_EMAIL_FROM);
        EmailUtil.sendMail(Collections.singletonList(registrationRequest.getEmail()),
                emailFrom, CONFIRM_EMAIL_SUBJECT, CONFIRM_EMAIL_CONTENT);
    }


    private void sendRegistrationRequestToAdmin(RegistrationRequest registrationRequest) throws MessagingException {
        String admin = configurationHelper.getString(ConfigurationParameter.REGISTRATION_EMAIL_TO);
        String emailFrom = configurationHelper.getString(ConfigurationParameter.REGISTRATION_EMAIL_FROM);
        EmailUtil.sendMail(Collections.singletonList(admin), emailFrom, REG_EMAIL_SUBJECT,
                registrationRequest.getMailBody());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean ldapAuthenticate(Map<String, String> connectionProperties, String userID, String password)
            throws CSInternalConfigurationException, CSInternalInsufficientAttributesException {
        @SuppressWarnings("PMD.ReplaceHashtableWithMap") // LDAPHelper.authenticate uses a Hashtable.
        Hashtable<String, String> connectionPropertiesTable = new Hashtable<String, String>();
        connectionPropertiesTable.putAll(connectionProperties);
        try {
            return LDAPHelper.authenticate(connectionPropertiesTable, userID, password.toCharArray(), null);
        } catch (CSInternalLoginException e) {
            // CSM throws this exception on valid user / wrong pass
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> getLdapContextParams() {
        Map<String, String> ldapContextParams = new HashMap<String, String>();
        XMLLoginConfigImpl config = retrieveConfigFile();
        if (config == null) {
            return ldapContextParams;
        }
        AppConfigurationEntry[] entries = config.getAppConfigurationEntry("caintegrator2");
        for (AppConfigurationEntry entry : entries) {
            if (StringUtils.containsIgnoreCase(entry.getLoginModuleName(), "ldap")) {
                Map<String, String> entryMap = (Map<String, String>) entry.getOptions();
                for (String entryKey : entryMap.keySet()) {
                    ldapContextParams.put(entryKey, entryMap.get(entryKey));
                }
            }
        }
        return ldapContextParams;
    }

    @SuppressWarnings({ "unchecked" })
    private XMLLoginConfigImpl retrieveConfigFile() {
        try {
            XMLLoginConfigImpl config = (XMLLoginConfigImpl) java.security.AccessController
                .doPrivileged(new java.security.PrivilegedAction() {
                    @Override
                    public Object run() {
                        return Configuration.getConfiguration();
                    }
                });
        return config;
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * @return the configurationHelper
     */
    public ConfigurationHelper getConfigurationHelper() {
        return configurationHelper;
    }

    /**
     * @param configurationHelper the configurationHelper to set
     */
    public void setConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configurationHelper = configurationHelper;
    }

}
