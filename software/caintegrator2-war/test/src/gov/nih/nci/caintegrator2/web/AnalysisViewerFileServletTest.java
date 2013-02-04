/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.TestDataFiles;
import gov.nih.nci.caintegrator2.application.analysis.SessionAnalysisResultsManager;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.heatmap.HeatmapResult;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVFileTypeEnum;
import gov.nih.nci.caintegrator2.application.analysis.igv.IGVResult;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * 
 */
public class AnalysisViewerFileServletTest {
    
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        IGVMockHttpServletResponse response = new IGVMockHttpServletResponse();
        
        // Invalid URI format
        request.setRequestURI("/invalid");
        assertFalse(response.outputStreamWritten);
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Invalid session and invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=invalid&viewer=igv&file=invalidFile.xml");
        request.setParameter("JSESSIONID", "invalid");
        request.setParameter("viewer", "igv");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file=invalidFile.xml");
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Valid session, valid file format, file exists
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file=" 
                + IGVFileTypeEnum.SESSION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.SESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertTrue(response.outputStreamWritten);
        
        // Valid session, valid file format, segmentation file doesn't exist.
        response.outputStreamWritten = false;
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file=" 
                + IGVFileTypeEnum.SEGMENTATION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.SEGMENTATION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Valid session, valid file format, expression file exists.
        response.outputStreamWritten = false;
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file=" 
                + IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertTrue(response.outputStreamWritten);
        
        // Valid session, valid file format, expression file exists.
        response.outputStreamWritten = false;
        result.setSampleInfoFile(TestDataFiles.VALID_FILE);
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file=" 
                + IGVFileTypeEnum.SAMPLE_CLASSIFICATION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertTrue(response.outputStreamWritten);
        
        // Valid session, valid file format, but the job result is off the session.
        IGVResult igvResult = null;
        resultsManager.storeJobResult(sessionId, igvResult);
        response.outputStreamWritten = false;
        request.setRequestURI("/viewer//retrieveFile.do?JSESSIONID=SessionId&viewer=igv&file=" 
                + IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "igv");
        request.setParameter("file", IGVFileTypeEnum.GENE_EXPRESSION.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        
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
        MockHttpServletRequest request = new MockHttpServletRequest();
        IGVMockHttpServletResponse response = new IGVMockHttpServletResponse();
        
        // Invalid URI format
        request.setRequestURI("/invalid");
        assertFalse(response.outputStreamWritten);
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Invalid session and invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=invalid&viewer=heatmap&file=invalidFile.xml");
        request.setParameter("JSESSIONID", "invalid");
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Invalid file.
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file=invalidFile.xml");
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", "invalidFile.xml");
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Valid session, valid file format, file exists
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file=" 
                + HeatmapFileTypeEnum.LAYOUT.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.LAYOUT.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertTrue(response.outputStreamWritten);
        
        // Valid session, valid file format, genomic file doesn't exist.
        response.outputStreamWritten = false;
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file=" 
                + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        // Valid session, valid file format, expression file exists.
        response.outputStreamWritten = false;
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file=" 
                + HeatmapFileTypeEnum.LAYOUT.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.LAYOUT.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertTrue(response.outputStreamWritten);
        
        // Valid session, valid file format, expression file exists.
        response.outputStreamWritten = false;
        result.setGenomicDataFile(TestDataFiles.VALID_FILE);
        request.setRequestURI("/viewer/retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file=" 
                + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertTrue(response.outputStreamWritten);
        
        // Valid session, valid file format, but the job result is off the session.
        HeatmapResult heatmapResult = null;
        resultsManager.storeJobResult(sessionId, heatmapResult);
        response.outputStreamWritten = false;
        request.setRequestURI("/viewer//retrieveFile.do?JSESSIONID=SessionId&viewer=heatmap&file=" 
                + HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        request.setParameter("JSESSIONID", sessionId);
        request.setParameter("viewer", "heatmap");
        request.setParameter("file", HeatmapFileTypeEnum.GENOMIC_DATA.getFilename());
        analysisViewerFileServlet.handleRequest(request, response);
        assertFalse(response.outputStreamWritten);
        
        
    }
    
    private static class IGVMockHttpServletResponse extends MockHttpServletResponse {
        
        public boolean outputStreamWritten = false;
        
        @Override
        public ServletOutputStream getOutputStream() {
            outputStreamWritten = true;
            return super.getOutputStream();
        }
    }

}
