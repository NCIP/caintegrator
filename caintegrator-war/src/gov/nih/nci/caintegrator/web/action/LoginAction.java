/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action;

import gov.nih.nci.caintegrator.application.quartz.DataRefreshJob;
import gov.nih.nci.caintegrator.security.SecurityHelper;
import gov.nih.nci.caintegrator.web.SessionHelper;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Action used to log in a user (may not actually need this action when using ACEGI).
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class LoginAction implements ServletContextAware {
    private static final String LOGIN = "login";
    private ServletContext servletContext;
    private DataRefreshJob dataRefreshRunner;

    /**
     * Opens login screen.
     * @return - login.
     */
    public String openLogin() {
        String ssoEnabled = servletContext.getInitParameter("ssoEnabled");
        String currentUser = SecurityHelper.getCurrentUsername();
        if (BooleanUtils.toBoolean(ssoEnabled) && SessionHelper.getInstance().isStudyManager()) {
            getDataRefreshRunner().setUser(currentUser);
            getDataRefreshRunner().startTask();
        }
        return LOGIN;
    }

    /**
     * @return the dataRefreshRunner
     */
    public DataRefreshJob getDataRefreshRunner() {
        return dataRefreshRunner;
    }

    /**
     * @param dataRefreshRunner the dataRefreshRunner to set
     */
    @Autowired
    @Qualifier("dataRefreshJob")
    public void setDataRefreshRunner(DataRefreshJob dataRefreshRunner) {
        this.dataRefreshRunner = dataRefreshRunner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setServletContext(ServletContext context) {
        this.servletContext = context;
    }
}
