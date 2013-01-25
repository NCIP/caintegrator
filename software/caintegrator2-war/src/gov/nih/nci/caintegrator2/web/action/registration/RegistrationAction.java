/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
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

import org.apache.commons.lang3.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.validator.annotations.VisitorFieldValidator;

/**
 * Action for registering a user to cai2.
 */
public class RegistrationAction extends ActionSupport implements Preparable {

    private static final long serialVersionUID = 1L;

    private SecurityManager securityManager;
    private RegistrationService registrationService;
    private ConfigurationHelper configurationHelper;

    private final Map<String, String> ldapContextParams = new HashMap<String, String>();
    private RegistrationRequest registrationRequest = new RegistrationRequest();
    private Boolean ldapAuthenticate;
    private String password;
    private String selectedPage;

    /**
     * {@inheritDoc}
     */
    @Override
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
                addFieldError("password", getText("struts.messages.error.registration.ldap.no.password"));
                return;
            }
            validateLdap();
        }
    }


    private boolean validateLoginName() {
        if (StringUtils.isBlank(registrationRequest.getLoginName())) {
            addFieldError("registrationRequest.loginName",
                    getText("struts.messages.error.registration.ldap.no.username"));
            return false;
        }
        if (securityManager.doesUserExist(registrationRequest.getLoginName())) {
           addFieldError("registrationRequest.loginName",
                   getText("struts.messages.error.registration.login.name.already.used"));
           return false;
        }
        return true;
    }


    private void validateLdap() {
        try {
            if (!registrationService.ldapAuthenticate(ldapContextParams, registrationRequest
                    .getLoginName(), password)) {
                addActionError(getText("struts.messages.error.registration.ldap.authentication"));
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
            addActionError(getText("struts.messages.error.registration.email.failure", new String[]{e.getMessage()}));
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

    /**
     * @return the selectedPage
     */
    public String getSelectedPage() {
        return selectedPage;
    }

    /**
     * @param selectedPage the selectedPage to set
     */
    public void setSelectedPage(String selectedPage) {
        this.selectedPage = selectedPage;
    }
}
