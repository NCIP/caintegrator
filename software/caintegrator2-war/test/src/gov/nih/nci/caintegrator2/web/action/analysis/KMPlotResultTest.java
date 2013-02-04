/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.AcegiAuthenticationStub;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.io.IOException;
import java.util.HashMap;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.mock.MockActionInvocation;

/**
 * 
 */
public class KMPlotResultTest {

    @Test
    public void testExecute() throws IOException {
        ServletActionContext.setResponse(new MockHttpServletResponse());
        SecurityContextHolder.getContext().setAuthentication(new AcegiAuthenticationStub());
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        KMPlotStub kmPlot = new KMPlotStub();
        kmPlot.clear();
        SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, kmPlot);
        KMPlotResult result = new KMPlotResult();
        result.setType(PlotTypeEnum.ANNOTATION_BASED.getValue());
        result.execute(new MockActionInvocation());
        assertTrue(kmPlot.writePlotImageCalled);
        kmPlot.clear();
        SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, kmPlot);
        result.setType(PlotTypeEnum.GENE_EXPRESSION.getValue());
        result.execute(new MockActionInvocation());
        assertTrue(kmPlot.writePlotImageCalled);
        kmPlot.clear();
        result.setType("Invalid Type");
        result.execute(new MockActionInvocation());
        assertFalse(kmPlot.writePlotImageCalled);
        
    }

}
