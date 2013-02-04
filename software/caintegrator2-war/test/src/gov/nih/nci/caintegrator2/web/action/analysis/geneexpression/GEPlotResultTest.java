/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis.geneexpression;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gov.nih.nci.caintegrator2.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator2.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;
import gov.nih.nci.caintegrator2.web.action.AbstractSessionBasedTest;

import java.io.IOException;

import org.apache.struts2.ServletActionContext;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import com.opensymphony.xwork2.mock.MockActionInvocation;

public class GEPlotResultTest extends AbstractSessionBasedTest {

    @Test
    public void testExecute() throws IOException {
        super.setUp();
        ServletActionContext.setResponse(new MockHttpServletResponse());
        GeneExpressionPlotStub meanBasedGePlot = new GeneExpressionPlotStub();
        meanBasedGePlot.clear();
        GeneExpressionPlotGroup plotGroup = new GeneExpressionPlotGroup();
        plotGroup.getGeneExpressionPlots().put(PlotCalculationTypeEnum.MEAN, meanBasedGePlot);
        SessionHelper.setGePlots(PlotTypeEnum.ANNOTATION_BASED, plotGroup);
        GEPlotResult result = new GEPlotResult();
        result.setCalculationType(PlotCalculationTypeEnum.MEAN.getValue());
        result.setPlotType(PlotTypeEnum.ANNOTATION_BASED.getValue());
        result.execute(new MockActionInvocation());
        assertTrue(meanBasedGePlot.writePlotImageCalled);
        meanBasedGePlot.clear();
        result.setCalculationType("Invalid Type");
        result.execute(new MockActionInvocation());
        assertFalse(meanBasedGePlot.writePlotImageCalled);
        result.setPlotType("Invalid Plot Type");
        result.execute(new MockActionInvocation());
        assertFalse(meanBasedGePlot.writePlotImageCalled);
    }

}
