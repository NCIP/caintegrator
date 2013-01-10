/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.listener;

import gov.nih.nci.caintegrator2.application.analysis.SessionAnalysisResultsManager;
import gov.nih.nci.caintegrator2.file.AnalysisFileManager;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * When a session is destroyed, this listener will remove things associated with the session.
 */
public class SessionCleanupListener implements HttpSessionListener {

    /**
     * {@inheritDoc}
     */
    public void sessionCreated(HttpSessionEvent event) {
        // Do nothing on session create.
    }

    /**
     * {@inheritDoc}
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        String sessionId = event.getSession().getId();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(
                event.getSession().getServletContext());
        SessionAnalysisResultsManager sessionAnalysisResultsManager =
            (SessionAnalysisResultsManager) context.getBean("sessionAnalysisResultsManager");
        AnalysisFileManager fileManager = (AnalysisFileManager) context.getBean("analysisFileManager");
        sessionAnalysisResultsManager.removeSession(sessionId);
        fileManager.deleteSessionDirectories(sessionId);
    }

}
