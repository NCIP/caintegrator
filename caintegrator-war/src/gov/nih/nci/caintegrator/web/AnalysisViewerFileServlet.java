/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web;

import gov.nih.nci.caintegrator.application.analysis.AnalysisViewerTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.SessionAnalysisResultsManager;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVFileTypeEnum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.HttpRequestHandler;

/**
 * The request URI will be of the format /caintegrator/igv/retrieveFile.do?JSESSIONID=1234567&file=igvSession.xml
 * An example URL (one full line):
 * 
 * http://www.broadinstitute.org/igv/dynsession/igv.jnlp?user=anonymous&sessionURL=
 *      http://localhost:8080/caintegrator/igv/retrieveFile.do%3FJSESSIONID%3D123456789%26file%3DigvSession.xml
 * 
 * Or instead of using broadinstitute.org could be:
 * 
 * http://localhost:60151/load?file=
 *      http://localhost:8080/caintegrator/igv/retrieveFile.do%3FJSESSIONID%3D123456789%26file%3DigvSession.xml
 * 
 * These URLs assume a caintegrator host being on the localhost machine.
 */
public class AnalysisViewerFileServlet implements HttpRequestHandler {
    /**
     * Session Parameter.
     */
    public static final String SESSION_PARAMETER = "JSESSIONID";
    /**
     * Filename parameter.
     */
    public static final String FILENAME_PARAMETER = "file";
    /**
     * ViewerType parameter.
     */
    public static final String VIEWERTYPE_PARAMETER = "viewer";
    
    private SessionAnalysisResultsManager sessionAnalysisResultsManager;
    
    
    /**
     * {@inheritDoc}
     */
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String sessionId = request.getParameter(SESSION_PARAMETER);
        String filename = request.getParameter(FILENAME_PARAMETER);
        String viewerType = request.getParameter(VIEWERTYPE_PARAMETER);
        if (AnalysisViewerTypeEnum.IGV.getValue().equalsIgnoreCase(viewerType)) {
            IGVFileTypeEnum fileType = IGVFileTypeEnum.getByFilename(filename);
            if (fileType != null) {
                streamIgvFile(response, sessionId, fileType);
            }
        } else if (AnalysisViewerTypeEnum.HEATMAP.getValue().equalsIgnoreCase(viewerType)) {
            HeatmapFileTypeEnum fileType = HeatmapFileTypeEnum.getByFilename(filename);
            if (fileType != null) {
                streamHeatmapFile(response, sessionId, fileType);
            }
        }
    }

    private void streamIgvFile(HttpServletResponse response, String sessionId, IGVFileTypeEnum fileType)
            throws IOException {
        File file = sessionAnalysisResultsManager.getJobResultFile(sessionId, fileType);
        if (file != null) {
            OutputStream outputStream = response.getOutputStream();
            FileInputStream inputStream = new FileInputStream(file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();
        }
    }
    
    private void streamHeatmapFile(HttpServletResponse response, String sessionId, HeatmapFileTypeEnum fileType)
    throws IOException {
        File file = sessionAnalysisResultsManager.getJobResultFile(sessionId, fileType);
        if (file != null) {
            if (HeatmapFileTypeEnum.LAUNCH_FILE.equals(fileType)) {
                response.setContentType("application/x-java-jnlp-file");
            }
            OutputStream outputStream = response.getOutputStream();
            FileInputStream inputStream = new FileInputStream(file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();
        }
    }

    /**
     * @return the sessionAnalysisResultsManager
     */
    public SessionAnalysisResultsManager getSessionAnalysisResultsManager() {
        return sessionAnalysisResultsManager;
    }

    /**
     * @param sessionAnalysisResultsManager the sessionAnalysisResultsManager to set
     */
    public void setSessionAnalysisResultsManager(SessionAnalysisResultsManager sessionAnalysisResultsManager) {
        this.sessionAnalysisResultsManager = sessionAnalysisResultsManager;
    }


}
