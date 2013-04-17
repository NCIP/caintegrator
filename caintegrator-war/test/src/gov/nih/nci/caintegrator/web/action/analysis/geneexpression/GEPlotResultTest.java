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

import java.io.IOException;
import java.io.OutputStream;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.xwork2.mock.MockActionInvocation;

/**
 * Tests gene plot results retrieval.
 *
 * @author Abraham J. Evans-EL <aevansel@5amsolutions.com>
 */
public class GEPlotResultTest extends AbstractSessionBasedTest {

    /**
     * Tests execution of method.
     * @throws IOException on unexpected error
     */
    @Test
    public void execute() throws IOException {
        ServletActionContext.setResponse(new MockHttpServletResponse());
        GeneExpressionPlot gePlot = mock(GeneExpressionPlot.class);

        GeneExpressionPlotGroup plotGroup = new GeneExpressionPlotGroup();
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.MEAN, gePlot);
        SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, plotGroup);
        GEPlotResult result = new GEPlotResult();
        result.setCalculationType(PlotCalculationTypeEnum.MEAN);
        result.setPlotType(PlotTypeEnum.ANNOTATION_BASED);
        result.execute(new MockActionInvocation());

        verify(gePlot, times(1)).writePlotImage(any(OutputStream.class));
        verifyNoMoreInteractions(gePlot);

        result.setCalculationType(null);
        result.execute(new MockActionInvocation());

        result.setPlotType(null);
        result.execute(new MockActionInvocation());
    }
}
