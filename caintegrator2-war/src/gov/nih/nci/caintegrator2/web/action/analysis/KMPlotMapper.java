/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See https://github.com/NCIP/caintegrator2/blob/master/LICENSEfor details.
 */
package gov.nih.nci.caintegrator2.web.action.analysis;

import gov.nih.nci.caintegrator2.application.kmplot.KMPlot;
import gov.nih.nci.caintegrator2.application.kmplot.PlotTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Object that is used to store KM Plots on the session, based on a maximum of one per plot type.
 */
public class KMPlotMapper {
    
    private final Map<PlotTypeEnum, KMPlot> kmPlotMap = new HashMap<PlotTypeEnum, KMPlot>();

    /**
     * Clears the map of KM Plots.
     */
    public void clear() {
        kmPlotMap.clear();
    }
    
    /**
     * @return the kmPlotMap
     */
    public Map<PlotTypeEnum, KMPlot> getKmPlotMap() {
        return kmPlotMap;
    }
    
    /**
     * Returns the Annotation Based KMPlot.
     * @return KMPlot object.
     */
    public KMPlot getAnnotationBasedKmPlot() {
        return kmPlotMap.get(PlotTypeEnum.ANNOTATION_BASED);
    }
    
    /**
     * Returns the Gene Expression based KMPlot.
     * @return KMPlot object.
     */
    public KMPlot getGeneExpressionBasedKmPlot() {
        return kmPlotMap.get(PlotTypeEnum.GENE_EXPRESSION);
    }
    
    /**
     * Returns the Query based KMPlot.
     * @return KMPlot object.
     */
    public KMPlot getQueryBasedKmPlot() {
        return kmPlotMap.get(PlotTypeEnum.QUERY_BASED);
    }

}
