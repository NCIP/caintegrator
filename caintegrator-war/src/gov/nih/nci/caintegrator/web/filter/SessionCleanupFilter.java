/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 *
 * Cleans out temporary analysis files when once a user's session has been marked invalid.
 *
 * @author hturksoy
 */
@Component
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class SessionCleanupFilter implements Filter {
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
        filterChain.doFilter(request, response);
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
    @Autowired
    public void setFileManager(AnalysisFileManager fileManager) {
        this.fileManager = fileManager;
    }
}