/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.xwork2.mock.MockActionInvocation;

/**
 *
 */
public class KMPlotResultTest extends AbstractSessionBasedTest {

    @Test
    public void testExecute() throws IOException {
        ServletActionContext.setResponse(new MockHttpServletResponse());
        KMPlot kmPlot = mock(KMPlot.class);

        SessionHelper.setKmPlot(PlotTypeEnum.ANNOTATION_BASED, kmPlot);
        KMPlotResult result = new KMPlotResult();
        result.setType(PlotTypeEnum.ANNOTATION_BASED.getValue());
        result.execute(new MockActionInvocation());
        verify(kmPlot, times(1)).writePlotImage(any(OutputStream.class));

        SessionHelper.setKmPlot(PlotTypeEnum.GENE_EXPRESSION, kmPlot);
        result.setType(PlotTypeEnum.GENE_EXPRESSION.getValue());
        result.execute(new MockActionInvocation());
        verify(kmPlot, times(2)).writePlotImage(any(OutputStream.class));

        verifyNoMoreInteractions(kmPlot);
        result.setType("Invalid Type");
        result.execute(new MockActionInvocation());
    }
}
