/**
 * Copyright 5AM Solutions Inc, ESAC, ScenPro & SAIC
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/caintegrator/LICENSE.txt for details.
 */
package gov.nih.nci.caintegrator.web.action.analysis.geneexpression;

import gov.nih.nci.caintegrator.application.geneexpression.GeneExpressionPlotGroup;
import gov.nih.nci.caintegrator.application.kmplot.PlotTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Object that is used to store GE Plots on the session, based on a maximum of one per plot type.
 */
public class GEPlotMapper {
    
    private final Map<PlotTypeEnum, GeneExpressionPlotGroup> gePlotMap = 
                        new HashMap<PlotTypeEnum, GeneExpressionPlotGroup>();

    /**
     * Clears the map of GE Plots.
     */
    public void clear() {
        gePlotMap.clear();
    }
    
    /**
     * @return the gePlotMap
     */
    public Map<PlotTypeEnum, GeneExpressionPlotGroup> getGePlotMap() {
        return gePlotMap;
    }
    
    /**
     * Returns the Annotation Based GeneExpressionPlotGroup.
     * @return GeneExpressionPlotGroup object.
     */
    public GeneExpressionPlotGroup getAnnotationBasedGePlot() {
        return gePlotMap.get(PlotTypeEnum.ANNOTATION_BASED);
    }
    
    /**
     * Returns the Query based GeneExpressionPlotGroup.
     * @return GeneExpressionPlotGroup object.
     */
    public GeneExpressionPlotGroup getGenomicQueryBasedGePlot() {
        return gePlotMap.get(PlotTypeEnum.GENOMIC_QUERY_BASED);
    }
    
    /**
     * Returns the Clinical Query based GeneExpressionPlotGroup.
     * @return GeneExpressionPlotGroup object.
     */
    public GeneExpressionPlotGroup getClinicalQueryBasedGePlot() {
        return gePlotMap.get(PlotTypeEnum.CLINICAL_QUERY_BASED);
    }

}
