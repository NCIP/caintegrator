/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;
import gov.nih.nci.caintegrator2.web.SessionHelper;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

/**
 * Struts2 result type for a KM Plot graph.
 */
public class KMPlotResult implements Result {

    private static final long serialVersionUID = 1L;
    private String type;

    /**
     * {@inheritDoc}
     */
    public void execute(ActionInvocation invocation) throws IOException {
        if (PlotTypeEnum.checkType(type)) {
            KMPlot kmPlot = retrieveKmPlot();
            if (kmPlot != null) {
                HttpServletResponse response = ServletActionContext.getResponse();
                kmPlot.writePlotImage(response.getOutputStream());
                response.setContentType("image/png");
                response.getOutputStream().flush();
            }
        }
        
    }

    private KMPlot retrieveKmPlot() {
        KMPlot kmPlot;
        switch (PlotTypeEnum.getByValue(type)) {
        case ANNOTATION_BASED:
            kmPlot = SessionHelper.getAnnotationBasedKmPlot();
            break;
        case GENE_EXPRESSION:
            kmPlot = SessionHelper.getGeneExpressionBasedKmPlot();
            break;
        case QUERY_BASED:
            kmPlot = SessionHelper.getQueryBasedKmPlot();
            break;
        default:
            kmPlot = null;
            break;
        }
        return kmPlot;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
}
