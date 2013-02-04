/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.registration;

import gov.nih.nci.caintegrator2.application.registration.RegistrationRequest;
import gov.nih.nci.caintegrator2.application.registration.RegistrationService;
import gov.nih.nci.caintegrator2.common.ConfigurationHelper;
import gov.nih.nci.caintegrator2.common.ConfigurationParameter;
import gov.nih.nci.caintegrator2.security.SecurityManager;
import gov.nih.nci.security.exceptions.internal.CSInternalConfigurationException;
import gov.nih.nci.security.exceptions.internal.CSInternalInsufficientAttributesException;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

/**
 * Action for registering a user to cai2.
 */
@SuppressWarnings("PMD.ReplaceHashtableWithMap") // LDAPHelper.authenticate uses a Hashtable.
public class RegistrationAction extends ActionSupport implements Preparable {

    private static final long serialVersionUID = 1L;
    
    private SecurityManager securityManager;
    private RegistrationService registrationService;
    private ConfigurationHelper configurationHelper;
    
    private final Map<String, String> ldapContextParams = new HashMap<String, String>();
    private RegistrationRequest registrationRequest = new RegistrationRequest();
    private Boolean ldapAuthenticate;
    private String password;
    
    /**
     * {@inheritDoc}
     */
    public void prepare() {
        ldapAuthenticate = Boolean.TRUE;
        ldapContextParams.putAll(registrationService.getLdapContextParams());
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        if (isLdapInstall() && ldapAuthenticate) {
            if (!validateLoginName()) {
                return;
            }
            if (StringUtils.isBlank(password)) {
                addFieldError("password", "Must enter a password for LDAP authentication.");
                return;
            }
            validateLdap();
        }
    }

    
    private boolean validateLoginName() {
        if (StringUtils.isBlank(registrationRequest.getLoginName())) {
            addFieldError("registrationRequest.loginName", "Username required for LDAP account.");
            return false;
        }
        if (securityManager.doesUserExist(registrationRequest.getLoginName())) {
           addFieldError("registrationRequest.loginName", "Login name is already in use.");
           return false;
        }
        return true;
    }


    private void validateLdap() {
        try {
            if (!registrationService.ldapAuthenticate(ldapContextParams, registrationRequest
                    .getLoginName(), password)) {
                addActionError("LDAP Authentication Failure.  " 
                             + "Please verify that your LDAP username and password are correct.");
            }
        } catch (CSInternalConfigurationException e) {
            addActionError(e.getMessage());
        } catch (CSInternalInsufficientAttributesException e) {
            addActionError(e.getMessage());
        }
    }

    /**
     * Action to actually save the registration with authentication.
     * @return struts result.
     */
    public String save() {
        try {
            registrationRequest.setUptUrl(configurationHelper.getString(ConfigurationParameter.UPT_URL));
            registrationService.registerUser(registrationRequest);
            return SUCCESS;
        } catch (MessagingException e) {
            addActionError("Failed to send email due to the following: " + e.getMessage());
            return INPUT;
        }
    }
    
    /**
     * If user wishes to cancel.
     * @return cancel string.
     */
    public String cancel() {
        return "cancel";
    }

    /**
     * @return the registrationRequest
     */
    @VisitorFieldValidator(message = "")
    public RegistrationRequest getRegistrationRequest() {
        return registrationRequest;
    }

    /**
     * @param registrationRequest the registrationRequest to set
     */
    public void setRegistrationRequest(RegistrationRequest registrationRequest) {
        this.registrationRequest = registrationRequest;
    }

    /**
     * @return the registrationService
     */
    public RegistrationService getRegistrationService() {
        return registrationService;
    }

    /**
     * @param registrationService the registrationService to set
     */
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    /**
     * @return the securityManager
     */
    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    /**
     * @param securityManager the securityManager to set
     */
    public void setSecurityManager(SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * @return is ldap install?
     */
    public boolean isLdapInstall() {
        return !ldapContextParams.isEmpty();
    }

    /**
     * @return the ldapAuthenticate
     */
    public Boolean getLdapAuthenticate() {
        return ldapAuthenticate;
    }

    /**
     * @param ldapAuthenticate the ldapAuthenticate to set
     */
    public void setLdapAuthenticate(Boolean ldapAuthenticate) {
        this.ldapAuthenticate = ldapAuthenticate;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
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
