/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlot;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.web.SessionHelper;
import gov.nih.nci.caintegrator.web.action.AbstractSessionBasedTest;
import gov.nih.nci.caintegrator.web.action.analysis.geneexpression.GEPlotResult;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.xwork2.mock.MockActionInvocation;

public class GEPlotResultTest extends AbstractSessionBasedTest {

    @Test
    public void testExecute() throws IOException {
        ServletActionContext.setResponse(new MockHttpServletResponse());
        GeneExpressionPlot gePlot = mock(GeneExpressionPlot.class);

        GeneExpressionPlotGroup plotGroup = new GeneExpressionPlotGroup();
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.MEAN, gePlot);
        SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, plotGroup);
        GEPlotResult result = new GEPlotResult();
        result.setCalculationType(PlotCalculationTypeEnum.MEAN.getValue());
        result.setPlotType(PlotTypeEnum.ANNOTATION_BASED.getValue());
        result.execute(new MockActionInvocation());

        verify(gePlot, times(1)).writePlotImage(any(OutputStream.class));
        verifyNoMoreInteractions(gePlot);

        result.setCalculationType("Invalid Type");
        result.execute(new MockActionInvocation());

        result.setPlotType("Invalid Plot Type");
        result.execute(new MockActionInvocation());
    }
}
