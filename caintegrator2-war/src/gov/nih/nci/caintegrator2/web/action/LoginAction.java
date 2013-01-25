/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator2.web.action;

import gov.nih.nci.caintegrator2.application.quartz.DataRefreshJob;
import gov.nih.nci.caintegrator2.security.SecurityHelper;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.struts2.util.ServletContextAware;

/**
 * Action used to log in a user (may not actually need this action when using ACEGI).
 */
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
