/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator/blob/master/LICENSE for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlot;
import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.geneexpression.PlotCalculationTypeEnum;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator.web.SessionHelper;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * Struts2 result type for a GE Plot graph.
 */
public class GEPlotResult implements Result {

    private static final long serialVersionUID = 1L;
    private String plotType;
    private String calculationType;

    /**
     * {@inheritDoc}
     */
    public void execute(ActionInvocation invocation) throws IOException {
        if (PlotTypeEnum.checkType(plotType)) {
            GeneExpressionPlotGroup gePlots = retrieveGePlots();
            GeneExpressionPlot gePlot = gePlots.getPlot(PlotCalculationTypeEnum.getByValue(calculationType));
            if (gePlot != null) {
                HttpServletResponse response = ServletActionContext.getResponse();
                gePlot.writePlotImage(response.getOutputStream());
                response.setContentType("image/png");
                response.getOutputStream().flush();
            }
        }
        
    }

    private GeneExpressionPlotGroup retrieveGePlots() {
        switch (PlotTypeEnum.getByValue(plotType)) {
        case ANNOTATION_BASED:
            return SessionHelper.getAnnotationBasedGePlots();
        case GENOMIC_QUERY_BASED:
            return SessionHelper.getGenomicQueryBasedGePlots();
        case CLINICAL_QUERY_BASED:
            return SessionHelper.getClinicalQueryBasedGePlots();
        default:
            return null;
        }
    }


    /**
     * @return the plotType
     */
    public String getPlotType() {
        return plotType;
    }


    /**
     * @param plotType the plotType to set
     */
    public void setPlotType(String plotType) {
        this.plotType = plotType;
    }


    /**
     * @return the calculationType
     */
    public String getCalculationType() {
        return calculationType;
    }


    /**
     * @param calculationType the calculationType to set
     */
    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }
}
