/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */

package gov.nih.nci.caintegrator.web.action;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action for logging out of the application.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class LogoutAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    private final String casServerLogoutUrl;

    /**
     * Default class constructor.
     */
    public LogoutAction() {
        casServerLogoutUrl = ServletActionContext.getServletContext().getInitParameter("casServerLogoutUrl");
    }

    /**
     * Logs the user out of the application.
     * @return the struts2 forwarding result
     */
    public String logout() {
        if (ServletActionContext.getRequest().getSession() != null) {
            ServletActionContext.getRequest().getSession().invalidate();
        }
        return (casServerLogoutUrl != null) ? "casLogout" : SUCCESS;
    }

    /**
     * @return the casLogoutUrl
     */
    public String getCasServerLogoutUrl() {
        return casServerLogoutUrl;
    }
}
