/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.nih.nci.caintegrator.TestDataFiles;
import gov.nih.nci.caintegrator.application.analysis.SessionAnalysisResultsManager;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator.application.analysis.igv.IGVResult;
import gov.nih.nci.caintegrator.web.AnalysisViewerFileServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 *
 */
public class AnalysisViewerFileServletTest {
    private HttpServletResponse response;
    private MockHttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();

        response = mock(HttpServletResponse.class);
        ServletOutputStream os = mock(ServletOutputStream.class);
        when(response.getOutputStream()).thenReturn(os);
    }

    @Test
    public void testHandleRequestForIgv() throws ServletException, IOException {
        String sessionId = "SessionId";
        AnalysisViewerFileServlet analysisViewerFileServlet = new AnalysisViewerFileServlet();
        SessionAnalysisResultsManager resultsManager = new SessionAnalysisResultsManager();
        analysisViewerFileServlet.setSessionAnalysisResultsManager(resultsManager);
        IGVResult result = new IGVResult();
        result.setGeneExpressionFile(TestDataFiles.VALID_FILE);
        result.setSessionFile(TestDataFiles.VALID_FILE);

        resultsManager.storeJobResult(sessionId, result);


        // Invalid URI format
        request.setRequestURI("/invalid");
        verify(response, never()).getOutputStream();
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, never()).getOutputStream();

        // Invalid session and invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=invalid&viewer=igv&file=invalidFile.xml");
        request.setParameter("JSESSIONID", "invalid");
        request.setParameter("viewer", "igv");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, never()).getOutputStream();

        // Invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file=invalidFile.xml");
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, never()).getOutputStream();

        // Valid session, valid file format, file exists
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file="
                + IGVFileTypeEnum.SESSION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.SESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(1)).getOutputStream();

        // Valid session, valid file format, segmentation file doesn't exist.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file="
                + IGVFileTypeEnum.SEGMENTATION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.SEGMENTATION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(1)).getOutputStream();

        // Valid session, valid file format, expression file exists.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file="
                + IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(2)).getOutputStream();

        // Valid session, valid file format, expression file exists.
        result.setSampleInfoFile(TestDataFiles.VALID_FILE);
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file="
                + IGVFileTypeEnum.SAMPLE_CLASSIFICATION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(3)).getOutputStream();

        // Valid session, valid file format, but the job result is off the session.
        IGVResult igvResult = null;
        resultsManager.storeJobResult(sessionId, igvResult);
        request.setRequestURI("/viewer//retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file="
                + IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(3)).getOutputStream();
    }

    @Test
    public void testHandleRequestForHeatmap() throws ServletException, IOException {
        String sessionId = "SessionId";
        AnalysisViewerFileServlet analysisViewerFileServlet = new AnalysisViewerFileServlet();
        SessionAnalysisResultsManager resultsManager = new SessionAnalysisResultsManager();
        analysisViewerFileServlet.setSessionAnalysisResultsManager(resultsManager);
        HeatmapResult result = new HeatmapResult();
        result.setSampleAnnotationFile(TestDataFiles.VALID_FILE);
        result.setLayoutFile(TestDataFiles.VALID_FILE);
        result.setJnlpFile(TestDataFiles.VALID_FILE);

        resultsManager.storeJobResult(sessionId, result);

        // Invalid URI format
        request.setRequestURI("/invalid");
        verify(response, never()).getOutputStream();
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, never()).getOutputStream();

        // Invalid session and invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=invalid&viewer=heatmap&file=invalidFile.xml");
        request.setParameter("JSESSIONID", "invalid");
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, never()).getOutputStream();

        // Invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file=invalidFile.xml");
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, never()).getOutputStream();

        // Valid session, valid file format, file exists
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file="
                + HeatmapFileTypeEnum.LAYOUT.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.LAYOUT.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(1)).getOutputStream();

        // Valid session, valid file format, genomic file doesn't exist.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file="
                + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(1)).getOutputStream();

        // Valid session, valid file format, expression file exists.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file="
                + HeatmapFileTypeEnum.LAYOUT.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.LAYOUT.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(2)).getOutputStream();

        // Valid session, valid file format, expression file exists.
        result.setGenomicDataFile(TestDataFiles.VALID_FILE);
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file="
                + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(3)).getOutputStream();

        // Valid session, valid file format, but the job result is off the session.
        HeatmapResult heatmapResult = null;
        resultsManager.storeJobResult(sessionId, heatmapResult);
        request.setRequestURI("/viewer//retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file="
                + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        verify(response, times(3)).getOutputStream();
    }
}
