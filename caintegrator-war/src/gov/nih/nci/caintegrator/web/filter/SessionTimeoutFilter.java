/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.filter;

import gov.nih.nci.caintegrator.file.AnalysisFileManager;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * When the session destroyed, MySessionListener will do necessary logout operations.
 * Later, at the first request of client, this filter will be fired and redirect
 * the user to the appropriate timeout page if the session is not valid.
 *
 * @author hturksoy
 *
 */
public class SessionTimeoutFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(SessionTimeoutFilter.class);

    private static final String TIMEOUT_PAGE = "sessionTimeout.action";
    private AnalysisFileManager fileManager;
    private static boolean firstTime = true;

    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        // no-op
    }

    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
    throws IOException, ServletException {

        if (firstTime) {
            getFileManager().deleteAllTempAnalysisDirectories();
            firstTime = false;
        }
        if (sessionExpired(request, response)) {
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean sessionExpired(ServletRequest request, ServletResponse response) throws IOException {
        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            // is session expire control required for this request?
            // and is session invalid?
            if (isSessionControlRequiredForThisResource(httpServletRequest)
                    && isSessionInvalid(httpServletRequest)) {
                String timeoutUrl = httpServletRequest.getContextPath() + "/" + TIMEOUT_PAGE;
                LOGGER.debug("session is invalid! redirecting to timeoutpage : " + timeoutUrl);
                httpServletResponse.sendRedirect(timeoutUrl);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * session shouldn't be checked for some pages. For example: for timeout page.. Since we're redirecting to timeout
     * page from this filter, if we don't disable session control for it, filter will again redirect to it and this will
     * be result with an infinite loop...
     */
    private boolean isSessionControlRequiredForThisResource(HttpServletRequest httpServletRequest) {
        String requestPath = httpServletRequest.getRequestURI();
        boolean controlRequired = !StringUtils.contains(requestPath, TIMEOUT_PAGE)
            && !StringUtils.endsWithIgnoreCase(requestPath, "login.action");

        return controlRequired;
    }

    private boolean isSessionInvalid(HttpServletRequest httpServletRequest) {
        boolean sessionInvalid = (httpServletRequest.getRequestedSessionId() != null)
                && !httpServletRequest.isRequestedSessionIdValid();
        return sessionInvalid;
    }

    /**
     * {@inheritDoc}
     */
    public void destroy() {
        // No-op
    }

    /**
     * @return the fileManager
     */
    public AnalysisFileManager getFileManager() {
        return fileManager;
    }

    /**
     * @param fileManager the fileManager to set
     */
    public void setFileManager(AnalysisFileManager fileManager) {
        this.fileManager = fileManager;
    }

}
